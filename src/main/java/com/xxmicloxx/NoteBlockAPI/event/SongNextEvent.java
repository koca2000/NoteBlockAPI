package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.NotNull;

public class SongNextEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer song;

	public SongNextEvent(@NotNull SongPlayer song) {
		this.song = song;
	}

	/**
	 * Returns SongPlayer which is going to play next song in playlist
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
