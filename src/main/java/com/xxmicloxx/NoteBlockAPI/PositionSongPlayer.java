package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer}
 */
@Deprecated
public class PositionSongPlayer extends SongPlayer {

	private Location targetLocation;
	private int distance = 16;

	public PositionSongPlayer(Song song) {
		super(song);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer.class);
	}

	public PositionSongPlayer(Song song, SoundCategory soundCategory) {
		super(song, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer.class);
	}
	
	public PositionSongPlayer(com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer songPlayer) {
		super(songPlayer);
	}

	@Override
	void update(String key, Object value) {
		super.update(key, value);
		
		switch (key){
			case "distance":
				distance = (int) value;
				break;
			case "targetLocation":
				targetLocation = (Location) value;
				break;
		}
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
		CallUpdate("targetLocation", targetLocation);
	}

	@Override
	public void playTick(Player player, int tick) {
		if (!player.getWorld().getName().equals(targetLocation.getWorld().getName())) {
			return; // not in same world
		}

		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		for (Layer layer : song.getLayerHashMap().values()) {
			Note note = layer.getNote(tick);
			if (note == null) continue;

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume) / 1000000F) 
					* ((1F / 16F) * getDistance());
			float pitch = NotePitch.getPitch(note.getKey() - 33);

			if (InstrumentUtils.isCustomInstrument(note.getInstrument())) {
				CustomInstrument instrument = song.getCustomInstruments()
						[note.getInstrument() - InstrumentUtils.getCustomInstrumentFirstIndex()];

				if (instrument.getSound() != null) {
					CompatibilityUtils.playSound(player, targetLocation, instrument.getSound(),
							this.soundCategory, volume, pitch);
				} else {
					CompatibilityUtils.playSound(player, targetLocation, instrument.getSoundfile(),
							this.soundCategory, volume, pitch);
				}
			} else {
				CompatibilityUtils.playSound(player, targetLocation,
						InstrumentUtils.getInstrument(note.getInstrument()), this.soundCategory, 
						volume, pitch);
			}

			if (isPlayerInRange(player)) {
				if (!this.playerList.get(player.getName())) {
					playerList.put(player.getName(), true);
					Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true));
				}
			} else {
				if (this.playerList.get(player.getName())) {
					playerList.put(player.getName(), false);
					Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false));
				}
			}
		}
	}

	/**
	 * Sets distance in blocks where would be player able to hear sound. 
	 * @param distance (Default 16 blocks)
	 */
	public void setDistance(int distance) {
		this.distance = distance;
		CallUpdate("distance", distance);
	}

	public int getDistance() {
		return distance;
	}
	
	/**
	 * Returns true if the Player is able to hear the current PositionSongPlayer 
	 * @param player in range
	 * @return ability to hear the current PositionSongPlayer
	 */
	@Deprecated
	public boolean isPlayerInRange(Player player) {
		return player.getLocation().distance(targetLocation) <= getDistance();
	}
	
}
