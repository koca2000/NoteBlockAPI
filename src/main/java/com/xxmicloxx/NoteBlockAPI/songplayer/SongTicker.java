package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.utils.NamedThreadFactory;
import org.bukkit.Bukkit;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SongTicker {
    private static final ScheduledExecutorService ticker = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("NoteBlockAPI"));
    private static final Set<SongPlayer> activeSongPlayers = ConcurrentHashMap.newKeySet();

    static {
        ticker.scheduleAtFixedRate(SongTicker::tick, 0, 10, TimeUnit.MILLISECONDS);
    }

    private static void tick() {
        for (SongPlayer player : activeSongPlayers) {
            try {
                player.updateTick();
            } catch (Exception e) {
                Bukkit.getLogger().severe("Error updating song player: " + player);
                e.printStackTrace();
            }
        }
    }

    public static void register(SongPlayer songPlayer) {
        activeSongPlayers.add(songPlayer);
    }

    public static void unregister(SongPlayer songPlayer) {
        activeSongPlayers.remove(songPlayer);
    }

    public static void shutdown() {
        ticker.shutdown();
    }
}
