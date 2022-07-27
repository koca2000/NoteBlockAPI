package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * SongPlayer created at a specified Location
 *
 */
public class PositionSongPlayer extends RangeSongPlayer {

	private Location targetLocation;

	@Deprecated
	public PositionSongPlayer(@NotNull Song song) {
		super(song);
	}

	@Deprecated
	public PositionSongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	@Deprecated
	public PositionSongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory) {
		super(playlist, soundCategory);
	}

	@Deprecated
	public PositionSongPlayer(@NotNull Playlist playlist) {
		super(playlist);
	}

	public PositionSongPlayer(@NotNull cz.koca2000.nbs4j.Song song, @NotNull Location location) {
		super(song);
		setTargetLocation(location);
	}

	public PositionSongPlayer(@NotNull Playlist playlist, @NotNull Location location) {
		super(playlist);
		setTargetLocation(location);
	}

	/**
	 * Gets location on which is the PositionSongPlayer playing
	 * @return {@link Location}
	 */
	@Nullable
	public Location getTargetLocation() {
		return targetLocation;
	}

	/**
	 * Sets location on which is the PositionSongPlayer playing
	 */
	public void setTargetLocation(@NotNull Location targetLocation) {
		if (targetLocation.getWorld() == null)
			throw new IllegalArgumentException("Location does not have the world set.");

		this.targetLocation = targetLocation;
	}

	@Override
	public void playTick(@NotNull Player player, int tick) {
		if (targetLocation.getWorld() != null && !player.getWorld().getName().equals(targetLocation.getWorld().getName())) {
			return; // not in same world or invalid location
		}

		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		for (int i = 0; i < playingSong.getLayersCount(); i++) {
			cz.koca2000.nbs4j.Layer layer = playingSong.getLayer(i);
			cz.koca2000.nbs4j.Note note = layer.getNote(tick);
			if (note == null) continue;

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVolume()) / 100_00_00_00F)
					* ((1F / 16F) * getDistance());

			channelMode.play(player, targetLocation, playingSong, layer, note, soundCategory, volume, !enable10Octave);

			if (isInRange(player)) {
				if (!playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), true);
					plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true)));
				}
			} else {
				if (playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), false);
					plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false)));
				}
			}
		}
	}
	
	/**
	 * Returns true if the Player is able to hear the current PositionSongPlayer 
	 * @param player in range
	 * @return ability to hear the current PositionSongPlayer
	 */
	@Override
	public boolean isInRange(@NotNull Player player) {
		return player.getLocation().distance(targetLocation) <= getDistance();
	}
}
