package com.xxmicloxx.NoteBlockAPI.utils;

import com.google.common.base.Preconditions;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Song;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Decodes MIDI files to {@link Song} with high precision
 */
public class MIDIDecoder {

    /**
     * Parse a midi file to {@link Song}
     *
     * @param file Midi file
     * @return Decoded song
     * @throws InvalidMidiDataException corrupted midi file
     * @throws IOException              an IO exceptions occurs
     */
    public static Song parse(File file) throws InvalidMidiDataException, IOException {
        return parse(MidiSystem.getSequence(file));
    }

    /**
     * Parse a Midi {@link Sequence}
     *
     * @param sequence Midi sequence
     * @return Decoded song
     */
    public static Song parse(Sequence sequence) {
        return new MidiFile(sequence).getDecodedSong();
    }

    private static long lcm(long... num) {
        long current = 1;
        for (long n : num) {
            long result;
            Preconditions.checkArgument(n >= 1);

            long step, max;
            max = step = Math.max(current, n);

            while (true) {
                if (max % current == 0 && max % n == 0) {
                    result = max;
                    break;
                }
                max += step;
            }
            current = result;
        }
        return current;
    }

    /**
     * Represents a Midi file
     */
    private static class MidiFile extends AbstractList<MidiTrack> {

        private final List<MidiTrack> midiTracks = new ArrayList<>();
        private final Song decodedSong;

        MidiFile(Sequence sequence) {
            float framesPerSecond = sequence.getDivisionType(); // PPQ results 0 and others results framerate
            int resolution = sequence.getResolution(); // PPQ results t/beat and others results t/frame
            HashMap<Integer, Layer> layerHashMap = new HashMap<>(); // Converted layers
            for (Track track : sequence.getTracks())
                try {
                    midiTracks.add(new MidiTrack(track, sequence));
                } catch (EmptyTrackException ignored) {
                }

            decodedSong = new Song(1000, layerHashMap, layerHashMap.size(), sequence.getMicrosecondLength() / 1000,
                    );
        }

        public Song getDecodedSong() {
            return decodedSong;
        }

        @Override
        public MidiTrack get(int i) {
            return midiTracks.get(i);
        }

        @Override
        public int size() {
            return midiTracks.size();
        }
    }

    /**
     * Represents a Midi track
     */
    private static class MidiTrack extends AbstractList<MidiPart> {

        private static final int initialInstrument = 0;

        private final List<MidiPart> parts;
        private final String copyright;
        private final String name;
        private final int initialTPS;
        private final int initialChannel;

        private final MidiTrack(Track track, Sequence sequence) throws EmptyTrackException {
            if (track.size() <= 0) throw new EmptyTrackException(); // Empty track
            List<MidiEvent> midiEvents = new ArrayList<>(track.size()); // Midi events in current track
            for (int i = 0; i < track.size(); i++)
                midiEvents.add(track.get(i));
            midiEvents.sort(Comparator.comparingLong(MidiEvent::getTick)); // Sort Midi events by ticks
            List<MidiPart> result = new ArrayList<>();

            List<MetaMessage> metadata = new ArrayList<>(); // Get metadata
            for (MidiEvent event : midiEvents)
                if (event.getMessage() instanceof MetaMessage)
                    metadata.add((MetaMessage) event.getMessage());
                else break;
            midiEvents = midiEvents.subList(metadata.size(), midiEvents.size() - 1); // Remove header to prevent unwanted tempo change

            String copyright = null;
            String name = null;
            int initialTPS = sequence.getDivisionType() == 0F ? 2 * sequence.getResolution() :
                    Math.round(sequence.getResolution() * sequence.getDivisionType());
            int initialChannel = 0;
            for (MetaMessage metaMessage : metadata) {
                if (metaMessage.getStatus() == 0xFF) {
                    switch (metaMessage.getType()) {
                        case 0x02:
                            copyright = new String(metaMessage.getData());
                            break;
                        case 0x03:
                            name = new String(metaMessage.getData());
                            break;
                        case 0x20:
                            initialChannel = metaMessage.getData()[0];
                            break;
                        case 0x51:
                            if (sequence.getDivisionType() == 0F) {
                                long microsPerBeat = 0;
                                byte[] byteData = metaMessage.getData();
                                for (byte byteDatum : byteData) {
                                    microsPerBeat *= 0x100;
                                    microsPerBeat += Byte.toUnsignedInt(byteDatum);
                                }
                                if (microsPerBeat == 0) break;
                                initialTPS = Math.toIntExact(100_000 / microsPerBeat);
                            }
                            break;
                    }
                }
            }
            this.copyright = copyright;
            this.name = name;
            this.initialChannel = initialChannel;
            this.initialTPS = initialTPS;

            List<MidiEvent> currentEvents = new LinkedList<>();
            int currentTPS =
                    sequence.getDivisionType() == 0F
                            ? sequence.getResolution() * 2
                            : Math.toIntExact((long) (sequence.getDivisionType() * sequence.getResolution()));
            int currentInstrument = MidiTrack.initialInstrument;
            for (MidiEvent event : midiEvents) {
                MidiMessage message = event.getMessage();
                if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    if (metaMessage.getStatus() == 0xff && metaMessage.getType() == 0x51
                            && sequence.getDivisionType() == 0F) {
                        long microsPerBeat = 0;
                        byte[] byteData = metaMessage.getData();
                        for (byte byteDatum : byteData) {
                            microsPerBeat *= 0x100;
                            microsPerBeat += Byte.toUnsignedInt(byteDatum);
                        }
                        if (microsPerBeat == 0) continue;
                        int changedTPS = Math.toIntExact(1000000 / microsPerBeat) * sequence.getResolution();
                        result.add(new MidiPart(currentEvents, currentTPS, currentInstrument));
                        currentEvents = new LinkedList<>();
                        currentTPS = changedTPS;
                    } else
                        currentEvents.add(event);
                } else if (message instanceof ShortMessage) {
                    ShortMessage shortMessage = (ShortMessage) message;
                    if (shortMessage.getStatus() >= 0xC0 && shortMessage.getStatus() <= 0xCE) {
                        result.add(new MidiPart(currentEvents, currentTPS, currentInstrument));
                        currentEvents = new LinkedList<>();
                        currentInstrument = shortMessage.getData1();
                    }
                } else
                    currentEvents.add(event);
            }
            result.add(new MidiPart(currentEvents, currentTPS, currentInstrument));

            this.parts = Collections.unmodifiableList(result);
        }

        @Override
        public MidiPart get(int i) {
            return parts.get(i);
        }

        @Override
        public int size() {
            return parts.size();
        }

    }

    /**
     * Represents a Midi part with constant
     */
    private static class MidiPart extends AbstractList<MidiEvent> {

        final List<MidiEvent> midiEvents;
        final int tps;
        final int instrument;

        MidiPart(List<MidiEvent> midiEvents, int tps, int instrument) {
            this.midiEvents = Collections.unmodifiableList(midiEvents);
            this.tps = tps;
            this.instrument = instrument;
        }

        @Override
        public MidiEvent get(int i) {
            return midiEvents.get(i);
        }

        @Override
        public int size() {
            return midiEvents.size();
        }

        public int getTps() {
            return tps;
        }
    }

    private static class EmptyTrackException extends Exception {
    }

}
