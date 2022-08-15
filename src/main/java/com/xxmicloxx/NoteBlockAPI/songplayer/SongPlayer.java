package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.*;
import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.model.fade.IFade;
import com.xxmicloxx.NoteBlockAPI.model.fade.NoFade;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Plays a Song for a list of Players
 *
 */
public abstract class SongPlayer {
	protected NoteBlockAPI plugin;

	@Deprecated
	protected Song song;

	protected cz.koca2000.nbs4j.Song playingSong;
	protected Playlist playlist;
	protected int actualSong = 0;

	protected short tick = -1;
	protected double elapsedTimeInSeconds = 0;

	protected boolean playing = false;
	protected boolean fading = false;

	protected Map<UUID, Boolean> playerList = new ConcurrentHashMap<>();
	protected Map<cz.koca2000.nbs4j.Song, Boolean> songQueue = new ConcurrentHashMap<>(); //True if already played

	protected byte volume = 100;
	@Deprecated
	protected Fade fadeIn;
	@Deprecated
	protected Fade fadeOut;
	@Deprecated
	protected Fade fadeTemp = null;
	protected FadeInstance fadeInInstance;
	protected FadeInstance fadeOutInstance;
	protected FadeInstance fadeTempInstance;

	protected SoundCategory soundCategory = SoundCategory.MASTER;
	protected RepeatMode repeat = RepeatMode.NO;
	protected ChannelMode channelMode = new MonoMode();
	protected boolean random;
	protected boolean enable10Octave = false;
	protected boolean autoStop = false;

	private final Lock lock = new ReentrantLock();
	private final Random rng = new Random();
	private BukkitTask backgroundTask = null;

	private boolean wasStarted = false;

	@Deprecated
	public SongPlayer(@NotNull Song song) {
		this(new Playlist(song));
	}

	@Deprecated
	public SongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory) {
		this(new Playlist(song), soundCategory);
	}

	@Deprecated
	public SongPlayer(@NotNull Song song, @NotNull SoundCategory soundCategory, boolean random) {
		this(new Playlist(song), soundCategory, random);
	}

	public SongPlayer(@NotNull cz.koca2000.nbs4j.Song song) {
		this(new Playlist(song));
	}

	@Deprecated
	public SongPlayer(@NotNull cz.koca2000.nbs4j.Song song, @NotNull SoundCategory soundCategory) {
		this(new Playlist(song), soundCategory);
	}

	public SongPlayer(@NotNull Playlist playlist){
		this.playlist = playlist;
		plugin = NoteBlockAPI.getAPI();

		this.playingSong = playlist.getSong(actualSong);
		this.song = new Song(playingSong);

		fadeInInstance = new FadeInstance(NoFade.Instance);
		fadeInInstance.setInitialVolume((byte) 0);
		fadeInInstance.setTargetVolume((byte) 100);
		fadeIn = new Fade(fadeInInstance, song.getSpeed());

		fadeOutInstance = new FadeInstance(NoFade.Instance);
		fadeOutInstance.setInitialVolume((byte) 100);
		fadeOutInstance.setTargetVolume((byte) 0);
		fadeOut = new Fade(fadeOutInstance, song.getSpeed());
	}

	@Deprecated
	public SongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory){
		this(playlist, soundCategory, false);
	}

	@Deprecated
	public SongPlayer(@NotNull Playlist playlist, @NotNull SoundCategory soundCategory, boolean random){
		this(playlist);
		setSoundCategory(soundCategory);
		setRandom(random);
	}

	/**
	 * Check if 6 octave range is enabled
	 *
	 * @return true if enabled, false otherwise
	 */
	public boolean isEnable10Octave() {
		return enable10Octave;
	}

	/**
	 * Enable or disable 10 octave range
	 *
	 * If not enabled, notes will be transposed to 2 octave range
	 *
	 * @param enable10Octave true if enabled, false otherwise
	 */
	public void setEnable10Octave(boolean enable10Octave) {
		this.enable10Octave = enable10Octave;
	}

	/**
	 * Starts playback of this SongPlayer
	 */
	private void run() {
		if (backgroundTask != null && Bukkit.getScheduler().isCurrentlyRunning(backgroundTask.getTaskId()) && Bukkit.getScheduler().isQueued(backgroundTask.getTaskId()))
			return;

		backgroundTask = plugin.doAsync(() -> {
			while (playing || fading) {
				long startTime = System.currentTimeMillis();

				if (NoteBlockAPI.getAPI().isDisabling()){
					break;
				}

				lock.lock();
				try {
					float timeDelta = 1f / playingSong.getTempo(tick);
					if (fadeTemp != null){
						if (fadeTempInstance.isDone()) {
							fadeTemp = null;
							fading = false;
							if (!playing) {
								SongStoppedEvent event = new SongStoppedEvent(this);
								plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
								volume = fadeInInstance.getTargetVolume();
								continue;
							}

							if (elapsedTimeInSeconds <  fadeInInstance.getFade().getDurationInSeconds()) {
								fadeInInstance.setElapsedTime(fadeInInstance.getFade().getDurationInSeconds());
							}

						} else {
							volume = fadeTempInstance.calculateVolume(timeDelta);
						}
					} else if (elapsedTimeInSeconds < fadeInInstance.getFade().getDurationInSeconds()){
						volume = fadeInInstance.calculateVolume(timeDelta);
					} else if (elapsedTimeInSeconds >= playingSong.getSongLengthInSeconds() - fadeOutInstance.getFade().getDurationInSeconds()){
						volume = fadeOutInstance.calculateVolume(timeDelta);
					}

					elapsedTimeInSeconds += timeDelta;
					tick++;
					if (tick > playingSong.getSongLength()) {
						tick = -1;
						elapsedTimeInSeconds = 0;
						fadeInInstance.setElapsedTime(0);
						fadeOutInstance.setElapsedTime(0);
						volume = fadeInInstance.getTargetVolume();
						if (repeat == RepeatMode.ONE){
							SongLoopEvent event = new SongLoopEvent(this);
							plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));

							if (!event.isCancelled()) {
								continue;
							}
						} else {
							if (random) {
								songQueue.put(playingSong, true);
								checkPlaylistQueue();
								ArrayList<cz.koca2000.nbs4j.Song> left = new ArrayList<>();
								for (cz.koca2000.nbs4j.Song s : songQueue.keySet()) {
									if (!songQueue.get(s)) {
										left.add(s);
									}
								}

								if (left.size() == 0) {
									left.addAll(songQueue.keySet());
									songQueue.replaceAll((song, played) -> false);
									playingSong = left.get(rng.nextInt(left.size()));
									song = new Song(playingSong);
									actualSong = playlist.getIndex(playingSong);
									if (repeat == RepeatMode.ALL) {
										SongLoopEvent event = new SongLoopEvent(this);
										plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));

										if (!event.isCancelled()) {
											continue;
										}
									}
								} else {
									playingSong = left.get(rng.nextInt(left.size()));
									song = new Song(playingSong);
									actualSong = playlist.getIndex(playingSong);

									SongNextEvent event = new SongNextEvent(this);
									plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
									continue;
								}
							} else {
								if (playlist.hasNext(actualSong)) {
									actualSong++;
									playingSong = playlist.getSong(actualSong);
									song = new Song(playingSong);
									SongNextEvent event = new SongNextEvent(this);
									plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
									continue;
								} else {
									actualSong = 0;
									playingSong = playlist.getSong(actualSong);
									song = new Song(playingSong);
									if (repeat == RepeatMode.ALL) {
										SongLoopEvent event = new SongLoopEvent(this);
										plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));

										if (!event.isCancelled()) {
											continue;
										}
									}
								}
							}
						}
						playing = false;
						SongEndEvent event = new SongEndEvent(this);
						plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
						continue;
					}
					try {
						for (UUID uuid : playerList.keySet()) {
							Player player = Bukkit.getPlayer(uuid);
							if (player == null) {
								// offline...
								continue;
							}
							playTick(player, tick);
						}
					}
					catch (Exception e){
						Bukkit.getLogger().severe("An error occurred during the playback of song "
								+ (song != null ?
								"(author: " + playingSong.getMetadata().getAuthor() + ", title: " + playingSong.getMetadata().getTitle() + ")"
								: "null"));
						e.printStackTrace();
					}
				} catch (Exception e) {
					Bukkit.getLogger().severe("An error occurred during the playback of song "
							+ (song != null ?
									"(author: " + playingSong.getMetadata().getAuthor() + ", title: " + playingSong.getMetadata().getTitle() + ")"
									: "null"));
					e.printStackTrace();
				} finally {
					lock.unlock();
				}

				long duration = System.currentTimeMillis() - startTime;
				float delayMillis = (20 / playingSong.getTempo(tick)) * 50;
				if (duration < delayMillis) {
					try {
						Thread.sleep((long) (delayMillis - duration));
					} catch (InterruptedException e) {
						// do nothing
					}
				}
			}
		});
	}

	private void checkPlaylistQueue(){
		for (cz.koca2000.nbs4j.Song s : songQueue.keySet()){
			if (!playlist.contains(s)){
				songQueue.remove(s);
			}
		}

		for (cz.koca2000.nbs4j.Song s : playlist.getSongs()){
			if (!songQueue.containsKey(s)){
				songQueue.put(s, false);
			}
		}
	}
	
	/**
	 * Returns {@link Fade} for Fade in effect
	 * @deprecated Use {@link #getFadeInEffect()}
	 * @return Fade
	 */
	@NotNull
	@Deprecated
	public Fade getFadeIn(){
		return fadeIn;
	}

	/**
	 * Returns {@link Fade} for Fade out effect
	 * @deprecated Use {@link #getFadeOutEffect()}
	 * @return Fade
	 */
	@NotNull
	@Deprecated
	public Fade getFadeOut(){
		return fadeOut;
	}

	/**
	 * Returns {@link IFade} for Fade in effect
	 * @return IFade
	 */
	@NotNull
	public IFade getFadeInEffect(){
		return fadeInInstance.getFade();
	}

	/**
	 * Returns {@link IFade} for Fade out effect
	 * @return IFade
	 */
	@NotNull
	public IFade getFadeOutEffect(){
		return fadeOutInstance.getFade();
	}

	public void setFadeInEffect(IFade fadeIn) {
		fadeInInstance = new FadeInstance(fadeInInstance, fadeIn);
	}

	public void setFadeOutEffect(IFade fadeOut) {
		fadeOutInstance = new FadeInstance(fadeOutInstance, fadeOut);
	}

	/**
	 * Gets list of current Player UUIDs listening to this SongPlayer
	 * @return list of Player UUIDs
	 */
	@NotNull
	public Set<UUID> getPlayerUUIDs() {
		Set<UUID> uuids = new HashSet<>(playerList.keySet());
		return Collections.unmodifiableSet(uuids);
	}

	/**
	 * Adds a Player to the list of Players listening to this SongPlayer
	 * @param player {@link Player} to be added
	 */
	public void addPlayer(@NotNull Player player) {
		addPlayer(player.getUniqueId());
	}
	
	/**
	 * Adds a Player to the list of Players listening to this SongPlayer
	 * @param player's uuid
	 */
	public void addPlayer(@NotNull UUID player) {
		lock.lock();
		try {
			if (!playerList.containsKey(player)) {
				playerList.put(player, false);
				ArrayList<SongPlayer> songs = NoteBlockAPI.getSongPlayersByPlayer(player);
				if (songs == null) {
					songs = new ArrayList<>();
				}
				songs.add(this);
				NoteBlockAPI.setSongPlayersByPlayer(player, songs);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @deprecated SongPlayer is not being destroyed anymore. This method now does the same as {@link #getAutoStop}
	 * @return {@link #getAutoStop}
	 */
	public boolean getAutoDestroy() {
		return autoStop;
	}

	/**
	 * @deprecated SongPlayer is not being destroyed anymore. This method now does the same as {@link #setAutoStop}
	 * @param autoDestroy {@link #setAutoStop}
	 */
	public void setAutoDestroy(boolean autoDestroy) {
		setAutoStop(autoDestroy);
	}

	/**
	 * Returns whether the SongPlayer is going to stop playback when no one is listening
	 * @return if autoDestroy is enabled (default false)
	 */
	public boolean getAutoStop() {
		return autoStop;
	}

	/**
	 * Sets whether the SongPlayer is going to stop playback when no one is listening
	 * @param autoStop if autoStop is enabled
	 */
	public void setAutoStop(boolean autoStop) {
		lock.lock();
		try {
			this.autoStop = autoStop;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Plays the Song for the specific player
	 * @param player to play this SongPlayer for
	 * @param tick to play at
	 */
	public abstract void playTick(@NotNull Player player, int tick);

	/**
	 * SongPlayer will destroy itself
	 * @deprecated Stop playback and set tick to -1. It doesn't really destroy anything anymore.
	 */
	public void destroy() {
		SongDestroyingEvent event = new SongDestroyingEvent(this);
		plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
		if (event.isCancelled()) {
			return;
		}
		playing = false;
		setTick((short) -1);
	}

	/**
	 * Returns whether the SongPlayer is actively playing
	 * @return is playing
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Sets whether the SongPlayer is playing
	 * @param playing true to start the playback; false to stop the playback
	 */
	public void setPlaying(boolean playing) {
		setPlaying(playing, NoFade.Instance);
	}

	/**
	 * Sets whether the SongPlayer is playing and whether it should fade if previous value was different
	 * @param playing true to start the playback; false to stop the playback
	 * @param doFade whether the fade in or out effect should be used
	 */
	public void setPlaying(boolean playing, boolean doFade) {
		IFade fade = NoFade.Instance;
		if (doFade)
			fade = (playing ? fadeInInstance.getFade() : fadeOutInstance.getFade());
		setPlaying(playing, fade);
	}

	/**
	 * @deprecated Use {@link #setPlaying(boolean, IFade)}
	 */
	@Deprecated
	public void setPlaying(boolean playing, @Nullable Fade fade) {
		setPlaying(playing, fade != null ? fade.getFadeInstance().getFade() : NoFade.Instance);
	}

	/**
	 * Starts or stops the playback with the given fade effect
	 * @param playing true to start playback; false to stop playback
	 * @param fade fade effect to use
	 */
	public void setPlaying(boolean playing, @NotNull IFade fade) {
		if (this.playing == playing) return;

		lock.lock();
		try {
			this.playing = playing;

			if (!(fade instanceof NoFade)) {
				fadeTempInstance = new FadeInstance(fade);
				fadeTempInstance.setInitialVolume(playing ? 0 : volume);
				fadeTempInstance.setTargetVolume(playing ? volume : 0);
				fadeTemp = new Fade(fadeTempInstance, song.getSpeed());
				fading = true;
			} else {
				fading = false;
				fadeTemp = null;
				fadeTempInstance = null;
				volume = fadeInInstance.getTargetVolume();
				if (!playing) {
					SongStoppedEvent event = new SongStoppedEvent(this);
					plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
				}
			}

			if (playing) {
				if (wasStarted && random) {
					checkPlaylistQueue();
					actualSong = rng.nextInt(playlist.getCount());
				}
				wasStarted = true;

				run();
			}
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Gets the current tick of this SongPlayer
	 * @return current zero-based tick number
	 */
	public short getTick() {
		return tick;
	}

	/**
	 * Sets the current tick of this SongPlayer
	 * @param tick zero-based tick number
	 */
	public void setTick(short tick) {
		this.tick = tick;
		elapsedTimeInSeconds = playingSong.getTimeInSecondsAtTick(tick);
	}

	/**
	 * Removes a player from this SongPlayer
	 * @param player to remove
	 */
	public void removePlayer(@NotNull Player player) {
		removePlayer(player.getUniqueId());
	}
	
	/**
	 * Removes a player from this SongPlayer
	 * @param uuid of player to remove
	 */
	public void removePlayer(@NotNull UUID uuid) {
		lock.lock();
		try {
			playerList.remove(uuid);
			ArrayList<SongPlayer> playerSongPlayers = NoteBlockAPI.getSongPlayersByPlayer(uuid);
			if (playerSongPlayers != null) {
				playerSongPlayers = new ArrayList<>(playerSongPlayers);
				playerSongPlayers.remove(this);
				NoteBlockAPI.setSongPlayersByPlayer(uuid, playerSongPlayers);
			}
			if (playerList.isEmpty() && autoStop) {
				SongEndEvent event = new SongEndEvent(this);
				plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
				setPlaying(false);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gets the current volume of this SongPlayer
	 * @return volume (0-100)
	 */
	public byte getVolume() {
		return volume;
	}

	/**
	 * Sets the current volume of this SongPlayer
	 * @param volume (0-100)
	 */
	public void setVolume(byte volume) {
		if (volume > 100){
			volume = 100;
		} else if (volume < 0){
			volume = 0;
		}
		this.volume = volume;
		
		fadeInInstance.setTargetVolume(volume);
		fadeOutInstance.setInitialVolume(volume);
		if (fadeTempInstance != null) {
			if (playing) fadeTempInstance.setTargetVolume(volume);
			else fadeTempInstance.setInitialVolume(volume);
		}
	}

	/**
	 * Gets the Song being played by this SongPlayer
	 * @deprecated Use {@link #getPlayingSong()}
	 * @return {@link Song}
	 */
	@NotNull
	@Deprecated
	public Song getSong() {
		return new Song(song);
	}

	/**
	 * Gets the Song being played
	 * @return {@link cz.koca2000.nbs4j.Song}
	 */
	@NotNull
	public cz.koca2000.nbs4j.Song getPlayingSong(){
		return playingSong;
	}
	
	/**
	 * Gets the Playlist being played by this SongPlayer
	 * @return {@link Playlist}
	 */
	@NotNull
	public Playlist getPlaylist() {
		return playlist;
	}
	
	/**
	 * Sets the Playlist being played by this SongPlayer. Will affect next Song
	 */
	public void setPlaylist(@NotNull Playlist playlist) {
		this.playlist = playlist;
	}
	
	/**
	 * Get index of actually played {@link cz.koca2000.nbs4j.Song} in {@link Playlist}
	 * @return index of the song in playlist
	 */
	public int getPlayedSongIndex(){
		return actualSong;
	}
	
	/**
	 * Start playing {@link cz.koca2000.nbs4j.Song} at specified index in {@link Playlist}
	 * If there is no {@link cz.koca2000.nbs4j.Song} at this index, {@link SongPlayer} will continue playing current song
	 * @param index index of the song in playlist
	 */
	public void playSong(int index){
		lock.lock();
		try {
			if (playlist.exist(index)){
				playingSong = playlist.getSong(index);
				song = new Song(playingSong);
				actualSong = index;
				tick = -1;
				elapsedTimeInSeconds = 0;
				fadeInInstance.setElapsedTime(0);
				fadeIn.setTempo(song.getSpeed());
				fadeOutInstance.setElapsedTime(0);
				fadeOut.setTempo(song.getSpeed());
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Start playing {@link cz.koca2000.nbs4j.Song} that is next in {@link Playlist} or random {@link cz.koca2000.nbs4j.Song} from {@link Playlist}
	 */
	public void playNextSong(){
		lock.lock();
		try {
			tick = (short) playingSong.getSongLength();
			elapsedTimeInSeconds = playingSong.getSongLengthInSeconds();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gets the SoundCategory of this SongPlayer
	 * @see SoundCategory
	 * @return SoundCategory of this SongPlayer
	 */
	@NotNull
	public SoundCategory getCategory() {
		return soundCategory;
	}

	/**
	 * Sets the SoundCategory for this SongPlayer
	 * @param soundCategory {@link SoundCategory}
	 * @deprecated Use {@link #setSoundCategory(SoundCategory)}
	 */
	@Deprecated
	public void setCategory(@NotNull SoundCategory soundCategory) {
		this.soundCategory = soundCategory;
	}

	/**
	 * Sets the SoundCategory for this SongPlayer. This decides under which volume settings the sound will be in client.
	 * @param soundCategory {@link SoundCategory}
	 */
	public void setSoundCategory(@NotNull SoundCategory soundCategory) {
		this.soundCategory = soundCategory;
	}

	/**
	 * Sets SongPlayer's {@link RepeatMode}
	 * @param repeatMode one of {@link RepeatMode} values
	 */
	public void setRepeatMode(@NotNull RepeatMode repeatMode){
		this.repeat = repeatMode;
	}

	/**
	 * Gets SongPlayer's {@link RepeatMode}
	 * @return {@link RepeatMode}
	 */
	@NotNull
	public RepeatMode getRepeatMode(){
		return repeat;
	}

	/**
	 * Sets whether the SongPlayer will choose next song from player randomly
	 * @param random true to choose next song randomly; otherwise, false
	 */
	public void setRandom(boolean random){
		this.random = random;
	}

	/**
	 * Gets whether the SongPlayer will choose next song from player randomly
	 * @return true if random; otherwise, false
	 */
	public boolean isRandom(){
		return random;
	}

	@NotNull
	public ChannelMode getChannelMode(){
		return channelMode;
	}

}
