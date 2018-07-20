package com.xxmicloxx.NoteBlockAPI;

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
	 * @deprecated Unused parameters
	 */
	public CustomInstrument(byte index, String name, String soundFileName, byte pitch, byte press) {
		this.index = index;
		this.name = name;
		this.soundFileName = soundFileName.replaceAll(".ogg", "");
		if (this.soundFileName.equalsIgnoreCase("pling")){
			this.sound = Sound.getFromBukkitName("BLOCK_NOTE_PLING");
		}
	}

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
			this.sound = Sound.getFromBukkitName("BLOCK_NOTE_PLING");
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
	 * @deprecated misleading name
	 */
	@Deprecated
	public String getSoundfile() {
		return getSoundFileName();
	}

	/**
	 * Gets file name of the sound
	 * @return file name
	 */
	public String getSoundFileName() {
		return soundFileName;
	}

	/**
	 * Gets the Sound enum for this CustomInstrument
	 * @return
	 */
	public org.bukkit.Sound getSound() {
		return sound;
	}

}
