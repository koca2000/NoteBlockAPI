package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Sound;

import com.xxmicloxx.NoteBlockAPI.CompatibilityUtils.NoteBlockCompatibility;

public class Instrument {

    public static Sound getInstrument(byte instrument) {
    	if (CompatibilityUtils.getCompatibility() == NoteBlockCompatibility.pre1_9){
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
    	} else {
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
			}
			
			if (CompatibilityUtils.getCompatibility() == NoteBlockCompatibility.post1_12){
				switch (instrument) {
		            case 5:
		            	return Sound.valueOf("BLOCK_NOTE_GUITAR");
		            case 6:
		            	return Sound.valueOf("BLOCK_NOTE_FLUTE");
		            case 7:
		            	return Sound.valueOf("BLOCK_NOTE_BELL");
		            case 8:
		                return Sound.valueOf("BLOCK_NOTE_CHIME");
		            case 9:
		                return Sound.valueOf("BLOCK_NOTE_XYLOPHONE");
				}
			}
			
			return Sound.valueOf("BLOCK_NOTE_HARP");
			
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
    
    public static boolean isCustomInstrument(byte instrument){
    	if (CompatibilityUtils.getCompatibility() != NoteBlockCompatibility.post1_12){
    		if (instrument > 4){
    			return true;
    		}
    		return false;
    	} else {
    		if (instrument > 9){
    			return true;
    		}
    		return false;
    	}
    }
    
    public static byte getCustomInstrumentFirstIndex(){
    	if (CompatibilityUtils.getCompatibility() != NoteBlockCompatibility.post1_12){
    		return 5;
    	} else {
    		return 10;
    	}
    }
}
