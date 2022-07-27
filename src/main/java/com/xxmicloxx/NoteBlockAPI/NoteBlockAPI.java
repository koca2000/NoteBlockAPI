package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main class; contains methods for playing and adjusting songs for players
 */
public class NoteBlockAPI extends JavaPlugin {

	private static NoteBlockAPI plugin;
	
	private final Map<UUID, ArrayList<SongPlayer>> playingSongs = new ConcurrentHashMap<>();
	private final Map<UUID, Byte> playerVolume = new ConcurrentHashMap<>();

	private boolean disabling = false;

	/**
	 * Returns true if a Player is currently receiving a song
	 * @param player The player you want to know the information about
	 * @return is receiving a song
	 */
	public static boolean isReceivingSong(@NotNull Player player) {
		return isReceivingSong(player.getUniqueId());
	}

	/**
	 * Returns true if a Player with specified UUID is currently receiving a song
	 * @param uuid UUID of the player
	 * @return is receiving a song
	 */
	public static boolean isReceivingSong(@NotNull UUID uuid) {
		ArrayList<SongPlayer> songs = plugin.playingSongs.get(uuid);
		return (songs != null && !songs.isEmpty());
	}

	/**
	 * Stops the song for a Player
	 * @param player The player for whose the playback will be stopped
	 */
	public static void stopPlaying(@NotNull Player player) {
		stopPlaying(player.getUniqueId());
	}

	/**
	 * Stops the song for a Player
	 * @param uuid UUID of the player
	 */
	public static void stopPlaying(@NotNull UUID uuid) {
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
	 * @param player Player whose volume will be set
	 * @param volume Volume in range 0 - 100
	 */
	public static void setPlayerVolume(@NotNull Player player, byte volume) {
		setPlayerVolume(player.getUniqueId(), volume);
	}

	/**
	 * Sets the volume for a given Player
	 * @param uuid UUID of the player
	 * @param volume Volume in range 0 - 100
	 */
	public static void setPlayerVolume(@NotNull UUID uuid, byte volume) {
		if (volume > 100) volume = 100;
		if (volume < 0) volume = 0;

		plugin.playerVolume.put(uuid, volume);
	}

	/**
	 * Gets the volume for a given Player
	 * @param player Player whose volume would be returned
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(@NotNull Player player) {
		return getPlayerVolume(player.getUniqueId());
	}

	/**
	 * Gets the volume for a given Player
	 * @param uuid UUID of the player
	 * @return volume (byte)
	 */
	public static byte getPlayerVolume(@NotNull UUID uuid) {
		return plugin.playerVolume.computeIfAbsent(uuid, k -> (byte) 100);
	}

	@Nullable
	public static ArrayList<SongPlayer> getSongPlayersByPlayer(@NotNull Player player){
		return getSongPlayersByPlayer(player.getUniqueId());
	}

	@Nullable
	public static ArrayList<SongPlayer> getSongPlayersByPlayer(UUID player){
		return plugin.playingSongs.get(player);
	}

	public static void setSongPlayersByPlayer(@NotNull Player player, @NotNull ArrayList<SongPlayer> songs){
		setSongPlayersByPlayer(player.getUniqueId(), songs);
	}

	public static void setSongPlayersByPlayer(@NotNull UUID player, @NotNull ArrayList<SongPlayer> songs){
		plugin.playingSongs.put(player, songs);
	}

	@Override
	public void onEnable() {
		plugin = this;
		
		new Metrics(this, 1083);

		getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
			try {
				String newVersion = Updater.checkUpdate("19287", getDescription().getVersion());
				if (newVersion != null){
					Bukkit.getLogger().info(String.format("[%s] New update available! Available version: %s. Your version: %s", plugin.getDescription().getName(), newVersion, getDescription().getVersion()));
				}
			} catch (IOException e) {
				Bukkit.getLogger().info(String.format("[%s] Cannot receive version information from Spigot resource page!", plugin.getDescription().getName()));
			}
		}, 20*10, 20 * 60 * 60 * 24);
	}

	@Override
	public void onDisable() {    	
		disabling = true;
		Bukkit.getScheduler().cancelTasks(this);
		List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
		for (BukkitWorker worker : workers){
			if (!worker.getOwner().equals(this))
				continue;
			worker.getThread().interrupt();
		}
	}

	@NotNull
	public BukkitTask doSync(@NotNull Runnable runnable) {
		 return getServer().getScheduler().runTask(this, runnable);
	}

	@NotNull
	public BukkitTask doAsync(@NotNull Runnable runnable) {
		return getServer().getScheduler().runTaskAsynchronously(this, runnable);
	}

	public boolean isDisabling() {
		return disabling;
	}
	
	public static NoteBlockAPI getAPI(){
		return plugin;
	}

	/**
	 * Calls Plugin Manager to enable NoteBlockAPI as a plugin.
	 * Only necessary if the plugin is shaded.
	 */
	public static void initialize(){
		if (getAPI() != null)
			return;

		Bukkit.getPluginManager().enablePlugin(new NoteBlockAPI());
	}
}
