package com.xxmicloxx.NoteBlockAPI.model;

import java.util.HashMap;

/**
 * Represents a series of notes in Note Block Studio. 
 * A Layer can have a maximum of one note per tick (20 ticks a second)
 *
 */
public class Layer {

	private HashMap<Integer, Note> notesAtTicks = new HashMap<Integer, Note>();
	private byte volume = 100;
	private int panning = 100;
	private String name = "";

	/**
	 * Gets the notes in the Layer with the tick they are created as a hash map
	 * @return HashMap of notes with the tick they are played at
	 */
	public HashMap<Integer, Note> getNotesAtTicks() {
		return notesAtTicks;
	}

	/**
	 * Sets the notes in the Layer with the tick they are created as a hash map
	 */
	public void setNotesAtTicks(HashMap<Integer, Note> notesAtTicks) {
		this.notesAtTicks = notesAtTicks;
	}

	/**
	 * Gets the name of the Layer
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Layer
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the note played at a given tick
	 */
	public Note getNote(int tick) {
		return notesAtTicks.get(tick);
	}

	/**
	 * Sets the given note at the given tick in the Layer
	 */
	public void setNote(int tick, Note note) {
		notesAtTicks.put(tick, note);
	}

	/**
	 * Gets the volume of all notes in the Layer
	 * @return byte representing the volume
	 */
	public byte getVolume() {
		return volume;
	}

	/**
	 * Sets the volume for all notes in the Layer
	 * @param volume
	 */
	public void setVolume(byte volume) {
		this.volume = volume;
	}

	/**
	 * Gets the panning of all notes in the Layer
	 * @return byte representing the panning
	 */
	public int getPanning() {
		return panning;
	}

	/**
	 * Sets the panning for all notes in the Layer
	 * @param panning
	 */
	public void setPanning(int panning) {
		this.panning = panning;
	}
}
