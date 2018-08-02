package com.xxmicloxx.NoteBlockAPI.model;

/**
 * Create custom instruments from a sound file
 * 
 */
public class CustomInstrument {
	
	private byte index;
	private String name;
	private String soundFileName;
	private org.bukkit.Sound sound;

	/**
	 * Creates a CustomInstrument
	 * @param index
	 * @param name
	 * @param soundFileName
	 */
	public CustomInstrument(byte index, String name, String soundFileName) {
		this.index = index;
		this.name = name;
		this.soundFileName = soundFileName.replaceAll(".ogg", "");
		if (this.soundFileName.equalsIgnoreCase("pling")){
			this.sound = Sound.NOTE_PLING.bukkitSound();
		}
	}

	/**
	 * Gets index of CustomInstrument
	 * @return index
	 */
	public byte getIndex() {
		return index;
	}

	/**
	 * Gets name of CustomInstrument
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets file name of the sound
	 * @return file name
	 */
	public String getSoundFileName() {
		return soundFileName;
	}

	/**
	 * Gets the org.bukkit.Sound enum for this CustomInstrument
	 * @return org.bukkit.Sound enum
	 */
	public org.bukkit.Sound getSound() {
		return sound;
	}
}
