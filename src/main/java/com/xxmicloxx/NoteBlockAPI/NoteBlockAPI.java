package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.MathUtils;
import com.xxmicloxx.NoteBlockAPI.utils.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * Main class; contains methods for playing and adjusting songs for players
 */
public class NoteBlockAPI extends JavaPlugin {

	private static NoteBlockAPI plugin;
	
	private Map<UUID, ArrayList<SongPlayer>> playingSongs = new ConcurrentHashMap<UUID, ArrayList<SongPlayer>>();
	private Map<UUID, Byte> playerVolume = new ConcurrentHashMap<UUID, Byte>();

	private boolean disabling = false;
	
	private HashMap<Plugin, Boolean> dependentPlugins = new HashMap<>();

	/**
	 * Returns true if a Player is currently receiving a song
	 * @param player
	 * @return is receiving a song
	 */
	public static boolean isReceivingSong(Player player) {
		return isReceivingSong(player.getUniqueId());
	}

	/**
	 * Returns true if a Player with specified UUID is currently receiving a song
	 * @param uuid
	 * @return is receiving a song
	 */
	public static boolean isReceivingSong(UUID uuid) {
		ArrayList<SongPlayer> songs = plugin.playingSongs.get(uuid);
		return (songs != null && !songs.isEmpty());
	}

	/**
	 * Stops the song for a Player
	 * @param player
	 */
	public static void stopPlaying(Player player) {
		stopPlaying(player.getUniqueId());
	}

	/**
	 * Stops the song for a Player
	 * @param uuid
	 */
	public static void stopPlaying(UUID uuid) {
		ArrayList<SongPlayer> songs = plugin.playingSongs.get(uuid);
		if (songs == null) {
			return;
		}
		for (SongPlayer songPlayer : songs) {
			songPlayer.removePlayer(uuid);
		}
	}

	/**
	 * Sets the volume for a given Player
	 * @param player
	 * @param volume
	 */
	public static void setPlayerVolume(Player player, byte volume) {
		setPlayerVolume(player.getUniqueId(), volume);
	}

	/**
	 * Sets the volume for a given Player
	 * @param uuid
	 * @param volume
	 */
	public static void setPlayerVolume(UUID uuid, byte volume) {
		plugin.playerVolume.put(uuid, volume);
	}

	/**
	 * Gets the volume for a given Player
	 * @param player
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(Player player) {
		return getPlayerVolume(player.getUniqueId());
	}

	/**
	 * Gets the volume for a given Player
	 * @param uuid
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(UUID uuid) {
		Byte byteObj = plugin.playerVolume.get(uuid);
		if (byteObj == null) {
			byteObj = 100;
			plugin.playerVolume.put(uuid, byteObj);
		}
		return byteObj;
	}
	
	public static ArrayList<SongPlayer> getSongPlayersByPlayer(Player player){
		return getSongPlayersByPlayer(player.getUniqueId());
	}
	
	public static ArrayList<SongPlayer> getSongPlayersByPlayer(UUID player){
		return plugin.playingSongs.get(player);
	}
	
	public static void setSongPlayersByPlayer(Player player, ArrayList<SongPlayer> songs){
		setSongPlayersByPlayer(player.getUniqueId(), songs);
	}
	
	public static void setSongPlayersByPlayer(UUID player, ArrayList<SongPlayer> songs){
		plugin.playingSongs.put(player, songs);
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
		
		new NoteBlockPlayerMain().onEnable();
		
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				Plugin[] plugins = getServer().getPluginManager().getPlugins();
		        Type[] types = new Type[]{PlayerRangeStateChangeEvent.class, SongDestroyingEvent.class, SongEndEvent.class, SongStoppedEvent.class };
		        for (Plugin plugin : plugins) {
		            ArrayList<RegisteredListener> rls = HandlerList.getRegisteredListeners(plugin);
		            for (RegisteredListener rl : rls) {
		                Method[] methods = rl.getListener().getClass().getDeclaredMethods();
		                for (Method m : methods) {
		                    Type[] params = m.getParameterTypes();
		                    param:
		                    for (Type paramType : params) {
		                    	for (Type type : types){
		                    		if (paramType.equals(type)) {
		                    			dependentPlugins.put(plugin, true);
		                    			break param;
		                    		}
		                    	}
		                    }
		                }

		            }
		        }
		        
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
			}
		}, 1);
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			
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
		}, 20*10, 20 * 60 * 60 * 24);
		
		new MathUtils();
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

    public Future<?> doSyncBlock(Callable callable) {
        return getServer().getScheduler().callSyncMethod(this, callable);
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
			dependentPlugins.put(plugins.get(0), true);
		}
	}
	
}
