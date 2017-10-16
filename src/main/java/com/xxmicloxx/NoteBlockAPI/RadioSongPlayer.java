package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;

public class RadioSongPlayer extends SongPlayer {

    public RadioSongPlayer(Song song) {
        super(song);
    }

    public RadioSongPlayer(Song song, SoundCategory soundCategory) {
    	super(song, soundCategory);
    }

    @Override
    public void playTick(Player p, int tick) {
        byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(p);

        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            if (Instrument.isCustomInstrument(note.getInstrument())){
            	if (song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound() != null){
            		CompatibilityUtils.playSound(p, p.getEyeLocation(),
                            song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound(),
                            this.soundCategory,(l.getVolume() * (int) volume * (int) playerVolume) / 1000000f,
                            NotePitch.getPitch(note.getKey() - 33));
            	}else {
            		CompatibilityUtils.playSound(p, p.getEyeLocation(),
                            song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSoundfile(),
                            this.soundCategory,(l.getVolume() * (int) volume * (int) playerVolume) / 1000000f,
                            NotePitch.getPitch(note.getKey() - 33));
            	}
            	
            }else {
            	CompatibilityUtils.playSound(p, p.getEyeLocation(),
                    Instrument.getInstrument(note.getInstrument()),
                    this.soundCategory,(l.getVolume() * (int) volume * (int) playerVolume) / 1000000f,
                    NotePitch.getPitch(note.getKey() - 33));
            }
        }
    }
}
