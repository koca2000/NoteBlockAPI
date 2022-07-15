package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import cz.koca2000.nbs4j.SongCorruptedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utils for reading Note Block Studio data
 * @deprecated Use {@link SongLoader}.
 */
@Deprecated
public class NBSDecoder {

	/**
	 * Parses a Song from a Note Block Studio project file (.nbs)
	 * @deprecated Use {@link SongLoader#loadSong(File)}
	 * @param songFile .nbs file
	 * @return Song representing a Note Block Studio project
	 */
	@Deprecated
	public static Song parse(File songFile) {
		return new Song(SongLoader.loadSong(songFile));
	}

	/**
	 * Parses a Song from an InputStream
	 * @deprecated Use {@link SongLoader#loadSong(InputStream)}
	 * @param inputStream InputStream of a Note Block Studio project file (.nbs)
	 * @return Song from the InputStream
	 */
	public static Song parse(InputStream inputStream) {
		return new Song(SongLoader.loadSong(inputStream));
	}
}
