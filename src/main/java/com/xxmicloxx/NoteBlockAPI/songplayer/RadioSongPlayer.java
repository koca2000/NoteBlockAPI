package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;

/**
 * SongPlayer playing to everyone added to it no matter where he is
 *
 */
public class RadioSongPlayer extends SongPlayer {
	
	//protected boolean stereo = true;
	
	public RadioSongPlayer(Playable playable) {
		super(playable);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	public RadioSongPlayer(Playable playable, SoundCategory soundCategory) {
		super(playable, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	private RadioSongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
		super(songPlayer);
	}

	public RadioSongPlayer(Playlist playlist, SoundCategory soundCategory) {
		super(playlist, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	public RadioSongPlayer(Playlist playlist) {
		super(playlist);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	@Override
	public void playTick(Player player, long tick) {
		if (!(currentPlaying instanceof Song))
			throw new IllegalStateException("Unexpected call to playTick");
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		for (Layer layer : ((Song) currentPlaying).getLayerHashMap().values()) {
			Note note = layer.getNote((int) tick);
			if (note == null) {
				continue;
			}

			float volume = (layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00_00F;

			channelMode.play(player, player.getEyeLocation(), (Song) currentPlaying, layer, note, soundCategory, volume, !enable10Octave);
		}
	}

	@Override
	public void playNote(Player player, Note note) {
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		if (note == null) {
			return;
		}

		float volume = ((int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00F;

		channelMode.play(player, player.getEyeLocation(), note, soundCategory, volume, !enable10Octave);
	}

	/**
	 * Returns if the SongPlayer will play Notes from two sources as stereo
	 * @return if is played stereo
     * @deprecated
	 */
	@Deprecated
	public boolean isStereo(){
		return !(channelMode instanceof MonoMode);
	}
	
	/**
	 * Sets if the SongPlayer will play Notes from two sources as stereo
	 * @param stereo
     * @deprecated
	 */
	@Deprecated
	public void setStereo(boolean stereo){
		channelMode = stereo ? new MonoMode() : new MonoStereoMode();
	}

	/**
	 * Sets how will be {@link Note} played to {@link Player} (eg. mono or stereo). Default is {@link MonoMode}.
	 * @param mode
	 */
	public void setChannelMode(ChannelMode mode){
	    channelMode = mode;
    }
}
