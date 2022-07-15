package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Uses panning for individual {@link cz.koca2000.nbs4j.Note} and {@link cz.koca2000.nbs4j.Layer} based on data from nbs file.
 */
public class StereoMode extends ChannelMode {

    private float maxDistance = 2;
    private ChannelMode fallbackChannelMode = null;

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, float pitch) {
        if (!song.isStereo() && fallbackChannelMode != null){
            fallbackChannelMode.play(player, location, song, layer, note, soundCategory, volume, pitch);
            return;
        }

        float distance = 0;
        if (layer.getPanning() == 100){
            distance = (note.getPanning() - 100) * maxDistance;
        } else {
            distance = ((layer.getPanning() - 100 + note.getPanning() - 100) / 200f) * maxDistance;
        }
        cz.koca2000.nbs4j.CustomInstrument customInstrument = InstrumentUtils.getCustomInstrumentForNote(note.getNote());
        if (customInstrument != null) {
            CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, distance);
        } else {
            CompatibilityUtils.playSound(player, location, InstrumentUtils.getInstrument((byte) note.getNote().getInstrument()), soundCategory, volume, pitch, distance);
        }
    }

    @Override
    public void play(Player player, Location location, Song song, Layer layer, Note note, SoundCategory soundCategory, float volume, boolean doTranspose) {
        play(player, location, song.getSong(), layer.getLayer(), note.getNote(), soundCategory, volume, doTranspose);
    }

    @Override
    public void play(Player player, Location location, cz.koca2000.nbs4j.Song song, cz.koca2000.nbs4j.Layer layer, cz.koca2000.nbs4j.Note note, SoundCategory soundCategory, float volume, boolean doTranspose) {
        if (!song.isStereo() && fallbackChannelMode != null){
            fallbackChannelMode.play(player, location, song, layer, note, soundCategory, volume, doTranspose);
            return;
        }

        float pitch = getPitch(note, doTranspose);

        float distance;
        if (layer.getPanning() == cz.koca2000.nbs4j.Layer.NEUTRAL_PANNING){
            distance = note.getPanning() * maxDistance;
        } else {
            distance = ((layer.getPanning() + note.getPanning()) / 200f) * maxDistance;
        }

        cz.koca2000.nbs4j.CustomInstrument customInstrument = InstrumentUtils.getCustomInstrumentForNote(note);
        if (customInstrument != null) {
            if (!doTranspose){
                CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange(customInstrument.getFileName(), (byte) note.getKey(), (short) note.getPitch()), soundCategory, volume, pitch, distance);
            } else {
                CompatibilityUtils.playSound(player, location, customInstrument.getFileName(), soundCategory, volume, pitch, distance);
            }
        } else {
            if (NoteUtils.isOutOfRange((byte) note.getKey(), (short) note.getPitch()) && !doTranspose) {
                CompatibilityUtils.playSound(player, location, InstrumentUtils.warpNameOutOfRange((byte) note.getInstrument(), (byte) note.getKey(), (short) note.getPitch()), soundCategory, volume, pitch, distance);
            } else {
                CompatibilityUtils.playSound(player, location, InstrumentUtils.getInstrument((byte) note.getInstrument()), soundCategory, volume, pitch, distance);
            }
        }
    }

    /**
     * Returns scale of panning in blocks. {@link cz.koca2000.nbs4j.Note} with maximum left panning will be played this distance from {@link Player}'s head on left side.
     * @return
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets scale of panning in blocks. {@link cz.koca2000.nbs4j.Note} with maximum left panning will be played this distance from {@link Player}'s head on left side.
     * @param maxDistance
     */
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Returns fallback {@link ChannelMode} used when song is not stereo.
     * @return ChannelMode or null when fallback ChannelMode is disabled
     */
    public ChannelMode getFallbackChannelMode() {
        return fallbackChannelMode;
    }

    /**
     * Sets fallback {@link ChannelMode} which is used when song is not stereo. Set to null to disable.
     * @param fallbackChannelMode
     * @throws IllegalArgumentException if parameter is instance of StereoMode
     */
    public void setFallbackChannelMode(ChannelMode fallbackChannelMode) {
        if (fallbackChannelMode instanceof StereoMode) throw new IllegalArgumentException("Fallback ChannelMode can't be instance of StereoMode!");

        this.fallbackChannelMode = fallbackChannelMode;
    }
}
