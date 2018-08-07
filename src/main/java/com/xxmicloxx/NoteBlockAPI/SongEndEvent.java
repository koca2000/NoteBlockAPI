package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.SongEndEvent}
 */
@Deprecated
public class SongEndEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private SongPlayer song;

	public SongEndEvent(SongPlayer song) {
		this.song = song;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public SongPlayer getSongPlayer() {
		return song;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
}
