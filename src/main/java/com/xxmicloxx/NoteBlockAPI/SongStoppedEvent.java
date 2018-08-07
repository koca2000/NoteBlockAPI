package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.event.SongStoppedEvent}
 */
@Deprecated
public class SongStoppedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private SongPlayer songPlayer;

	public SongStoppedEvent(SongPlayer songPlayer) {
		this.songPlayer = songPlayer;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
