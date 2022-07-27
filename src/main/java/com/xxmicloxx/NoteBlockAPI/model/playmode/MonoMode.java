package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link cz.koca2000.nbs4j.Note} is played inside of {@link Player}'s head.
 */
public class MonoMode extends ChannelMode {

    @Deprecated
    @Override
    public void play(@NotNull Player player, @NotNull Location location, @NotNull Song song, @NotNull Layer layer, @NotNull Note note, @NotNull SoundCategory soundCategory, float volume, float pitch) {
        cz.koca2000.nbs4j.CustomInstrument customInstrument = InstrumentUtils.getCustomInstrumentForNote(note.getNote());
        if (customInstrument != null) {
            CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, 0);
        } else {
            CompatibilityUtils.playSound(player, location, InstrumentUtils.getInstrument((byte) note.getNote().getInstrument()), soundCategory, volume, pitch, 0);
        }
    }

    @Deprecated
    @Override
    public void play(@NotNull Player player, @NotNull Location location, @NotNull Song song, @NotNull Layer layer, @NotNull Note note, @NotNull SoundCategory soundCategory, float volume, boolean doTranspose) {
        play(player, location, song.getSong(), layer.getLayer(), note.getNote(), soundCategory, volume, doTranspose);
    }

    @Override
    public void play(@NotNull Player player, @NotNull Location location, @NotNull cz.koca2000.nbs4j.Song song, @NotNull cz.koca2000.nbs4j.Layer layer, @NotNull cz.koca2000.nbs4j.Note note, @NotNull SoundCategory soundCategory, float volume, boolean doTranspose) {
        float pitch = getPitch(note, doTranspose);

        cz.koca2000.nbs4j.CustomInstrument customInstrument = InstrumentUtils.getCustomInstrumentForNote(note);
        if (customInstrument != null) {
            if (!doTranspose){
                CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(customInstrument.getFileName(), (byte) note.getKey(), (short) note.getPitch()), soundCategory, volume, pitch, 0);
            } else {
                CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, 0);
            }
        } else {
            if (NoteUtils.isOutOfRange((byte) note.getKey(), (short) note.getPitch()) && !doTranspose) {
                CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange((byte) note.getInstrument(), (byte) note.getKey(), (short) note.getPitch()), soundCategory, volume, pitch, 0);
            } else {
                CompatibilityUtils.playSound(player, location, InstrumentUtils.getInstrument((byte) note.getInstrument()), soundCategory, volume, pitch, 0);
            }
        }
    }
}
