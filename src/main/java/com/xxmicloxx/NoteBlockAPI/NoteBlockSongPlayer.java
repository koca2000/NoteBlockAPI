package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NoteBlockSongPlayer extends SongPlayer {
    private Block noteBlock;
    private int distance = 16;

    public NoteBlockSongPlayer(Song song) {
        super(song);
    }

    public Block getNoteBlock() {
        return noteBlock;
    }

    public void setNoteBlock(Block noteBlock) {
        this.noteBlock = noteBlock;
    }

    @Override
    public void playTick(Player p, int tick) {
        if (noteBlock.getType() != Material.NOTE_BLOCK) {
            return;
        }
        if (!p.getWorld().getName().equals(noteBlock.getWorld().getName())) {
            // not in same world
            return;
        }
        byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(p);

        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            p.playNote(noteBlock.getLocation(), Instrument.getBukkitInstrument(note.getInstrument()),
                    new org.bukkit.Note(note.getKey() - 33));
            
            if (Instrument.isCustomInstrument(note.getInstrument())){
            	if (song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound() != null){
            		p.playSound(noteBlock.getLocation(),
                            song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSound(),
                            ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                            NotePitch.getPitch(note.getKey() - 33));
            	}else {
            		p.playSound(noteBlock.getLocation(),
                            song.getCustomInstruments()[note.getInstrument() - Instrument.getCustomInstrumentFirstIndex()].getSoundfile(),
                            ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                            NotePitch.getPitch(note.getKey() - 33));
            	}
            	
            }else {
            	p.playSound(noteBlock.getLocation(),
                    Instrument.getInstrument(note.getInstrument()),
                    ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                    NotePitch.getPitch(note.getKey() - 33));
            }
            
            if (isPlayerInRange(p)){
            	if (!this.playerList.get(p.getName())){
            		playerList.put(p.getName(), true);
            		PlayerRangeStateChangeEvent event = new PlayerRangeStateChangeEvent(this, p, true);
            		Bukkit.getPluginManager().callEvent(event);
            	}
            } else {
            	if (this.playerList.get(p.getName())){
            		playerList.put(p.getName(), false);
            		PlayerRangeStateChangeEvent event = new PlayerRangeStateChangeEvent(this, p, false);
            		Bukkit.getPluginManager().callEvent(event);
            	}
            }
        }
    }
    
    /**
     * Sets distance in blocks where would be player able to hear sound. 
     * @param distance (Default 16 blocks)
     */
    public void setDistance(int distance){
    	this.distance = distance;
    }
    
    public int getDistance(){
    	return distance;
    }
    
    public boolean isPlayerInRange(Player p){
    	if (p.getLocation().distance(noteBlock.getLocation()) > distance){
    		return false;
    	} else {
    		return true;
    	}
    }
}
