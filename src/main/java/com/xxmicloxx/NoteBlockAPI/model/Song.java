package com.xxmicloxx.NoteBlockAPI.model;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Represents a Note Block Studio project
 */
public class Song implements Cloneable {

	private final HashMap<Integer, Layer> layers;
	private final short songHeight;
	private final short length;
	private final float speed;
	private final float delay;
	private final CustomInstrument[] customInstruments;
	private final int firstCustomInstrumentIndex;
	private final boolean isStereo;

	private SongMetadata metadata;

	/**
	 * Create Song instance by copying other Song parameters
	 * @param other song
	 */
	public Song(Song other) {
		this(other.getSpeed(), other.layers, other.getSongHeight(),
				other.getLength(), other.getMetadata(), other.getFirstCustomInstrumentIndex(), other.getCustomInstruments(), other.isStereo);
	}

	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layers,
				short songHeight, final short length, String title, String author, String originalAuthor,
				String description, File path, int firstCustomInstrumentIndex, boolean isStereo) {
		this(speed, layers, songHeight, length, title, author, originalAuthor, description, path, firstCustomInstrumentIndex, new CustomInstrument[0], isStereo);
	}

	@Deprecated
	public Song(float speed, HashMap<Integer, Layer> layers,
		short songHeight, final short length, String title, String author, String originalAuthor,
				String description, File path, int firstCustomInstrumentIndex, CustomInstrument[] customInstruments, boolean isStereo) {
		this(speed, layers, songHeight, length, new SongMetadata(title, author, originalAuthor, description, path), firstCustomInstrumentIndex, customInstruments, isStereo);
	}

	public Song(float speed, HashMap<Integer, Layer> layers, short songHeight, final short length,
				SongMetadata metadata, int firstCustomInstrumentIndex, CustomInstrument[] customInstruments, boolean isStereo) {
		this.speed = speed;
		delay = 20 / speed;
		this.layers = layers;
		this.songHeight = songHeight;
		this.length = length;
		this.metadata = metadata;
		this.firstCustomInstrumentIndex = firstCustomInstrumentIndex;
		this.customInstruments = customInstruments;
		this.isStereo = isStereo;
	}

	/**
	 * Gets all Layers in this Song and their index
	 * @deprecated Use {@link #getLayer(int)} or {@link #getLayers()} instead
	 * @return HashMap of Layers and their index
	 */
	@Deprecated
	public HashMap<Integer, Layer> getLayerHashMap() {
		return layers;
	}

	/**
	 * Returns unmodifiable collection of layers. Order of layers is not guaranteed in any way.
	 * @return Unmodifiable collection
	 */
	public Collection<Layer> getLayers(){
		return Collections.unmodifiableCollection(layers.values());
	}

	/**
	 * Returns {@link Layer} with the specified index or null if the layer doesn't have any notes
	 * @param index Index of the layer in the Song
	 * @return {@link Layer} or null if the layer with specified index doesn't have any notes
	 */
	public Layer getLayer(int index) {
		return layers.get(index);
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
	 * @deprecated Use {@link #getMetadata()}
	 * @return title of the Song
	 */
	@Deprecated
	public String getTitle() {
		return metadata.getTitle();
	}

	/**
	 * Gets the author of the Song
	 * @deprecated Use {@link #getMetadata()}
	 * @return author
	 */
	@Deprecated
	public String getAuthor() {
		return metadata.getAuthor();
	}

	/**
	 * Gets the original author of the Song
	 * @deprecated Use {@link #getMetadata()}
	 * @return author
	 */
	@Deprecated
	public String getOriginalAuthor() {
		return metadata.getOriginalAuthor();
	}

	/**
	 * Returns the File from which this Song is sourced
	 * @deprecated Use {@link #getMetadata()}
	 * @return file of this Song
	 */
	@Deprecated
	public File getPath() {
		return metadata.getPath();
	}

	/**
	 * Gets the description of this Song
	 * @deprecated Use {@link #getMetadata()}
	 * @return description
	 */
	@Deprecated
	public String getDescription() {
		return metadata.getDescription();
	}

	/**
	 * Gets the speed (ticks per second) of this Song
	 * @return ticks per second
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

	public SongMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(SongMetadata metadata) {
		this.metadata = metadata;
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
	 */
	public boolean isStereo() {
		return isStereo;
	}
}
