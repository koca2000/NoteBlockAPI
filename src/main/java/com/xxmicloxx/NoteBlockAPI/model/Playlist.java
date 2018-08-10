package com.xxmicloxx.NoteBlockAPI.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist {

	ArrayList<Song> songs = new ArrayList<>();
	
	public Playlist(Song ...songs){
		this.songs.addAll(Arrays.asList(songs));
	}
	
	public void add(Song ...songs){
		this.songs.addAll(Arrays.asList(songs));
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
	
	public Song get(int songNumber){
		return songs.get(songNumber);
	}
	
	public int getCount(){
		return songs.size();
	}
	
	public boolean hasNext(int songNumber){
		return songs.size() > (songNumber + 1);
	}
	
	public boolean exits(int songNumber){
		return songs.size() > songNumber;
	}
}
