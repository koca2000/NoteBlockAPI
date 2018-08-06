package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongLoopEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private SongPlayer song;
	private boolean cancelled = false;

	public SongLoopEvent(SongPlayer song) {
		this.song = song;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Returns SongPlayer which {@link Song} ends and is going to start again
	 * @return SongPlayer
	 */
	public SongPlayer getSongPlayer() {
		return song;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
