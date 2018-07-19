package com.xxmicloxx.NoteBlockAPI;

public class CustomInstrument {

	private byte index;
	private String name;
	private String soundFileName;
	private org.bukkit.Sound sound;

	@Deprecated
	public CustomInstrument(byte index, String name, String soundFileName, byte pitch, byte press) {
		this.index = index;
		this.name = name;
		this.soundFileName = soundFileName.replaceAll(".ogg", "");
		if (this.soundFileName.equalsIgnoreCase("pling")){
			this.sound = Sound.getFromBukkitName("BLOCK_NOTE_PLING");
		}
	}

	public CustomInstrument(byte index, String name, String soundFileName) {
		this.index = index;
		this.name = name;
		this.soundFileName = soundFileName.replaceAll(".ogg", "");
		if (this.soundFileName.equalsIgnoreCase("pling")){
			this.sound = Sound.getFromBukkitName("BLOCK_NOTE_PLING");
		}
	}

	public byte getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	@Deprecated
	public String getSoundfile() {
		return getSoundFileName();
	}

	public String getSoundFileName() {
		return soundFileName;
	}

	public org.bukkit.Sound getSound() {
		return sound;
	}

}
