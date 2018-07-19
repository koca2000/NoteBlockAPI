package com.xxmicloxx.NoteBlockAPI;

public class CustomInstrument {
	
	private byte index;
	private String name;
	private String soundfile;
	private byte pitch;
	private byte press;
	private org.bukkit.Sound sound;
	
	public CustomInstrument(byte index, String name, String soundfile, byte pitch, byte press){
		this.index = index;
		this.name = name;
		this.soundfile = soundfile.replaceAll(".ogg", "");
		if (this.soundfile.equalsIgnoreCase("pling")){
			this.sound = Sound.getFromBukkitName("BLOCK_NOTE_PLING");
		}
		this.pitch = pitch;
		this.press = press;
	}

	public byte getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getSoundfile() {
		return soundfile;
	}
	
	public org.bukkit.Sound getSound() {
		return sound;
	}
}
