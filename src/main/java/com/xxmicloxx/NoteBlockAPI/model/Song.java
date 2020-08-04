package com.xxmicloxx.NoteBlockAPI.model;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Represents a Note Block Studio project
 *
 */
public class Song implements Cloneable {

	private HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
	private short songHeight;
	private short length;
	private String title;
	private File path;
	private String author;
	private String originalAuthor;
	private String description;
	private float speed;
	private float delay;
	private CustomInstrument[] customInstruments;
	private int firstCustomInstrumentIndex;
	private boolean isStereo = false;

	/**
	 * Create Song instance by copying other Song parameters
	 * @param other song
	 */
	public Song(Song other) {
		this(other.getSpeed(), other.getLayerHashMap(), other.getSongHeight(), 
				other.getLength(), other.getTitle(), other.getAuthor(), other.getOriginalAuthor(),
				other.getDescription(), other.getPath(), other.getFirstCustomInstrumentIndex(), other.getCustomInstruments(), other.isStereo);
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
		this.speed = speed;
		delay = 20 / speed;
		this.layerHashMap = layerHashMap;
		this.songHeight = songHeight;
		this.length = length;
		this.title = title;
		this.author = author;
		this.originalAuthor = originalAuthor;
		this.description = description;
		this.path = path;
		this.firstCustomInstrumentIndex = firstCustomInstrumentIndex;
		this.customInstruments = customInstruments;
		this.isStereo = isStereo;
	}

	/**
	 * Gets all Layers in this Song and their index
	 * @return HashMap of Layers and their index
	 */
	public HashMap<Integer, Layer> getLayerHashMap() {
		return layerHashMap;
	}

	/**
	 * Gets the Song's height
	 * @return Song height
	 */
	public short getSongHeight() {
		return songHeight;
	}

	/**
	 * Gets the length in ticks of this Song
	 * @return length of this Song
	 */
	public short getLength() {
		return length;
	}

	/**
	 * Gets the title / name of this Song
	 * @return title of the Song
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the author of the Song
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the original author of the Song
	 * @return author
	 */
	public String getOriginalAuthor() {
		return originalAuthor;
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
		return description;
	}

	/**
	 * Gets the speed (ticks per second) of this Song
	 * @return
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Gets the delay of this Song
	 * @return delay
	 */
	public float getDelay() {
		return delay;
	}

	/**
	 * Gets the CustomInstruments made for this Song
	 * @see CustomInstrument
	 * @return array of CustomInstruments
	 */
	public CustomInstrument[] getCustomInstruments() {
		return customInstruments;
	}

	@Override
	public Song clone() {
		return new Song(this);
	}

	public int getFirstCustomInstrumentIndex() {
		return firstCustomInstrumentIndex;
	}

	/**
	 * Returns true if song has at least one stereo {@link Note} or {@link Layer} in nbs file
	 * @return
	 */
	public boolean isStereo() {
		return isStereo;
	}
}
