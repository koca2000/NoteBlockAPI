package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class NoteBlockAPI extends JavaPlugin {

	public static NoteBlockAPI plugin;
	
	private Map<UUID, ArrayList<SongPlayer>> playingSongs = 
			Collections.synchronizedMap(new HashMap<UUID, ArrayList<SongPlayer>>());
	private Map<UUID, Byte> playerVolume = Collections.synchronizedMap(new HashMap<UUID, Byte>());

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
		plugin.playerVolume.put(player.getUniqueId(), volume);
	}

	/**
	 * Gets the volume for a given Player
	 * @param player
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(Player player) {
		Byte byteObj = plugin.playerVolume.get(player.getUniqueId());
		if (byteObj == null) {
			byteObj = 100;
			plugin.playerVolume.put(player.getUniqueId(), byteObj);
		}
		return byteObj;
	}
	
	public static ArrayList<SongPlayer> getSongPlayersByPlayer(Player player){
		return plugin.playingSongs.get(player.getUniqueId());
	}
	
	public static void setSongPlayersByPlayer(Player player, ArrayList<SongPlayer> songs){
		plugin.playingSongs.put(player.getUniqueId(), songs);
	}

	@Override
	public void onEnable() {
		plugin = this;
		new Metrics(this);
		new NoteBlockPlayerMain().onEnable();
	}

	@Override
	public void onDisable() {    	
		disabling = true;
		Bukkit.getScheduler().cancelTasks(this);
		NoteBlockPlayerMain.plugin.onDisable();
	}

	public void doSync(Runnable runnable) {
		getServer().getScheduler().runTask(this, runnable);
	}

	public void doAsync(Runnable runnable) {
		getServer().getScheduler().runTaskAsynchronously(this, runnable);
	}

	public boolean isDisabling() {
		return disabling;
	}
	
}
