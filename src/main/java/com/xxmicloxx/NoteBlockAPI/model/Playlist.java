package com.xxmicloxx.NoteBlockAPI.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Playlist {

	ArrayList<cz.koca2000.nbs4j.Song> songs = new ArrayList<>();


	@Deprecated
	public Playlist(@NotNull Song ...songs){
		if (songs.length == 0){
			throw new IllegalArgumentException("Cannot create empty playlist");
		}
		checkNull(songs);
		add(songs);
	}

	public Playlist(@NotNull cz.koca2000.nbs4j.Song ...songs){
		if (songs.length == 0){
			throw new IllegalArgumentException("Cannot create empty playlist");
		}
		checkNull(songs);
		add(songs);
	}

	/**
	 * Add array of {@link Song} to playlist
	 * @param songs
	 */
	@Deprecated
	public void add(@NotNull Song ...songs){
		if (songs.length == 0){
			return;
		}
		checkNull(songs);
		this.songs.addAll(SongsToNBS4j(songs));
	}

	/**
	 * Add array of {@link cz.koca2000.nbs4j.Song} to playlist
	 * @param songs
	 */
	public void add(@NotNull cz.koca2000.nbs4j.Song ...songs){
		if (songs.length == 0){
			return;
		}
		checkNull(songs);
		this.songs.addAll(Arrays.asList(songs));
	}

	/**
	 * Insert array of {@link Song} at a specified index
	 * @param index
	 * @param songs
	 */
	@Deprecated
	public void insert(int index, @NotNull Song ...songs){
		if (songs.length == 0){
			return;
		}
		if (index > this.songs.size()){
			throw new IllegalArgumentException("Index is higher than playlist size");
		}
		checkNull(songs);
		this.songs.addAll(index, SongsToNBS4j(songs));
	}

	/**
	 * Insert array of {@link cz.koca2000.nbs4j.Song} at a specified index
	 * @param index
	 * @param songs
	 */
	public void insert(int index, @NotNull cz.koca2000.nbs4j.Song...songs){
		if (songs.length == 0){
			return;
		}
		if (index > this.songs.size()){
			throw new IllegalArgumentException("Index is higher than playlist size");
		}
		checkNull(songs);
		this.songs.addAll(index, Arrays.asList(songs));
	}

	@Deprecated
	private void checkNull(@NotNull Song ...songs){
		List<Song> songList = Arrays.asList(songs);
		if (songList.contains(null)){
			throw new IllegalArgumentException("Cannot add null to playlist");
		}
	}

	private void checkNull(@NotNull cz.koca2000.nbs4j.Song ...songs){
		List<cz.koca2000.nbs4j.Song> songList = Arrays.asList(songs);
		if (songList.contains(null)){
			throw new IllegalArgumentException("Cannot add null to playlist");
		}
	}
	
	/**
	 * Removes songs from playlist
	 * @param songs
	 * @throws IllegalArgumentException when you try to remove all {@link Song} from {@link Playlist}
	 */
	@Deprecated
	public void remove(@NotNull Song ...songs){
		List<cz.koca2000.nbs4j.Song> oldSongs = SongsToNBS4j(songs);
		ArrayList<cz.koca2000.nbs4j.Song> songsTemp = new ArrayList<>(this.songs);
		songsTemp.removeAll(oldSongs);
		if (songsTemp.size() > 0){
			this.songs = songsTemp;
		} else {
			throw new IllegalArgumentException("Cannot remove all songs from playlist");
		}
	}

	/**
	 * Removes songs from playlist
	 * @param songs
	 * @throws IllegalArgumentException when you try to remove all {@link cz.koca2000.nbs4j.Song} from {@link Playlist}
	 */
	public void remove(@NotNull cz.koca2000.nbs4j.Song...songs){
		ArrayList<cz.koca2000.nbs4j.Song> songsTemp = new ArrayList<>(this.songs);
		songsTemp.removeAll(Arrays.asList(songs));
		if (songsTemp.size() > 0){
			this.songs = songsTemp;
		} else {
			throw new IllegalArgumentException("Cannot remove all songs from playlist");
		}
	}
	
	/**
	 * Get {@link Song} in playlist at specified index
	 * @param songNumber - song index
	 * @return
	 */
	@Deprecated
	@NotNull
	public Song get(int songNumber){
		return new Song(songs.get(songNumber));
	}

	/**
	 * Get {@link cz.koca2000.nbs4j.Song} in playlist at specified index
	 * @param songNumber - song index
	 * @return
	 */
	@NotNull
	public cz.koca2000.nbs4j.Song getSong(int songNumber){
		return songs.get(songNumber);
	}
	
	/**
	 * Get number of {@link cz.koca2000.nbs4j.Song} in playlist
	 * @return
	 */
	public int getCount(){
		return songs.size();
	}
	
	/**
	 * Check whether {@link cz.koca2000.nbs4j.Song} is not last in playlist
	 * @param songNumber
	 * @return true if there is another {@link cz.koca2000.nbs4j.Song} after specified index
	 */
	public boolean hasNext(int songNumber){
		return songs.size() > (songNumber + 1);
	}
	
	/**
	 * Check whether {@link cz.koca2000.nbs4j.Song} with specified index exists in playlist
	 * @param songNumber
	 * @return
	 */
	public boolean exist(int songNumber){
		return songs.size() > songNumber;
	}

	/**
	 * Returns index of song.
	 * @param song
	 * @return Index of song. -1 if song is not in playelist
	 */
	@Deprecated
	public int getIndex(@NotNull Song song){	return songs.indexOf(song.getSong());	}

	/**
	 * Returns index of song.
	 * @param song
	 * @return Index of song. -1 if song is not in playelist
	 */
	public int getIndex(@NotNull cz.koca2000.nbs4j.Song song){	return songs.indexOf(song);	}

	/**
	 * Check whether playlist contains song.
	 * @param song
	 * @return
	 */
	@Deprecated
	public boolean contains(@NotNull Song song) { return songs.contains(song.getSong()); }

	/**
	 * Check whether playlist contains song.
	 * @param song
	 * @return
	 */
	public boolean contains(@NotNull cz.koca2000.nbs4j.Song song) { return songs.contains(song); }

	/**
	 * Returns list of Songs in Playlist
	 * @return
	 */
	@Deprecated
	@NotNull
	public ArrayList<Song> getSongList(){
		return new ArrayList<>(NBS4jToSong(songs));
	}

	@NotNull
	public List<cz.koca2000.nbs4j.Song> getSongs(){
		return new ArrayList<>(songs);
	}

	@Deprecated
	@NotNull
	private List<cz.koca2000.nbs4j.Song> SongsToNBS4j(@NotNull Song[] songs){
		return Arrays.stream(songs).map(Song::getSong).collect(Collectors.toList());
	}

	@Deprecated
	@NotNull
	private List<Song> NBS4jToSong(@NotNull List<cz.koca2000.nbs4j.Song> songs){
		return songs.stream().map(Song::new).collect(Collectors.toList());
	}
}
