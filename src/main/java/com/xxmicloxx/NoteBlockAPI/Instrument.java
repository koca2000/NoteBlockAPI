package com.xxmicloxx.NoteBlockAPI;

import com.xxmicloxx.NoteBlockAPI.model.Sound;
import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils}
 */
@Deprecated
public class Instrument {

	/**
	 * Returns the org.bukkit.Sound enum for the current server version
	 * @param instrument
	 * @see Sound
	 * @return Sound enum (for the current server version)
	 */
	public static org.bukkit.Sound getInstrument(byte instrument) {
		return org.bukkit.Sound.valueOf(getInstrumentName(instrument));
	}

	/**
	 * Returns the name of the org.bukkit.Sound enum for the current server version
	 * @param instrument
	 * @see Sound
	 * @return Sound enum name (for the current server version)
	 */
	public static String getInstrumentName(byte instrument) {
		switch (instrument) {
			case 0:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HARP").name();
			case 1:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASS").name();
			case 2:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BASEDRUM").name();
			case 3:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_SNARE").name();
			case 4:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HAT").name();
			case 5:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_GUITAR").name();
			case 6:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_FLUTE").name();
			case 7:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BELL").name();
			case 8:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_CHIME").name();
			case 9:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_XYLOPHONE").name();
			case 10:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_IRON_XYLOPHONE").name();
			case 11:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_COW_BELL").name();
			case 12:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_DIDGERIDOO").name();
			case 13:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BIT").name();
			case 14:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_BANJO").name();
			case 15:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_PLING").name();
			default:
				return Sound.getFromBukkitName("BLOCK_NOTE_BLOCK_HARP").name();
		}
}

	/**
	 * Returns the name of the org.bukkit.Instrument enum for the current server version
	 * @param instrument
	 * @return Instrument enum (for the current server version)
	 */
	public static org.bukkit.Instrument getBukkitInstrument(byte instrument) {
		return InstrumentUtils.getBukkitInstrument(instrument);
	}

	/**
	 * If true, the byte given represents a custom instrument
	 * @param instrument
	 * @return whether the byte represents a custom instrument
	 */
	public static boolean isCustomInstrument(byte instrument) {
		return InstrumentUtils.isCustomInstrument(instrument);
	}

	/**
	 * Gets the first index in which a custom instrument 
	 * can be added to the existing list of instruments
	 * @return index where an instrument can be added
	 */
	public static byte getCustomInstrumentFirstIndex() {
		return InstrumentUtils.getCustomInstrumentFirstIndex();
	}
	
}
