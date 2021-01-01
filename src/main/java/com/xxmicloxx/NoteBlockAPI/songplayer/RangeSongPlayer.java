package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.Playable;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;

/**
 * SongPlayer playing only in specified distance
 *
 */
public abstract class RangeSongPlayer extends SongPlayer{

	private int distance = 16;
	
	public RangeSongPlayer(Playable playable, SoundCategory soundCategory) {
		super(playable, soundCategory);
	}

	public RangeSongPlayer(Playable playable) {
		super(playable);
	}	

	protected RangeSongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
		super(songPlayer);
	}

	public RangeSongPlayer(Playlist playlist, SoundCategory soundCategory) {
		super(playlist, soundCategory);
	}

	public RangeSongPlayer(Playlist playlist) {
		super(playlist);
	}

	@Override
	void update(String key, Object value) {
		super.update(key, value);
		
		switch (key){
			case "distance":
				distance = (int) value;
				break;
		}
	}

	/**
	 * Sets distance in blocks where would be player able to hear sound. 
	 * @param distance (Default 16 blocks)
	 */
	public void setDistance(int distance) {
		this.distance = distance;
		CallUpdate("distance", distance);
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
