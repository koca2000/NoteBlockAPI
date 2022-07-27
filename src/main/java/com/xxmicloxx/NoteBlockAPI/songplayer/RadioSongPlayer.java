package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * SongPlayer playing to everyone added to it no matter where he is
 *
 */
public class RadioSongPlayer extends SongPlayer {

	@Deprecated
	public RadioSongPlayer(@NotNull Song song) {
		super(song);
	}

	@Deprecated
	public RadioSongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory) {
		super(song, soundCategory);
	}

	@Deprecated
	public RadioSongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory) {
		super(playlist, soundCategory);
	}

	public RadioSongPlayer(@NotNull cz.koca2000.nbs4j.Song song) {
		super(song);
	}

	public RadioSongPlayer(@NotNull Playlist playlist) {
		super(playlist);
	}

	@Override
	public void playTick(@NotNull Player player, int tick) {
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		for (int i = 0; i < playingSong.getLayersCount(); i++) {
			cz.koca2000.nbs4j.Layer layer = playingSong.getLayer(i);
			cz.koca2000.nbs4j.Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}

			float volume = (layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVolume()) / 100_00_00_00F;

			channelMode.play(player, player.getEyeLocation(), playingSong, layer, note, soundCategory, volume, !enable10Octave);
		}
	}

	/**
	 * Sets how will be {@link cz.koca2000.nbs4j.Note} played to {@link Player} (eg. mono or stereo). Default is {@link MonoMode}.
	 * @param mode
	 */
	public void setChannelMode(@NotNull ChannelMode mode){
	    channelMode = mode;
    }
}
