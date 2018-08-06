package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

/**
 * Called whenever a SongPlayer is destroyed
 * @see SongPlayer
 *
 */
public class SongDestroyingEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private SongPlayer song;
	private boolean cancelled = false;

	public SongDestroyingEvent(SongPlayer song) {
		this.song = song;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * Returns SongPlayer which is being destroyed
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
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

	

}
