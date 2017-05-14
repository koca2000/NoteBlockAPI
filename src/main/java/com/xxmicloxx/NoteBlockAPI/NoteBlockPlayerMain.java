package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoteBlockPlayerMain extends JavaPlugin {

    public static NoteBlockPlayerMain plugin;
    
    public Map<String, ArrayList<SongPlayer>> playingSongs = Collections.synchronizedMap(new HashMap<String, ArrayList<SongPlayer>>());
    public Map<String, Byte> playerVolume = Collections.synchronizedMap(new HashMap<String, Byte>());

    private boolean disabling = false;
    
    public static boolean isReceivingSong(Player p) {
        return ((plugin.playingSongs.get(p.getName()) != null) && (!plugin.playingSongs.get(p.getName()).isEmpty()));
    }

    public static void stopPlaying(Player p) {
        if (plugin.playingSongs.get(p.getName()) == null) {
            return;
        }
        for (SongPlayer s : plugin.playingSongs.get(p.getName())) {
            s.removePlayer(p);
        }
    }

    public static void setPlayerVolume(Player p, byte volume) {
        plugin.playerVolume.put(p.getName(), volume);
    }

    public static byte getPlayerVolume(Player p) {
        Byte b = plugin.playerVolume.get(p.getName());
        if (b == null) {
            b = 100;
            plugin.playerVolume.put(p.getName(), b);
        }
        return b;
    }

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {    	
    	disabling = true;
        Bukkit.getScheduler().cancelTasks(this);
    }
    
    protected static int getCompatibility(){
    	if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7")){
    		return NoteBlockCompatibility.pre1_9;
    	} else if (Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11")){
    		return NoteBlockCompatibility.pre1_12;
    	} else {
    		return NoteBlockCompatibility.post1_12;
    	}
    }
    
    public void doSync(Runnable r) {
        getServer().getScheduler().runTask(this, r);
    }

    public void doAsync(Runnable r) {
        getServer().getScheduler().runTaskAsynchronously(this, r);
    }
    
    protected boolean isDisabling(){
    	return disabling;
    }
    
    public class NoteBlockCompatibility{
    	public static final int pre1_9 = 0;
    	public static final int pre1_12 = 1;
    	public static final int post1_12 = 2;
    }
}
