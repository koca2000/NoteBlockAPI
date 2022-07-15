package com.xxmicloxx.NoteBlockAPI.model;

/**
 * Represents a note played; contains the instrument and the key
 *
 */
@Deprecated
public class Note {

	private final cz.koca2000.nbs4j.Note note;

	@Deprecated
	public Note(byte instrument, byte key) {
		this(instrument, key, (byte) 100, (byte) 100, (short) 0);
	}

	@Deprecated
	public Note(byte instrument, byte key, byte velocity, int panning, short pitch) {
		note = new cz.koca2000.nbs4j.Note()
				.setInstrument(instrument)
				.setKey(key)
				.setVolume(velocity)
				.setPanning(panning)
				.setPitch(pitch);
	}

	public Note(cz.koca2000.nbs4j.Note note){
		this.note = note;
	}

	/**
	 * Gets instrument number
	 */
	@Deprecated
	public byte getInstrument() {
		return (byte) note.getInstrument();
	}

	/**
	 * Sets instrument number
	 */
	@Deprecated
	public void setInstrument(byte instrument) {
		note.setInstrument(instrument);
	}

	/**
	 * Returns note key number (Minecraft key 0 corresponds to key 33)
	 * @return
	 */
	@Deprecated
	public byte getKey() {
		return (byte) note.getKey();
	}

	/**
	 * Sets note key number (Minecraft key 0 corresponds to key 33)
	 * @param key
	 */
	@Deprecated
	public void setKey(byte key) {
		note.setKey(key);
	}

	/**
	 * Returns note pitch.
	 * 100 = 1 key
	 * 1200 = 1 octave
 	 * @return
	 */
	@Deprecated
	public short getPitch() {
		return (short) note.getPitch();
	}

	/**
	 * Sets note pitch.
	 * 100 = 1 key
	 * 1200 = 1 octave
	 * @param pitch note pitch
	 */
	@Deprecated
	public void setPitch(short pitch) {
		note.setPitch(pitch);
	}

	/**
	 * Returns note velocity (volume)
	 * @return
	 */
	@Deprecated
	public byte getVelocity() {
		return note.getVolume();
	}

	/**
	 * Sets note velocity (volume)
	 * @param velocity number from 0 - 100
	 */
	@Deprecated
	public void setVelocity(byte velocity) {
		if (velocity < 0) velocity = 0;
		if (velocity > 100) velocity = 100;

		note.setVolume(velocity);
	}

	/**
	 * Returns stereo panning of this note
	 * @return
	 */
	@Deprecated
	public int getPanning() {
		return note.getPanning();
	}

	/**
	 * Sets stereo panning of this note
	 * @param panning
	 */
	@Deprecated
	public void setPanning(int panning) {
		note.setPanning(panning);
	}

	public cz.koca2000.nbs4j.Note getNote(){
		return note;
	}
}
