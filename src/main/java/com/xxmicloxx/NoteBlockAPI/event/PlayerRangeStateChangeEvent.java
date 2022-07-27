package com.xxmicloxx.NoteBlockAPI.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Called whenever a Player enters or leave the range of a stationary SongPlayer
 *
 */
public class PlayerRangeStateChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final SongPlayer song;
	private final Player player;
	private final boolean state;

	public PlayerRangeStateChangeEvent(@NotNull SongPlayer song, @NotNull Player player, boolean state) {
		this.song = song;
		this.player = player;
		this.state = state;
	}

	/**
	 * Returns SongPlayer which range Player enters or leaves
	 * @return SongPlayer
	 */
	@NotNull
	public SongPlayer getSongPlayer() {
		return song;
	}

	/**
	 * Returns Player which enter/leave SongPlayer range
	 * @return Player
	 */
	@NotNull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns true if Player is actually in SongPlayer range
	 * @return boolean determining if is Player in SongPlayer range
	 */
	public boolean isInRange() {
		return state;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
