package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRangeStateChangeEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private SongPlayer song;
	private Player p;
	private Boolean state;

    public PlayerRangeStateChangeEvent(SongPlayer song, Player p, Boolean state) {
        this.song = song;
        this.p = p;
        this.state = state;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public SongPlayer getSongPlayer() {
        return song;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

	public Player getPlayer() {
		return p;
	}
	
	public Boolean isInRange() {
		return state;
	}
}
