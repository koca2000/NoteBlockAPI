package com.xxmicloxx.NoteBlockAPI.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a series of notes in Note Block Studio. 
 * A Layer can have a maximum of one note per tick
 * @deprecated Use {@link cz.koca2000.nbs4j.Layer}
 */
@Deprecated
public class Layer {

	private final cz.koca2000.nbs4j.Layer layer;

	@Deprecated
	public Layer(){
		layer = new cz.koca2000.nbs4j.Layer();
	}

	public Layer(cz.koca2000.nbs4j.Layer layer){
		this.layer = layer;
	}

	/**
	 * Gets the notes in the Layer with the tick they are created as a hash map
	 * @deprecated
	 * @return HashMap of notes with the tick they are played at
	 */
	@Deprecated
	public HashMap<Integer, Note> getNotesAtTicks() {
		HashMap<Integer, Note> notes = new HashMap<>();
		Map<Integer, cz.koca2000.nbs4j.Note> layerNotes = layer.getNotes();
		for (Map.Entry<Integer, cz.koca2000.nbs4j.Note> noteEntry : layerNotes.entrySet()){
			notes.put(noteEntry.getKey(), new Note(noteEntry.getValue()));
		}
		return notes;
	}

	/**
	 * Sets the notes in the Layer with the tick they are created as a hash map
	 * @deprecated Notes in layer can not be changed anymore
	 */
	@Deprecated
	public void setNotesAtTicks(HashMap<Integer, Note> notesAtTicks) {
		if (layer.getSong() != null){
			cz.koca2000.nbs4j.Song song = layer.getSong();
			int index = getLayerIndex();

			for (Map.Entry<Integer, Note> noteEntry : notesAtTicks.entrySet()){
				song.setNote(noteEntry.getKey(), index, noteEntry.getValue().getNote());
			}
		}
	}

	/**
	 * Gets the name of the Layer
	 */
	@Deprecated
	public String getName() {
		return layer.getName();
	}

	/**
	 * Sets the name of the Layer
	 */
	@Deprecated
	public void setName(String name) {
		layer.setName(name);
	}

	/**
	 * Gets the note played at a given tick
	 */
	@Deprecated
	public Note getNote(int tick) {
		cz.koca2000.nbs4j.Note note = layer.getNote(tick);
		if (note == null)
			return null;
		return new Note(note);
	}

	/**
	 * Sets the given note at the given tick in the Layer
	 * @deprecated Notes in layer can not be changed anymore
	 */
	@Deprecated
	public void setNote(int tick, Note note) {
		if (layer.getSong() != null){
			int index = getLayerIndex();
			layer.getSong().setNote(tick, index, note.getNote());
		}
	}

	/**
	 * Gets the volume of all notes in the Layer
	 * @return byte representing the volume
	 */
	@Deprecated
	public byte getVolume() {
		return (byte) layer.getVolume();
	}

	/**
	 * Sets the volume for all notes in the Layer
	 * @param volume Value in range 0 - 100
	 */
	@Deprecated
	public void setVolume(byte volume) {
		if (volume < 0) volume = 0;
		if (volume > 100) volume = 100;
		layer.setVolume(volume);
	}

	/**
	 * Gets the panning of all notes in the Layer
	 * @return byte representing the panning
	 */
	@Deprecated
	public int getPanning() {
		return layer.getPanning() + 100;
	}

	/**
	 * Sets the panning for all notes in the Layer
	 * @param panning
	 */
	@Deprecated
	public void setPanning(int panning) {
		layer.setPanning(panning - 100);
	}

	public cz.koca2000.nbs4j.Layer getLayer(){
		return layer;
	}

	private int getLayerIndex(){
		cz.koca2000.nbs4j.Song song = layer.getSong();
		if (song == null)
			return -1;
		for (int index = 0; index < song.getLayersCount(); index++){
			if (song.getLayer(index) == layer)
				return index;
		}
		return -1;
	}
}
