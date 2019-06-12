package com.xxmicloxx.NoteBlockAPI.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Playlist {

	ArrayList<Song> songs = new ArrayList<>();
	
	public Playlist(Song ...songs){
		if (songs.length == 0){
			throw new IllegalArgumentException("Cannot create empty playlist");
		}
		checkNull(songs);
		this.songs.addAll(Arrays.asList(songs));
	}
	
	/**
	 * Add array of {@link Song} to playlist
	 * @param songs
	 */
	public void add(Song ...songs){
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
	public void insert(int index, Song ...songs){
		if (songs.length == 0){
			return;
		}
		if (index > this.songs.size()){
			throw new IllegalArgumentException("Index is higher than playlist size");
		}
		checkNull(songs);
		this.songs.addAll(index, Arrays.asList(songs));
	}
	
	private void checkNull(Song ...songs){
		List<Song> songList = Arrays.asList(songs);
		if (songList.contains(null)){
			throw new IllegalArgumentException("Cannot add null to playlist");
		}
	}
	
	/**
	 * Removes songs from playlist
	 * @param songs
	 * @throws IllegalArgumentException when you try to remove all {@link Song} from {@link Playlist}
	 */
	public void remove(Song ...songs){
		ArrayList<Song> songsTemp = new ArrayList<>();
		songsTemp.addAll(this.songs);
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
	public Song get(int songNumber){
		return songs.get(songNumber);
	}
	
	/**
	 * Get number of {@link Song} in playlist
	 * @return
	 */
	public int getCount(){
		return songs.size();
	}
	
	/**
	 * Check whether {@link Song} is not last in playlist
	 * @param songNumber
	 * @return true if there is another {@link Song} after specified index
	 */
	public boolean hasNext(int songNumber){
		return songs.size() > (songNumber + 1);
	}
	
	/**
	 * Check whether {@link Song} with specified index exists in playlist
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
	public int getIndex(Song song){	return songs.indexOf(song);	}

	/**
	 * Check whether playlist contains song.
	 * @param song
	 * @return
	 */
	public boolean contains(Song song) { return songs.contains(song); }

	/**
	 * Returns list of Songs in Playlist
	 * @return
	 */	
	public ArrayList<Song> getSongList(){
		return (ArrayList<Song>) songs.clone();
	}
}
