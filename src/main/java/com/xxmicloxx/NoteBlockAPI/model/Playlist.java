package com.xxmicloxx.NoteBlockAPI.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Playlist {

	ArrayList<Playable> playables = new ArrayList<>();
	
	public Playlist(Playable... playables){
		if (playables.length == 0){
			throw new IllegalArgumentException("Cannot create empty playlist");
		}
		checkNull(playables);
		this.playables.addAll(Arrays.asList(playables));
	}
	
	/**
	 * Add array of {@link Song} to playlist
	 * @param playables
	 */
	public void add(Playable... playables){
		if (playables.length == 0){
			return;
		}
		checkNull(playables);
		this.playables.addAll(Arrays.asList(playables));
	}

	/**
	 * Insert array of {@link Song} at a specified index
	 * @param index
	 * @param playables
	 */
	public void insert(int index, Playable... playables){
		if (playables.length == 0){
			return;
		}
		if (index > this.playables.size()){
			throw new IllegalArgumentException("Index is higher than playlist size");
		}
		checkNull(playables);
		this.playables.addAll(index, Arrays.asList(playables));
	}
	
	private void checkNull(Playable... playables){
		List<Playable> playableList = Arrays.asList(playables);
		if (playableList.contains(null)){
			throw new IllegalArgumentException("Cannot add null to playlist");
		}
	}
	
	/**
	 * Removes songs from playlist
	 * @param playables
	 * @throws IllegalArgumentException when you try to remove all {@link Song} from {@link Playlist}
	 */
	public void remove(Playable... playables){
		ArrayList<Playable> songsTemp = new ArrayList<>();
		songsTemp.addAll(this.playables);
		songsTemp.removeAll(Arrays.asList(playables));
		if (songsTemp.size() > 0){
			this.playables = songsTemp;
		} else {
			throw new IllegalArgumentException("Cannot remove all songs from playlist");
		}
	}
	
	/**
	 * Get {@link Song} in playlist at specified index
	 * @param songNumber - song index
	 * @return
	 */
	public Playable get(int songNumber){
		return playables.get(songNumber);
	}
	
	/**
	 * Get number of {@link Song} in playlist
	 * @return
	 */
	public int getCount(){
		return playables.size();
	}
	
	/**
	 * Check whether {@link Song} is not last in playlist
	 * @param songNumber
	 * @return true if there is another {@link Song} after specified index
	 */
	public boolean hasNext(int songNumber){
		return playables.size() > (songNumber + 1);
	}
	
	/**
	 * Check whether {@link Song} with specified index exists in playlist
	 * @param songNumber
	 * @return
	 */
	public boolean exist(int songNumber){
		return playables.size() > songNumber;
	}

	/**
	 * Returns index of song.
	 * @param playable
	 * @return Index of song. -1 if song is not in playelist
	 */
	public int getIndex(Playable playable){	return playables.indexOf(playable);	}

	/**
	 * Check whether playlist contains song.
	 * @param playable
	 * @return
	 */
	public boolean contains(Playable playable) { return playables.contains(playable); }

	/**
	 * Returns list of Songs in Playlist
	 * @return
	 */	
	public ArrayList<Playable> getSongList(){
		return (ArrayList<Playable>) playables.clone();
	}
}
