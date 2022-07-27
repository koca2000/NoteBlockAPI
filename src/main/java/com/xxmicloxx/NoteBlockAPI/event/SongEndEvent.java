package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a Song ends
 * or when no players are listening and auto destroy is enabled
 * @see SongPlayer
 *
 */
public class SongEndEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer song;

	public SongEndEvent(@NotNull SongPlayer song) {
		this.song = song;
	}

	/**
	 * Returns SongPlayer which {@link cz.koca2000.nbs4j.Song} ends
	 * @return SongPlayer
	 */
	@NotNull
	public SongPlayer getSongPlayer() {
		return song;
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