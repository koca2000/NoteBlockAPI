package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Decides how is {@link cz.koca2000.nbs4j.Note} played to {@link Player}
 */
public abstract class ChannelMode {

    @Deprecated
    public abstract void play(@NotNull Player player, @NotNull Location location, @NotNull Song song, @NotNull Layer layer, @NotNull Note note,
                              @NotNull SoundCategory soundCategory, float volume, float pitch);

    @Deprecated
    public abstract void play(@NotNull Player player, @NotNull Location location, @NotNull Song song, @NotNull Layer layer, @NotNull Note note,
                              @NotNull SoundCategory soundCategory, float volume, boolean doTranspose);

    public abstract void play(@NotNull Player player, @NotNull Location location, @NotNull cz.koca2000.nbs4j.Song song, @NotNull cz.koca2000.nbs4j.Layer layer,
                              @NotNull cz.koca2000.nbs4j.Note note, @NotNull SoundCategory soundCategory, float volume, boolean doTranspose);

    protected float getPitch(@NotNull cz.koca2000.nbs4j.Note note, boolean doTranspose){
        return doTranspose ? NoteUtils.getPitchTransposed(note) : NoteUtils.getPitchInOctave(note);
    }
}