package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.NotePitch;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

/**
 * SongPlayer created at a specified NoteBlock
 *
 */
public class NoteBlockSongPlayer extends RangeSongPlayer {

	private Block noteBlock;

	public NoteBlockSongPlayer(Song song) {
		super(song);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
	}

	public NoteBlockSongPlayer(Song song, SoundCategory soundCategory) {
		super(song, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
	}

	public NoteBlockSongPlayer(Playlist playlist, SoundCategory soundCategory) {
		super(playlist, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
	}

	public NoteBlockSongPlayer(Playlist playlist) {
		super(playlist);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.NoteBlockSongPlayer.class);
	}

	private NoteBlockSongPlayer(SongPlayer songPlayer) {
		super(songPlayer);
	}
	
	@Override
	void update(String key, Object value) {
		super.update(key, value);
		
		switch (key){
			case "noteBlock":
				noteBlock = (Block) value;
				break;
		}
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
		CallUpdate("noteBlock", noteBlock);
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
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);
		Location loc = noteBlock.getLocation();
		loc = new Location(loc.getWorld(), loc.getX() + 0.5f, loc.getY() - 0.5f, loc.getZ() + 0.5f);
		
		for (Layer layer : song.getLayerHashMap().values()) {
			Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}
			player.playNote(loc, InstrumentUtils.getBukkitInstrument(note.getInstrument()),
					new org.bukkit.Note(note.getKey() - 33));

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00_00F)
					* ((1F / 16F) * getDistance());
			float pitch = NoteUtils.getPitch(note);

            channelMode.play(player, loc, song, layer, note, soundCategory, volume, pitch);

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
	 * Returns true if the Player is able to hear the current NoteBlockSongPlayer 
	 * @param player in range
	 * @return ability to hear the current NoteBlockSongPlayer
	 */	
	@Override
	public boolean isInRange(Player player) {
		Location loc = noteBlock.getLocation();
		loc = new Location(loc.getWorld(), loc.getX() + 0.5f, loc.getY() - 0.5f, loc.getZ() + 0.5f);
		if (player.getLocation().distance(loc) > getDistance()) {
			return false;
		} else {
			return true;
		}
	}
}