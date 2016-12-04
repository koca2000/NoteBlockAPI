package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PositionSongPlayer extends SongPlayer {

    private Location targetLocation;
    private int distance = 16;

    public PositionSongPlayer(Song song) {
        super(song);
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public void playTick(Player p, int tick) {
        if (!p.getWorld().getName().equals(targetLocation.getWorld().getName())) {
            // not in same world
            return;
        }
        byte playerVolume = NoteBlockPlayerMain.getPlayerVolume(p);

        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }
            p.playSound(targetLocation,
                    Instrument.getInstrument(note.getInstrument()),
                    ((l.getVolume() * (int) volume * (int) playerVolume) / 1000000f) * ((1f/16f) * distance),
                    NotePitch.getPitch(note.getKey() - 33));
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
}
