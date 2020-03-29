package com.xxmicloxx.NoteBlockAPI.model.playmode;

import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class ChannelMode {

    public abstract void play(Player player, Location location, Song song, Layer layer, Note note,
                              SoundCategory soundCategory, float volume, float pitch);
}