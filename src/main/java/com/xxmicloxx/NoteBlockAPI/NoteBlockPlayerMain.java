package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoteBlockPlayerMain extends JavaPlugin {

	public static NoteBlockPlayerMain plugin;

	public Map<String, ArrayList<SongPlayer>> playingSongs = 
			Collections.synchronizedMap(new HashMap<String, ArrayList<SongPlayer>>());
	public Map<String, Byte> playerVolume = Collections.synchronizedMap(new HashMap<String, Byte>());

	private boolean disabling = false;

	public static boolean isReceivingSong(Player p) {
		return ((plugin.playingSongs.get(p.getName()) != null) 
				&& (!plugin.playingSongs.get(p.getName()).isEmpty()));
	}

	public static void stopPlaying(Player player) {
		if (plugin.playingSongs.get(player.getName()) == null) {
			return;
		}
		for (SongPlayer songPlayer : plugin.playingSongs.get(player.getName())) {
			songPlayer.removePlayer(player);
		}
	}

	public static void setPlayerVolume(Player player, byte volume) {
		plugin.playerVolume.put(player.getName(), volume);
	}

	public static byte getPlayerVolume(Player player) {
		Byte byteObj = plugin.playerVolume.get(player.getName());
		if (byteObj == null) {
			byteObj = 100;
			plugin.playerVolume.put(player.getName(), byteObj);
		}
		return byteObj;
	}

	@Override
	public void onEnable() {
		plugin = this;
		new Metrics(this);
	}

	@Override
	public void onDisable() {    	
		disabling = true;
		Bukkit.getScheduler().cancelTasks(this);
	}

	public void doSync(Runnable runnable) {
		getServer().getScheduler().runTask(this, runnable);
	}

	public void doAsync(Runnable runnable) {
		getServer().getScheduler().runTaskAsynchronously(this, runnable);
	}

	protected boolean isDisabling() {
		return disabling;
	}

}
