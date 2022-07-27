package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntitySongPlayer extends RangeSongPlayer {

    private Entity entity;

    @Deprecated
    public EntitySongPlayer(@NotNull Song song) {
        super(song);
    }

    @Deprecated
    public EntitySongPlayer(@NotNull Song song, SoundCategory soundCategory) {
        super(song, soundCategory);
    }

    @Deprecated
    public EntitySongPlayer(@NotNull Playlist playlist, SoundCategory soundCategory) {
        super(playlist, soundCategory);
    }

    @Deprecated
    public EntitySongPlayer(@NotNull Playlist playlist) {
        super(playlist);
    }

    public EntitySongPlayer(@NotNull cz.koca2000.nbs4j.Song song, @NotNull Entity entity) {
        super(song);
        this.entity = entity;
    }

    public EntitySongPlayer(@NotNull Playlist playlist, @NotNull Entity entity) {
        super(playlist);
        this.entity = entity;
    }

    /**
     * Returns true if the Player is able to hear the current {@link EntitySongPlayer}
     * @param player in range
     * @return ability to hear the current {@link EntitySongPlayer}
     */
    @Override
    public boolean isInRange(@NotNull Player player) {
        return player.getLocation().distance(entity.getLocation()) <= getDistance();
    }

    /**
     * Set entity associated with this {@link EntitySongPlayer}
     * @param entity
     */
    public void setEntity(@NotNull Entity entity){
        this.entity = entity;
    }

    /**
     * Get {@link Entity} associated with this {@link EntitySongPlayer}
     * @return
     */
    @NotNull
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void playTick(@NotNull Player player, int tick) {
        if (entity.isDead()){
            setPlaying(false);
        }
        if (!player.getWorld().getName().equals(entity.getWorld().getName())) {
            return; // not in same world
        }

        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (int i = 0; i < playingSong.getLayersCount(); i++) {
            cz.koca2000.nbs4j.Layer layer = playingSong.getLayer(i);
            cz.koca2000.nbs4j.Note note = layer.getNote(tick);
            if (note == null) continue;

            float volume = ((layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVolume()) / 100_00_00_00F)
                    * ((1F / 16F) * getDistance());

            channelMode.play(player, entity.getLocation(), playingSong, layer, note, soundCategory, volume, !enable10Octave);

            if (isInRange(player)) {
                if (!playerList.get(player.getUniqueId())) {
                    playerList.put(player.getUniqueId(), true);
                    plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, true)));
                }
            } else {
                if (playerList.get(player.getUniqueId())) {
                    playerList.put(player.getUniqueId(), false);
                    plugin.doSync(() -> Bukkit.getPluginManager().callEvent(new PlayerRangeStateChangeEvent(this, player, false)));
                }
            }
        }
    }
}
