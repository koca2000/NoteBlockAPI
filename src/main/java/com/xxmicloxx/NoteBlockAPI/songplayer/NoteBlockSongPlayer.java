package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * SongPlayer created at a specified NoteBlock
 *
 */
public class NoteBlockSongPlayer extends RangeSongPlayer {

	private Block noteBlock;

	@Deprecated
	public NoteBlockSongPlayer(Song song) {
		super(song);
	}

	@Deprecated
	public NoteBlockSongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	@Deprecated
	public NoteBlockSongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory) {
		super(playlist, soundCategory);
	}

	@Deprecated
	public NoteBlockSongPlayer(@NotNull Playlist playlist) {
		super(playlist);
	}

	public NoteBlockSongPlayer(@NotNull cz.koca2000.nbs4j.Song song, @NotNull Block noteBlock) {
		super(song);
		this.noteBlock = noteBlock;
	}

	public NoteBlockSongPlayer(@NotNull Playlist playlist, @NotNull Block noteBlock) {
		super(playlist);
		this.noteBlock = noteBlock;
	}

	/**
	 * Get the Block this SongPlayer is played at
	 * @return Block representing a NoteBlock
	 */
	@Nullable
	public Block getNoteBlock() {
		return noteBlock;
	}

	/**
	 * Set the Block this SongPlayer is played at
	 */
	public void setNoteBlock(@NotNull Block noteBlock) {
		this.noteBlock = noteBlock;
	}

	@Override
	public void playTick(@NotNull Player player, int tick) {
		if (noteBlock.getType() != CompatibilityUtils.getNoteBlockMaterial()) {
			return;
		}
		if (!player.getWorld().getName().equals(noteBlock.getWorld().getName())) {
			// not in same world
			return;
		}
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);
		Location loc = noteBlock.getLocation();
		loc = new Location(loc.getWorld(), loc.getX() + 0.5f, loc.getY() - 0.5f, loc.getZ() + 0.5f);

		for (int i = 0; i < playingSong.getLayersCount(); i++) {
			cz.koca2000.nbs4j.Layer layer = playingSong.getLayer(i);
			cz.koca2000.nbs4j.Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVolume()) / 100_00_00_00F)
					* ((1F / 16F) * getDistance());

            channelMode.play(player, loc, playingSong, layer, note, soundCategory, volume, !enable10Octave);

			if (isInRange(player)) {
				if (!this.playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), true);
					plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true)));
				}
			} else {
				if (this.playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), false);
					plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false)));
				}
			}
		}
	}
	
	/**
	 * Returns true if the Player is able to hear the current NoteBlockSongPlayer 
	 * @param player in range
	 * @return ability to hear the current NoteBlockSongPlayer
	 */	
	@Override
	public boolean isInRange(@NotNull Player player) {
		Location loc = noteBlock.getLocation();
		loc = new Location(loc.getWorld(), loc.getX() + 0.5f, loc.getY() - 0.5f, loc.getZ() + 0.5f);
		return player.getLocation().distance(loc) <= getDistance();
	}
}