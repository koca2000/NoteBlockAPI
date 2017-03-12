package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteBlockPlayerMain extends JavaPlugin {

    public static NoteBlockPlayerMain plugin;
    public HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
    public HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

    public boolean disabling = false;
    
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
    
    public boolean isPre1_9(){
    	if (Bukkit.getVersion().contains("1.8")) {
    		   return true;
    	}
    	
    	if (Bukkit.getVersion().contains("1.7")) {
 		   return true;
    	}
    	
		return false;
    }
}
