package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntitySongPlayer extends RangeSongPlayer {

    private Entity entity;

    public EntitySongPlayer(Song song) {
        super(song);
    }

    public EntitySongPlayer(Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    public EntitySongPlayer(Playlist playlist) {
        super(playlist);
    }

    /**
     * Returns true if the Player is able to hear the current {@link EntitySongPlayer}
     * @param player in range
     * @return ability to hear the current {@link EntitySongPlayer}
     */
    @Override
    public boolean isInRange(Player player) {
        return player.getLocation().distance(entity.getLocation()) <= getDistance();
    }

    /**
     * Set entity associated with this {@link EntitySongPlayer}
     * @param entity
     */
    public void setEntity(Entity entity){
        this.entity = entity;
    }

    /**
     * Get {@link Entity} associated with this {@link EntitySongPlayer}
     * @return
     */
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void playTick(Player player, int tick) {
        if (entity.isDead()){
            if (autoDestroy){
                destroy();
            } else {
                setPlaying(false);
            }
        }
        if (!player.getWorld().getName().equals(entity.getWorld().getName())) {
            return; // not in same world
        }

        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) continue;

            float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00_00F)
                    * ((1F / 16F) * getDistance());

            channelMode.play(player, entity.getLocation(), song, layer, note, soundCategory, volume, !enable10Octave);

            if (isInRange(player)) {
                if (!playerList.get(player.getUniqueId())) {
                    playerList.put(player.getUniqueId(), true);
                    Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true));
                }
            } else {
                if (playerList.get(player.getUniqueId())) {
                    playerList.put(player.getUniqueId(), false);
                    Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false));
                }
            }
        }
    }
}
