package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * SongPlayer created at a specified NoteBlock
 *
 */
public class NoteBlockSongPlayer extends SongPlayer {

	private Block noteBlock;
	private int distance = 16;

	public NoteBlockSongPlayer(Song song) {
		super(song);
	}

	public NoteBlockSongPlayer(Song song, SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	/**
	 * Get the Block this SongPlayer is played at
	 * @return Block representing a NoteBlock
	 */
	public Block getNoteBlock() {
		return noteBlock;
	}

	/**
	 * Set the Block this SongPlayer is played at
	 */
	public void setNoteBlock(Block noteBlock) {
		this.noteBlock = noteBlock;
	}

	@Override
	public void playTick(Player player, int tick) {
		if (noteBlock.getType() != Material.NOTE_BLOCK) {
			return;
		}
		if (!player.getWorld().getName().equals(noteBlock.getWorld().getName())) {
			// not in same world
			return;
		}
		byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(player);

		for (Layer layer : song.getLayerHashMap().values()) {
			Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}
			player.playNote(noteBlock.getLocation(), InstrumentUtils.getBukkitInstrument(note.getInstrument()),
					new org.bukkit.Note(note.getKey() - 33));

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume) / 1000000F) 
					* ((1F / 16F) * distance);
			float pitch = NotePitch.getPitch(note.getKey() - 33);

			if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
				CustomInstrument instrument = song.getCustomInstruments()
						[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

				if (instrument.getSound() != null) {
					CompatibilityUtils.playSound(player, noteBlock.getLocation(), 
							instrument.getSound(), this.soundCategory, volume, pitch);
				} else {
					CompatibilityUtils.playSound(player, noteBlock.getLocation(), 
							instrument.getSoundFileName(), this.soundCategory, volume, pitch);
				}
			} else {
				CompatibilityUtils.playSound(player, noteBlock.getLocation(),
						InstrumentUtils.getInstrument(note.getInstrument()), this.soundCategory, volume, pitch);
			}

			if (isInRange(player)) {
				if (!this.playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), true);
					Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true));
				}
			} else {
				if (this.playerList.get(player.getUniqueId())) {
					playerList.put(player.getUniqueId(), false);
					Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false));
				}
			}
		}
	}

	/**
	 * Sets distance in blocks where the player would be able to hear sound 
	 * @param distance (default 16 blocks)
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Gets distance in blocks where the player is able to hear sound 
	 * @return distance (default 16 blocks)
	 */
	public int getDistance() {
		return distance;
	}

	@Deprecated
	public boolean isPlayerInRange(Player player) {
		return isInRange(player);
	}

	/**
	 * Returns true if the Player is able to hear the current NoteBlockSongPlayer 
	 * @param player in range
	 * @return ability to hear the current NoteBlockSongPlayer
	 */
	public boolean isInRange(Player player) {
		if (player.getLocation().distance(noteBlock.getLocation()) > distance) {
			return false;
		} else {
			return true;
		}
	}

}
