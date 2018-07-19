package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PositionSongPlayer extends SongPlayer {

	private Location targetLocation;
	private int distance = 16;

	public PositionSongPlayer(Song song) {
		super(song);
	}

	public PositionSongPlayer(Song song, SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	@Override
	public void playTick(Player player, int tick) {
		if (!player.getWorld().getName().equals(targetLocation.getWorld().getName())) {
			// not in same world
			return;
		}
		byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(player);

		for (Layer layer : song.getLayerHashMap().values()) {
			Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}

			float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume) / 1000000F) 
					* ((1F / 16F) * distance);
			float pitch = NotePitch.getPitch(note.getKey() - 33);

			if (Instrument.isCustomInstrument(note.getInstrument())) {
				CustomInstrument instrument = song.getCustomInstruments()
						[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()];

				if (instrument.getSound() != null) {
					CompatibilityUtils.playSound(player, targetLocation, instrument.getSound(),
							this.soundCategory, volume, pitch);
				} else {
					CompatibilityUtils.playSound(player, targetLocation, instrument.getSoundFileName(),
							this.soundCategory, volume, pitch);
				}
			} else {
				CompatibilityUtils.playSound(player, targetLocation,
						Instrument.getInstrument(note.getInstrument()), this.soundCategory, 
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
	}

	public int getDistance() {
		return distance;
	}

	public boolean isPlayerInRange(Player player) {
		return player.getLocation().distance(targetLocation) <= distance;
	}

}
