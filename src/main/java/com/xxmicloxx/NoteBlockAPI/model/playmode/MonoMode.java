package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MonoMode extends ChannelMode {

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, float pitch) {
        if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
            CustomInstrument instrument = song.getCustomInstruments()[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

            if (instrument.getSound() != null) {
                CompatibilityUtils.playSound(player, location, instrument.getSound(), soundCategory, volume, pitch, 0);
            } else {
                CompatibilityUtils.playSound(player, location, instrument.getSoundFileName(), soundCategory, volume, pitch, 0);
            }
        } else {
            CompatibilityUtils.playSound(player, location, InstrumentUtils.getInstrument(note.getInstrument()), soundCategory, volume, pitch, 0);
        }
    }
}
