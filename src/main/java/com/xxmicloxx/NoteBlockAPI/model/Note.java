package com.xxmicloxx.NoteBlockAPI.model;

/**
 * Represents a note played; contains the instrument and the key
 * @see NotePitch
 *
 */
public class Note {

	private byte instrument;
	private byte key;

	private short pitch;
	
	public Note(byte instrument, byte key, byte velocity, byte panning, short pitch) {
		this.instrument = instrument;
		this.key = key;
		this.pitch = pitch;
	}

	/**
	 * Gets instrument number
	 */
	public byte getInstrument() {
		return instrument;
	}

	/**
	 * Sets instrument number
	 */
	public void setInstrument(byte instrument) {
		this.instrument = instrument;
	}

	public byte getKey() {
		return key;
	}

	public void setKey(byte key) {
		this.key = key;
	}

	public short getPitch() {
		return pitch;
	}

	public void setPitch(short pitch) {
		this.pitch = pitch;
	}
}
