package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Sound;

public class CustomInstrument {
	
	private byte index;
	private String name;
	private String soundfile;
	private byte pitch;
	private byte press;
	private Sound sound;
	
	public CustomInstrument(byte index, String name, String soundfile, byte pitch, byte press){
		this.index = index;
		this.name = name;
		this.soundfile = soundfile.replaceAll(".ogg", "");
		if (this.soundfile.equalsIgnoreCase("pling")){
			switch (NoteBlockPlayerMain.getCompatibility()){
				case NoteBlockPlayerMain.NoteBlockCompatibility.pre1_9:
					this.sound = Sound.valueOf("NOTE_PLING");
					break;
				case NoteBlockPlayerMain.NoteBlockCompatibility.pre1_12:
				case NoteBlockPlayerMain.NoteBlockCompatibility.post1_12:
					this.sound = Sound.valueOf("BLOCK_NOTE_PLING");
					break;
			}
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
	
	public Sound getSound(){
		return sound;
	}
}
