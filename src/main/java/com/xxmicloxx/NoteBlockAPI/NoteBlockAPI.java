package com.xxmicloxx.NoteBlockAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.Updater;

/**
 * Main class; contains methods for playing and adjusting songs for players
 */
public class NoteBlockAPI extends JavaPlugin {

	private static NoteBlockAPI plugin;
	
	private Map<UUID, ArrayList<SongPlayer>> playingSongs = 
			Collections.synchronizedMap(new HashMap<UUID, ArrayList<SongPlayer>>());
	private Map<UUID, Byte> playerVolume = Collections.synchronizedMap(new HashMap<UUID, Byte>());

	private boolean disabling = false;
	
	private HashMap<Plugin, Boolean> dependentPlugins = new HashMap<>();

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
		
		for (Plugin pl : getServer().getPluginManager().getPlugins()){
			if (pl.getDescription().getDepend().contains("NoteBlockAPI") || pl.getDescription().getSoftDepend().contains("NoteBlockAPI")){
				dependentPlugins.put(pl, false);
			}
		}
		
		Metrics metrics = new Metrics(this);
		
		metrics.addCustomChart(new Metrics.DrilldownPie("deprecated", () -> {
	        Map<String, Map<String, Integer>> map = new HashMap<>();
	        for (Plugin pl : dependentPlugins.keySet()){
	        	String deprecated = dependentPlugins.get(pl) ? "yes" : "no";
	        	Map<String, Integer> entry = new HashMap<>();
		        entry.put(pl.getDescription().getFullName(), 1);
		        map.put(deprecated, entry);
	        }
	        return map;
	    }));
		
		new NoteBlockPlayerMain().onEnable();
		
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				Plugin[] plugins = getServer().getPluginManager().getPlugins();
		        for(Plugin plugin: plugins) {
		           
		            ArrayList<RegisteredListener> rls = new ArrayList<>();
		            rls.addAll(PlayerRangeStateChangeEvent.getHandlerList().getRegisteredListeners(plugin));
		            rls.addAll(SongDestroyingEvent.getHandlerList().getRegisteredListeners(plugin));
		            rls.addAll(SongEndEvent.getHandlerList().getRegisteredListeners(plugin));
		            rls.addAll(SongStoppedEvent.getHandlerList().getRegisteredListeners(plugin));
		            if (!rls.isEmpty()){
		            	dependentPlugins.put(plugin, true);
		            }
		        }
			}
		}, 20*60);
		
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					if (Updater.checkUpdate("19287", getDescription().getVersion())){
						Bukkit.getLogger().info(String.format("[%s] New update available!", plugin.getDescription().getName()));
					}
				} catch (IOException e) {
					Bukkit.getLogger().info(String.format("[%s] Cannot receive update from Spigot resource page!", plugin.getDescription().getName()));
				}
			}
		}, 20*10);
		
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
	
	public static NoteBlockAPI getAPI(){
		return plugin;
	}
	
	protected void handleDeprecated(StackTraceElement[] ste){
		int pom = 1;
		String clazz = ste[pom].getClassName();
		while (clazz.startsWith("com.xxmicloxx.NoteBlockAPI")){
			pom++;
			clazz = ste[pom].getClassName();
		}
		String[] packageParts = clazz.split("\\.");
		ArrayList<Plugin> plugins = new ArrayList<Plugin>();
		plugins.addAll(dependentPlugins.keySet());
		
		ArrayList<Plugin> notResult = new ArrayList<Plugin>();
		parts:
		for (int i = 0; i < packageParts.length - 1; i++){
			
			for (Plugin pl : plugins){
				if (notResult.contains(pl)){ continue;}
				if (plugins.size() - notResult.size() == 1){
					break parts;
				}
				String[] plParts = pl.getDescription().getMain().split("\\.");
				if (!packageParts[i].equalsIgnoreCase(plParts[i])){
					notResult.add(pl);
					continue;
				}
			}
			plugins.removeAll(notResult);
			notResult.clear();
		}
		
		plugins.removeAll(notResult);
		notResult.clear();
		if (plugins.size() == 1){
			Bukkit.getLogger().info(plugins.get(0).getName());
			dependentPlugins.put(plugins.get(0), true);
		}
	}
	
}
