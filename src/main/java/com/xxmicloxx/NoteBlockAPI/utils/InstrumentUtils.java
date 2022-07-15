package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Sound;
import cz.koca2000.nbs4j.CustomInstrument;
import cz.koca2000.nbs4j.Note;
import org.bukkit.Instrument;

/**
 * Various methods for working with instruments
 */
public class InstrumentUtils {

	/**
	 * Returns the org.bukkit.Sound enum for the current server version
	 *
	 * @param instrument
	 * @return Sound enum (for the current server version)
	 * @see Sound
	 */
	public static org.bukkit.Sound getInstrument(byte instrument) {
		Sound sound = Sound.getByIndex(instrument);
		if (sound == null || sound.getSound() == null)
			throw new IllegalArgumentException("Instrument with index " + instrument + " is not available.");
		return sound.getSound();
	}

	/**
	 * Add suffix to vanilla instrument to use sound outside 2 octave range
	 * @param instrument instrument id
	 * @param key sound key
	 * @param pitch
	 * @return warped name
	 */
	public static String warpNameOutOfRange(byte instrument, byte key, short pitch) {
		return warpNameOutOfRange(getSoundNameByInstrument(instrument), key, pitch);
	}

	/**
	 * Add suffix to qualified name to use sound outside 2 octave range
	 *
	 * @param name qualified name
	 * @param key sound key
	 * @param pitch
	 * @return warped name
	 */
	public static String warpNameOutOfRange(String name, byte key, short pitch) {
		key = NoteUtils.applyPitchToKey(key, pitch);
		// -15 base_-2
		// 9 base_-1
		// 33 base
		// 57 base_1
		// 81 base_2
		// 105 base_3
		if(key < 9) name += "_-2";
		else if(key < 33) name += "_-1";
		else if(key >= 57 && key < 81) name += "_1";
		else if(key < 105) name += "_2";
		return name;
	}

	/**
	 * Returns the name of vanilla instrument
	 *
	 * @param instrument instrument identifier
	 * @return Sound name with full qualified name
	 */
	public static String getSoundNameByInstrument(byte instrument) {
		Sound sound = Sound.getByIndex(instrument);
		if (sound == null)
			sound = Sound.NOTE_PIANO;
		return sound.getResourcePackName();
	}

	/**
	 * Returns the name of the org.bukkit.Sound enum for the current server version
	 *
	 * @param instrument
	 * @return Sound enum name (for the current server version)
	 * @see Sound
	 */
	public static String getInstrumentName(byte instrument) {
		Sound sound = Sound.getByIndex(instrument);
		if (sound == null)
			sound = Sound.NOTE_PIANO;
		org.bukkit.Sound bukkitSound = sound.getSound();
		if (bukkitSound == null)
			throw new IllegalArgumentException("Instrument is not available in this server's version.");

		return bukkitSound.name();
	}

	/**
	 * Returns the name of the org.bukkit.Instrument enum for the current server version
	 * @param instrument
	 * @return Instrument enum (for the current server version)
	 */
	public static Instrument getBukkitInstrument(byte instrument) {
		switch (instrument) {
			case 0:
				return Instrument.PIANO;
			case 1:
				return Instrument.BASS_GUITAR;
			case 2:
				return Instrument.BASS_DRUM;
			case 3:
				return Instrument.SNARE_DRUM;
			case 4:
				return Instrument.STICKS;
			default: {
				if (CompatibilityUtils.getServerVersion() >= 0.0112f) {
					switch (instrument) {
						case 5:
							return Instrument.valueOf("GUITAR");
						case 6:
							return Instrument.valueOf("FLUTE");
						case 7:
							return Instrument.valueOf("BELL");
						case 8:
							return Instrument.valueOf("CHIME");
						case 9:
							return Instrument.valueOf("XYLOPHONE");
						default: {
							if (CompatibilityUtils.getServerVersion() >= 0.0114f) {
								switch (instrument) {
									case 10:
										return Instrument.valueOf("IRON_XYLOPHONE");
									case 11:
										return Instrument.valueOf("COW_BELL");
									case 12:
										return Instrument.valueOf("DIDGERIDOO");
									case 13:
										return Instrument.valueOf("BIT");
									case 14:
										return Instrument.valueOf("BANJO");
									case 15:
										return Instrument.valueOf("PLING");
								}
							}
							return Instrument.PIANO;
						}
					}
				}
				return Instrument.PIANO;
			}
		}
	}

	/**
	 * If true, the byte given represents a custom instrument
	 * @param instrument
	 * @return whether the byte represents a custom instrument
	 */
	public static boolean isCustomInstrument(byte instrument) {
		return instrument >= getCustomInstrumentFirstIndex();
	}

	/**
	 * Gets the first index in which a custom instrument 
	 * can be added to the existing list of instruments
	 * @return index where an instrument can be added
	 */
	public static byte getCustomInstrumentFirstIndex() {
		if (CompatibilityUtils.getServerVersion() >= 0.0114f) {
			return 16;
		}
		if (CompatibilityUtils.getServerVersion() >= 0.0112f) {
			return 10;
		}
		return 5;
	}

	/**
	 * Returns custom instrument for the given song if custom instrument is needed for its playback.
	 * @param note note the custom instrument is wanted for
	 * @return {@link CustomInstrument} or null if no custom instrument is needed
	 */
	public static CustomInstrument getCustomInstrumentForNote(Note note){
		if (note.isCustomInstrument() && note.getLayer() != null && note.getLayer().getSong() != null)
			return note.getLayer().getSong().getCustomInstrument(note.getInstrument());
		else if (isCustomInstrument((byte) note.getInstrument())){
			Sound sound = Sound.getByIndex(note.getInstrument());
			if (sound != null)
				return new CustomInstrument()
						.setName(sound.getResourcePackName())
						.setFileName(sound.getResourcePackName());
		}
		return null;
	}
}