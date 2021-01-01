package com.xxmicloxx.NoteBlockAPI.model;

import com.google.common.base.Preconditions;

import javax.sound.midi.*;
import java.io.*;

public class MidiSequence implements Playable {

    public final Sequence sequence;
    private final MidiFileFormat format;
    private final File file;

    public MidiSequence(File file) throws InvalidMidiDataException, IOException {
        Preconditions.checkNotNull(file);
        this.file = file;
        format = MidiSystem.getMidiFileFormat(file);
        sequence = MidiSystem.getSequence(file);
    }

    public MidiSequence(InputStream inputStream) throws InvalidMidiDataException, IOException {
        Preconditions.checkNotNull(inputStream);
        file = null;
        format = MidiSystem.getMidiFileFormat(inputStream);
        sequence = MidiSystem.getSequence(inputStream);
    }

    private MidiSequence(Sequence sequence, MidiFileFormat format) {
        Preconditions.checkNotNull(sequence);
        Preconditions.checkNotNull(format);
        file = null;
        this.sequence = sequence;
        this.format = format;
    }

    /**
     * Gets the length in ticks of this Song
     *
     * @return length of this song in ticks
     */
    @Override
    public long getLengthInTicks() {
        return sequence.getTickLength();
    }

    /**
     * Gets the title / name of this Song
     *
     * @return title of the Song
     */
    @Override
    public String getTitle() {
        return String.valueOf(format.getProperty("title"));
    }

    /**
     * Gets the author of the Song
     *
     * @return author
     */
    @Override
    public String getAuthor() {
        return String.valueOf(format.getProperty("author"));
    }

    /**
     * Returns the File from which this Song is sourced
     *
     * @return file of this Song
     */
    @Override
    public File getPath() {
        return file;
    }

    /**
     * Gets the description of this Song
     *
     * @return description
     */
    @Override
    public String getDescription() {
        return String.format("Copyright: %s\nDate: %s\n\n%s", format.getProperty("copyright"), format.getProperty("date"), format.getProperty("comment"));
    }

    /**
     * Gets the approximate speed (ticks per second) of this Song
     *
     * @return approximate speed
     */
    @Override
    public double getSpeed() {
        return sequence.getTickLength() / (sequence.getMicrosecondLength() / 1_000_000.0);
    }
}
