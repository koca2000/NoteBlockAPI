package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Called whenever a SongPlayer is destroyed
 * @see SongPlayer
 *
 */
public class SongDestroyingEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer song;
	private boolean cancelled = false;

	public SongDestroyingEvent(@NotNull SongPlayer song) {
		this.song = song;
	}

	/**
	 * Returns SongPlayer which is being destroyed
	 * @return SongPlayer
	 */
	@NotNull
	public SongPlayer getSongPlayer() {
		return song;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
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
