package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Ignores panning of {@link cz.koca2000.nbs4j.Note} and {@link cz.koca2000.nbs4j.Layer} from nbs format and plays mono {@link cz.koca2000.nbs4j.Note} as fake stereo at fixed offset from {@link Player} head.
 */
public class MonoStereoMode extends ChannelMode{

    private float distance = 2;

    @Deprecated
    @Override
    public void play(@NotNull Player player, @NotNull Location location, @NotNull Song song, @NotNull Layer layer, @NotNull Note note, @NotNull SoundCategory soundCategory, float volume, float pitch) {
        cz.koca2000.nbs4j.CustomInstrument customInstrument = InstrumentUtils.getCustomInstrumentForNote(note.getNote());
        if (customInstrument != null) {
            CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, distance);
            CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, -distance);
        } else {
            org.bukkit.Sound sound = InstrumentUtils.getInstrument((byte) note.getNote().getInstrument());
            CompatibilityUtils.playSound(player, location, sound, soundCategory, volume, pitch, distance);
            CompatibilityUtils.playSound(player, location, sound, soundCategory, volume, pitch, -distance);
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
                String instrumentName = InstrumentUtils.warpNameOutOfRange(customInstrument.getFileName(), (byte) note.getKey(), (short) note.getPitch());
                CompatibilityUtils.playSound(player, location, instrumentName, soundCategory, volume, pitch, distance);
                CompatibilityUtils.playSound(player, location, instrumentName, soundCategory, volume, pitch, -distance);
            } else {
                CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, distance);
                CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, -distance);
            }
        } else {
            if (NoteUtils.isOutOfRange((byte) note.getKey(), (short) note.getPitch()) && !doTranspose) {
                String instrumentName = InstrumentUtils.warpNameOutOfRange((byte) note.getInstrument(), (byte) note.getKey(), (short) note.getPitch());
                CompatibilityUtils.playSound(player, location, instrumentName, soundCategory, volume, pitch, distance);
                CompatibilityUtils.playSound(player, location, instrumentName, soundCategory, volume, pitch, -distance);
            } else {
                org.bukkit.Sound sound = InstrumentUtils.getInstrument((byte) note.getInstrument());
                CompatibilityUtils.playSound(player, location, sound, soundCategory, volume, pitch, distance);
                CompatibilityUtils.playSound(player, location, sound, soundCategory, volume, pitch, -distance);
            }
        }
    }

    /**
     * Returns distance of {@link cz.koca2000.nbs4j.Note} from {@link Player}'s head.
     * @return distance in blocks
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Sets distance of {@link cz.koca2000.nbs4j.Note} from {@link Player}'s head.
     * @param distance distance in blocks
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }
}
