package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.NotNull;


/**
 * Called whenever a SongPlayer is stopped
 * @see SongPlayer
 */
public class SongStoppedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer songPlayer;

	public SongStoppedEvent(@NotNull SongPlayer songPlayer) {
		this.songPlayer = songPlayer;
	}

	/**
	 * Returns SongPlayer which is now stopped
	 * @return SongPlayer
	 */
	@NotNull
	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}

