package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.NoteBlockAPI}
 */
@Deprecated
public class NoteBlockPlayerMain {

	public static NoteBlockPlayerMain plugin;

	public Map<String, ArrayList<SongPlayer>> playingSongs = 
			Collections.synchronizedMap(new HashMap<String, ArrayList<SongPlayer>>());
	public Map<String, Byte> playerVolume = Collections.synchronizedMap(new HashMap<String, Byte>());

	private boolean disabling = false;

	/**
	 * Returns true if a Player is currently receiving a song
	 * @param player
	 * @return is receiving a song
	 */
	public static boolean isReceivingSong(Player player) {
		return ((plugin.playingSongs.get(player.getUniqueId()) != null) 
				&& (!plugin.playingSongs.get(player.getUniqueId()).isEmpty()));
	}

	/**
	 * Stops the song for a Player
	 * @param player
	 */
	public static void stopPlaying(Player player) {
		if (plugin.playingSongs.get(player.getUniqueId()) == null) {
			return;
		}
		for (SongPlayer songPlayer : plugin.playingSongs.get(player.getUniqueId())) {
			songPlayer.removePlayer(player);
		}
	}

	/**
	 * Sets the volume for a given Player
	 * @param player
	 * @param volume
	 */
	public static void setPlayerVolume(Player player, byte volume) {
		plugin.playerVolume.put(player.getName(), volume);
		NoteBlockAPI.setPlayerVolume(player, volume);
	}

	/**
	 * Gets the volume for a given Player
	 * @param player
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(Player player) {
		Byte byteObj = plugin.playerVolume.get(player.getName());
		if (byteObj == null) {
			byteObj = 100;
			plugin.playerVolume.put(player.getName(), byteObj);
		}
		return byteObj;
	}
	
	public void onEnable() {
		plugin = this;
	}
	
	public void onDisable() {    	
		disabling = true;
	}

	public void doSync(Runnable runnable) {
		Bukkit.getServer().getScheduler().runTask(NoteBlockAPI.getAPI(), runnable);
	}

	public void doAsync(Runnable runnable) {
		Bukkit.getServer().getScheduler().runTaskAsynchronously(NoteBlockAPI.getAPI(), runnable);
	}

	public boolean isDisabling() {
		return disabling;
	}
	
}
