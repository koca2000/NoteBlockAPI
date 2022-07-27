package com.xxmicloxx.NoteBlockAPI.model;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Note Block Studio project
 *
 */
@Deprecated
public class Song implements Cloneable {

	private File path;

	private cz.koca2000.nbs4j.Song song;

	/**
	 * Create Song instance by copying other Song parameters
	 * @param other song
	 */
	public Song(Song other) {
		this(other.getSpeed(), other.getLayerHashMap(), other.getSongHeight(),
				other.getLength(), other.getTitle(), other.getAuthor(), other.getOriginalAuthor(),
				other.getDescription(), other.getPath(), other.getFirstCustomInstrumentIndex(), other.getCustomInstruments(), other.isStereo());
	}

	/**
	 * @deprecated Use {@link #Song(float, HashMap, short, short, String, String, String, File, int, boolean)}
	 * @param speed
	 * @param layerHashMap
	 * @param songHeight
	 * @param length
	 * @param title
	 * @param author
	 * @param description
	 * @param path
	 */
	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path) {
		this(speed, layerHashMap, songHeight, length, title, author, description, path, InstrumentUtils.getCustomInstrumentFirstIndex(), new CustomInstrument[0], false);
	}

	/**
	 * @deprecated Use {@link #Song(float, HashMap, short, short, String, String, String, File, int, CustomInstrument[], boolean)}
	 * @param speed
	 * @param layerHashMap
	 * @param songHeight
	 * @param length
	 * @param title
	 * @param author
	 * @param description
	 * @param path
	 * @param customInstruments
	 */
	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path, CustomInstrument[] customInstruments) {
		this(speed, layerHashMap, songHeight, length, title, author, description, path, InstrumentUtils.getCustomInstrumentFirstIndex(), customInstruments, false);
	}

	/**
	 * @deprecated Use {@link #Song(float, HashMap, short, short, String, String, String, File, int, boolean)}
	 * @param speed
	 * @param layerHashMap
	 * @param songHeight
	 * @param length
	 * @param title
	 * @param author
	 * @param description
	 * @param path
	 * @param firstCustomInstrumentIndex
	 */
	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path, int firstCustomInstrumentIndex) {
		this(speed, layerHashMap, songHeight, length, title, author, description, path, firstCustomInstrumentIndex, new CustomInstrument[0], false);
	}

	/**
	 * @deprecated Use {@link #Song(float, HashMap, short, short, String, String, String, File, int, CustomInstrument[], boolean)}
	 * @param speed
	 * @param layerHashMap
	 * @param songHeight
	 * @param length
	 * @param title
	 * @param author
	 * @param description
	 * @param path
	 * @param firstCustomInstrumentIndex
	 * @param customInstruments
	 */
	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path, int firstCustomInstrumentIndex, CustomInstrument[] customInstruments) {
		this(speed, layerHashMap, songHeight, length, title, author, description, path, firstCustomInstrumentIndex, customInstruments, false);
	}

	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path, int firstCustomInstrumentIndex, boolean isStereo) {
		this(speed, layerHashMap, songHeight, length, title, author, "", description, path, firstCustomInstrumentIndex, new CustomInstrument[0], isStereo);
	}

	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author,
				String description, File path, int firstCustomInstrumentIndex, CustomInstrument[] customInstruments, boolean isStereo) {
		this(speed, layerHashMap, songHeight, length, title, author, "", description, path, firstCustomInstrumentIndex, customInstruments, isStereo);
	}

	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author, String originalAuthor,
				String description, File path, int firstCustomInstrumentIndex, boolean isStereo) {
		this(speed, layerHashMap, songHeight, length, title, author, originalAuthor, description, path, firstCustomInstrumentIndex, new CustomInstrument[0], isStereo);
	}

	public Song(float speed, HashMap<Integer, Layer> layerHashMap,
				short songHeight, final short length, String title, String author, String originalAuthor,
				String description, File path, int firstCustomInstrumentIndex, CustomInstrument[] customInstruments, boolean isStereo) {

		this.path = path;
		song = new cz.koca2000.nbs4j.Song()
				.setLength(length)
				.setTempoChange(-1, speed)
				.setLayersCount(songHeight);

		song.getMetadata()
				.setTitle(title)
				.setAuthor(author)
				.setOriginalAuthor(originalAuthor)
				.setDescription(description);

		for (Map.Entry<Integer, Layer> entry : layerHashMap.entrySet()){
			int index = entry.getKey();
			Layer layer = entry.getValue();
			song.getLayer(index)
					.setName(layer.getName())
					.setVolume(layer.getVolume())
					.setPanning(layer.getPanning() - 100);

			for (Map.Entry<Integer, Note> noteEntry : layer.getNotesAtTicks().entrySet()){
				cz.koca2000.nbs4j.Note note = new cz.koca2000.nbs4j.Note(noteEntry.getValue().getNote());
				if (note.getInstrument() > firstCustomInstrumentIndex){
					note.setInstrument(note.getInstrument() - firstCustomInstrumentIndex, true);
				}
				song.setNote(noteEntry.getKey(), index, note);
			}
		}

		for (CustomInstrument instrument : customInstruments){
			song.addCustomInstrument(new cz.koca2000.nbs4j.CustomInstrument()
					.setName(instrument.getName())
					.setFileName(instrument.getSoundFileName()));
		}
	}

	public Song(cz.koca2000.nbs4j.Song song){
		this.song = song;
	}

	/**
	 * Gets all Layers in this Song and their index
	 * @return HashMap of Layers and their index
	 */
	public HashMap<Integer, Layer> getLayerHashMap() {
		HashMap<Integer, Layer> layers = new HashMap<>();
		for (int i = 0; i < song.getLayersCount(); i++){
			layers.put(i, new Layer(song.getLayer(i)));
		}
		return layers;
	}

	/**
	 * Gets the Song's height
	 * @return Song height
	 */
	public short getSongHeight() {
		return (short) song.getLayersCount();
	}

	/**
	 * Gets the length in ticks of this Song
	 * @return length of this Song
	 */
	public short getLength() {
		return (short) song.getSongLength();
	}

	/**
	 * Gets the title / name of this Song
	 * @return title of the Song
	 */
	public String getTitle() {
		return song.getMetadata().getTitle();
	}

	/**
	 * Gets the author of the Song
	 * @return author
	 */
	public String getAuthor() {
		return song.getMetadata().getAuthor();
	}

	/**
	 * Gets the original author of the Song
	 * @return author
	 */
	public String getOriginalAuthor() {
		return song.getMetadata().getOriginalAuthor();
	}

	/**
	 * Returns the File from which this Song is sourced
	 * @return file of this Song
	 */
	public File getPath() {
		return path;
	}

	/**
	 * Gets the description of this Song
	 * @return description
	 */
	public String getDescription() {
		return song.getMetadata().getDescription();
	}

	/**
	 * Gets the speed (ticks per second) of this Song
	 * @return
	 */
	public float getSpeed() {
		return song.getTempo(0);
	}

	/**
	 * Gets the delay of this Song
	 * @return delay
	 */
	public float getDelay() {
		return 20 / song.getTempo(0);
	}

	/**
	 * Gets the CustomInstruments made for this Song
	 * @see CustomInstrument
	 * @return array of CustomInstruments
	 */
	public CustomInstrument[] getCustomInstruments() {
		CustomInstrument[] instruments = new CustomInstrument[song.getCustomInstrumentsCount()];
		for (int i = 0; i < instruments.length; i++){
			instruments[i] = new CustomInstrument(song.getCustomInstrument(i));
		}
		return instruments;
	}

	@Override
	public Song clone() {
		return new Song(this);
	}

	public int getFirstCustomInstrumentIndex() {
		return song.getNonCustomInstrumentsCount();
	}

	/**
	 * Returns true if song has at least one stereo {@link Note} or {@link Layer} in nbs file
	 * @return
	 */
	public boolean isStereo() {
		return song.isStereo();
	}

	public cz.koca2000.nbs4j.Song getSong(){
		return song;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Song){
			return song.equals(((Song)o).getSong());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return song.hashCode();
	}
}
