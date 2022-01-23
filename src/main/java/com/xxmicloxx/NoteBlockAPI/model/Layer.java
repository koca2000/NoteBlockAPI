package com.xxmicloxx.NoteBlockAPI.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a series of notes in Note Block Studio. 
 * A Layer can have a maximum of one note per tick
 */
public class Layer {

	private Map<Integer, Note> notesAtTicks;
	private byte volume = 100;
	private int panning = 100;
	private String name = "";

	@Deprecated
	public Layer(){
		notesAtTicks = new HashMap<>();
	}

	public Layer(Map<Integer, Note> notes){
		notesAtTicks = notes;
	}

	/**
	 * Gets the notes in the Layer with the tick they are created as a hash map
	 * @deprecated
	 * @return HashMap of notes with the tick they are played at
	 */
	@Deprecated
	public HashMap<Integer, Note> getNotesAtTicks() {
		return new HashMap<>(notesAtTicks);
	}

	/**
	 * Sets the notes in the Layer with the tick they are created as a hash map
	 * @deprecated Notes in layer can not be changed anymore
	 */
	@Deprecated
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
	 * @deprecated Notes in layer can not be changed anymore
	 */
	@Deprecated
	public void setNote(int tick, Note note) {
		notesAtTicks.put(tick, note);
	}

	/**
	 * Returns collection of all notes in this layer
	 */
	public Collection<Note> getNotes(){
		return Collections.unmodifiableCollection(notesAtTicks.values());
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
	 * @param volume Value in range 0 - 100
	 */
	public void setVolume(byte volume) {
		if (volume < 0) volume = 0;
		if (volume > 100) volume = 100;
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
