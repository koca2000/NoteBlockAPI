package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * {@link Note} is played inside of {@link Player}'s head.
 */
public class MonoMode extends ChannelMode {

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, float pitch) {
        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(instrument.getSoundFileName(), note.getKey()), soundCategory, volume, pitch, 0);
        } else {
            CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(note.getInstrument(), note.getKey()), soundCategory, volume, pitch, 0);
        }
    }

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, boolean doTranspose) {
        float pitch;
        if(doTranspose)
            pitch = NoteUtils.getPitchTransposed(note);
        else
            pitch = NoteUtils.getPitch(note);
        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(instrument.getSoundFileName(), note.getKey()), soundCategory, volume, pitch, 0);
        } else {
            CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(note.getInstrument(), note.getKey()), soundCategory, volume, pitch, 0);
        }
    }
}
