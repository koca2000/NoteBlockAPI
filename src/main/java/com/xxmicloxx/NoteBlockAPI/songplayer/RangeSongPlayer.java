package com.xxmicloxx.NoteBlockAPI.songplayer;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import org.jetbrains.annotations.NotNull;

/**
 * SongPlayer playing only in specified distance
 *
 */
public abstract class RangeSongPlayer extends SongPlayer{

	private int distance = 16;

	@Deprecated
	public RangeSongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	@Deprecated
	public RangeSongPlayer(@NotNull Song song) {
		super(song);
	}

	public RangeSongPlayer(@NotNull cz.koca2000.nbs4j.Song song) {
		super(song);
	}

	@Deprecated
	public RangeSongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory) {
		super(playlist, soundCategory);
	}

	public RangeSongPlayer(@NotNull Playlist playlist) {
		super(playlist);
	}

	/**
	 * Sets distance in blocks where would be player able to hear sound. 
	 * @param distance (Default 16 blocks)
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}
	
	/**
	 * Returns true if the Player is able to hear the current RangeSongPlayer 
	 * @param player in range
	 * @return ability to hear the current RangeSongPlayer
	 */
	public abstract boolean isInRange(Player player);
	
}
