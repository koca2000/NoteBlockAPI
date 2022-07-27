package com.xxmicloxx.NoteBlockAPI.event;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SongLoopEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer song;
	private boolean cancelled = false;

	public SongLoopEvent(@NotNull SongPlayer song) {
		this.song = song;
	}

	/**
	 * Returns SongPlayer which {@link cz.koca2000.nbs4j.Song} ends and is going to start again
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
