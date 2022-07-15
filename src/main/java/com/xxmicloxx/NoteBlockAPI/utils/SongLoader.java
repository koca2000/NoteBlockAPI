package com.xxmicloxx.NoteBlockAPI.utils;

import cz.koca2000.nbs4j.Song;
import cz.koca2000.nbs4j.SongCorruptedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility for loading songs of various formats (midi and nbs files).
 */
public class SongLoader {

    private SongLoader(){}

    /**
     * Loads the song from the given file.
     * Following formats in specified order are tried: midi, nbs
     * @param file file to be loaded
     * @return loaded {@link Song} or null if song could not be opened or loaded.
     */
    public static Song loadSong(File file){
        if (file == null)
            throw new IllegalArgumentException("Given file can not be null");
        return loadSong(file.toPath());
    }

    /**
     * Loads the song from the given file.
     * Following formats in specified order are tried: midi, nbs
     * @param path file to be loaded
     * @return loaded {@link Song} or null if song could not be opened or loaded.
     */
    public static Song loadSong(Path path){
        if (path == null)
            throw new IllegalArgumentException("Given path can not be null");
        try {
            return loadSongInternal(Files.newInputStream(path));
        }
        catch (SongCorruptedException e){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Song is corrupted: " + path.getFileName());
            e.printStackTrace();
        }
        catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Failed to load song: " + path.getFileName());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads the song from the given stream.
     * Following formats in specified order are tried: midi, nbs
     * @param stream stream to be read
     * @return loaded {@link Song} or null if song could not be loaded.
     */
    public static Song loadSong(InputStream stream){
        if (stream == null)
            throw new IllegalArgumentException("Given stream can not be null");
        try{
            return loadSongInternal(stream);
        }
        catch (SongCorruptedException e){
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Song from stream is corrupted.");
            e.printStackTrace();
        }
        return null;
    }

    private static Song loadSongInternal(InputStream stream){
        //TODO: try to load midi file

        return Song.fromStream(stream);
    }
}
