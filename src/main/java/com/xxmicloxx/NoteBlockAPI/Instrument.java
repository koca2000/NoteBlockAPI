package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Sound;

public class Instrument {

    public static Sound getInstrument(byte instrument) {
    	if (!NoteBlockPlayerMain.plugin.isPre1_9()){
	    	switch (instrument) {
	            case 0:
	                return Sound.valueOf("BLOCK_NOTE_HARP");
	            case 1:
	                return Sound.valueOf("BLOCK_NOTE_BASS");
	            case 2:
	                return Sound.valueOf("BLOCK_NOTE_BASEDRUM");
	            case 3:
	                return Sound.valueOf("BLOCK_NOTE_SNARE");
	            case 4:
	                return Sound.valueOf("BLOCK_NOTE_HAT");
	            default:
	                return Sound.valueOf("BLOCK_NOTE_HARP");
	                
	        }
    	} else {
    		switch (instrument) {
    		case 0:
                return Sound.valueOf("NOTE_PIANO");
            case 1:
                return Sound.valueOf("NOTE_BASS_GUITAR");
            case 2:
                return Sound.valueOf("NOTE_BASS_DRUM");
            case 3:
                return Sound.valueOf("NOTE_SNARE_DRUM");
            case 4:
                return Sound.valueOf("NOTE_STICKS");
            default:
                return Sound.valueOf("NOTE_PIANO");
    		}
    	}
    }

    public static org.bukkit.Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return org.bukkit.Instrument.PIANO;
            case 1:
                return org.bukkit.Instrument.BASS_GUITAR;
            case 2:
                return org.bukkit.Instrument.BASS_DRUM;
            case 3:
                return org.bukkit.Instrument.SNARE_DRUM;
            case 4:
                return org.bukkit.Instrument.STICKS;
            default:
                return org.bukkit.Instrument.PIANO;
        }
    }
}
