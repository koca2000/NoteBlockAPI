package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.*;
import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import com.xxmicloxx.NoteBlockAPI.utils.MidiInstruments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.sound.midi.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Plays a Song for a list of Players
 */
public abstract class SongPlayer {

	protected Playable currentPlaying;
	protected Playlist playlist;
	protected int actualSong = 0;

	protected boolean playing = false;
	protected boolean fading = false;
	protected long tick = -1;
	protected Map<UUID, Boolean> playerList = new ConcurrentHashMap<UUID, Boolean>();

	protected boolean autoDestroy = false;
	protected boolean destroyed = false;

	protected byte volume = 100;
	protected Fade fadeIn;
	protected Fade fadeOut;
	protected Fade fadeTemp = null;
	protected RepeatMode repeat = RepeatMode.NO;
	protected boolean random = false;

	protected Map<Playable, Boolean> songQueue = new ConcurrentHashMap<Playable, Boolean>(); //True if already played

	private final Lock lock = new ReentrantLock();
	private final Random rng = new Random();

	protected NoteBlockAPI plugin;

	protected SoundCategory soundCategory;
	protected ChannelMode channelMode = new MonoMode();
	protected boolean enable10Octave = true;

	com.xxmicloxx.NoteBlockAPI.SongPlayer oldSongPlayer;

	public SongPlayer(Playable playable) {
		this(new Playlist(playable), SoundCategory.MASTER);
	}

	public SongPlayer(Playable playable, SoundCategory soundCategory) {
		this(new Playlist(playable), soundCategory);
	}

	public SongPlayer(Playable playable, SoundCategory soundCategory, boolean random) {
		this(new Playlist(playable), soundCategory, random);
	}

	public SongPlayer(Playlist playlist) {
		this(playlist, SoundCategory.MASTER);
	}

	public SongPlayer(Playlist playlist, SoundCategory soundCategory) {
		this(playlist, soundCategory, false);
	}

	public SongPlayer(Playlist playlist, SoundCategory soundCategory, boolean random) {
		this.playlist = playlist;
		this.random = random;
		this.soundCategory = soundCategory;
		plugin = NoteBlockAPI.getAPI();

		fadeIn = new Fade(FadeType.NONE, 60);
		fadeIn.setFadeStart((byte) 0);
		fadeIn.setFadeTarget(volume);

		fadeOut = new Fade(FadeType.NONE, 60);
		fadeOut.setFadeStart(volume);
		fadeOut.setFadeTarget((byte) 0);

		if (random) {
			checkPlaylistQueue();
			actualSong = rng.nextInt(playlist.getCount());
		}
		this.currentPlaying = playlist.get(actualSong);

		start();
	}

	/**
	 * @param songPlayer
	 * @deprecated
	 */
	SongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
		oldSongPlayer = songPlayer;
		com.xxmicloxx.NoteBlockAPI.Song s = songPlayer.getSong();
		HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
		for (Integer i : s.getLayerHashMap().keySet()) {
			com.xxmicloxx.NoteBlockAPI.Layer l = s.getLayerHashMap().get(i);
			HashMap<Integer, Note> noteHashMap = new HashMap<Integer, Note>();
			for (Integer iL : l.getHashMap().keySet()) {
				com.xxmicloxx.NoteBlockAPI.Note note = l.getHashMap().get(iL);
				noteHashMap.put(iL, new Note(note.getInstrument(), note.getKey()));
			}
			Layer layer = new Layer();
			layer.setNotesAtTicks(noteHashMap);
			layer.setVolume(l.getVolume());
			layerHashMap.put(i, layer);
		}
		CustomInstrument[] instruments = new CustomInstrument[s.getCustomInstruments().length];
		for (int i = 0; i < s.getCustomInstruments().length; i++) {
			com.xxmicloxx.NoteBlockAPI.CustomInstrument ci = s.getCustomInstruments()[i];
			instruments[i] = new CustomInstrument(ci.getIndex(), ci.getName(), ci.getSoundfile());
		}
		currentPlaying = new Song(s.getSpeed(), layerHashMap, s.getSongHeight(), s.getLength(), s.getTitle(), s.getAuthor(), s.getDescription(), s.getPath(), instruments);
		playlist = new Playlist(currentPlaying);

		fadeIn = new Fade(FadeType.NONE, 60);
		fadeIn.setFadeStart((byte) 0);
		fadeIn.setFadeTarget(volume);

		fadeOut = new Fade(FadeType.NONE, 60);
		fadeOut.setFadeStart(volume);
		fadeOut.setFadeTarget((byte) 0);

		plugin = NoteBlockAPI.getAPI();
	}

	void update(String key, Object value){
		switch (key){
			case "playing":
				playing = (boolean) value;
				break;
			case "fadeType":
				fadeIn.setType(FadeType.valueOf(((String) value).replace("FADE_", "")));
				break;
			case "fadeTarget":
				fadeIn.setFadeTarget((byte) value);
				break;
			case "fadeStart":
				fadeIn.setFadeStart((byte) value);
				break;
			case "fadeDuration":
				fadeIn.setFadeDuration((int) value);
				break;
			case "fadeDone":
				fadeIn.setFadeDone((int) value);
				break;
			case "tick":
				tick = (short) value;
				break;
			case "addplayer":
				addPlayer(((Player) value).getUniqueId(), false);
				break;
			case "removeplayer":
				removePlayer(((Player) value).getUniqueId(), false);
				break;
			case "autoDestroy":
				autoDestroy = (boolean) value;
				break;
			case "volume":
				volume = (byte) value;
				break;
			case "soundCategory":
				soundCategory = SoundCategory.valueOf((String) value);
				break;
				
		}
	}
	
	/**
	 * Gets the FadeType for this SongPlayer (unused)
	 * @return FadeType
	 * @deprecated returns fadeIn value
	 */
	@Deprecated
	public FadeType getFadeType() {
		return fadeIn.getType();
	}

	/**
	 * Sets the FadeType for this SongPlayer
	 * @param fadeType
	 * @deprecated set fadeIn value
	 */
	@Deprecated
	public void setFadeType(FadeType fadeType) {
		fadeIn.setType(fadeType);
		CallUpdate("fadetype", "FADE_" + fadeType.name());
	}

	/**
	 * Target volume for fade
	 * @return byte representing fade target
	 * @deprecated returns fadeIn value
	 */
	@Deprecated
	public byte getFadeTarget() {
		return fadeIn.getFadeTarget();
	}

	/**
	 * Set target volume for fade
	 * @param fadeTarget
	 * @deprecated set fadeIn value
	 */
	@Deprecated
	public void setFadeTarget(byte fadeTarget) {
		fadeIn.setFadeTarget(fadeTarget);
		CallUpdate("fadeTarget", fadeTarget);
	}

	/**
	 * Gets the starting volume for the fade
	 * @return
	 * @deprecated returns fadeIn value
	 */
	@Deprecated
	public byte getFadeStart() {
		return fadeIn.getFadeStart();
	}

	/**
	 * Sets the starting volume for the fade
	 * @param fadeStart
	 * @deprecated set fadeIn value
	 */
	@Deprecated
	public void setFadeStart(byte fadeStart) {
		fadeIn.setFadeStart(fadeStart);
		CallUpdate("fadeStart", fadeStart);
	}

	/**
	 * Gets the duration of the fade
	 * @return duration of the fade
	 * @deprecated returns fadeIn value
	 */
	@Deprecated
	public int getFadeDuration() {
		return fadeIn.getFadeDuration();
	}

	/**
	 * Sets the duration of the fade
	 * @param fadeDuration
	 * @deprecated set fadeIn value
	 */
	@Deprecated
	public void setFadeDuration(int fadeDuration) {
		fadeIn.setFadeDuration(fadeDuration);
		CallUpdate("fadeDuration", fadeDuration);
	}

	/**
	 * Gets the tick when fade will be finished
	 * @return tick
	 * @deprecated returns fadeIn value
	 */
	@Deprecated
	public int getFadeDone() {
		return fadeIn.getFadeDone();
	}

	/**
	 * Sets the tick when fade will be finished
	 *
	 * @param fadeDone
	 * @deprecated set fadeIn value
	 */
	@Deprecated
	public void setFadeDone(int fadeDone) {
		fadeIn.setFadeDone(fadeDone);
		CallUpdate("fadeDone", fadeDone);
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
	 * Enable or disable 6 octave range
	 * <p>
	 * If not enabled, notes will be transposed to 2 octave range
	 *
	 * @param enable10Octave true if enabled, false otherwise
	 */
	public void setEnable10Octave(boolean enable10Octave) {
		this.enable10Octave = enable10Octave;
	}

	/**
	 * Starts this SongPlayer
	 */
	private void start() {
		plugin.doAsync(() -> {
			Sequencer lastSequencer = null;
			while (!destroyed) {
				long startTime = System.currentTimeMillis();
				if(currentPlaying instanceof MidiSequence && lastSequencer != null) {
					while (!(destroyed || NoteBlockAPI.getAPI().isDisabling()) && lastSequencer.isRunning()) {
						try {
							//noinspection BusyWait
							Thread.sleep(50);
							tick = lastSequencer.getTickPosition();
						} catch (InterruptedException e) {
							break;
						}
					}
					tick = currentPlaying.getLengthInTicks() + 1;
					lastSequencer.close();
					lastSequencer = null;
				}
				lock.lock();
				try {
					if (destroyed || NoteBlockAPI.getAPI().isDisabling()){
						break;
					}

					if (playing || fading) {
						if (fadeTemp != null) {
							if (fadeTemp.isDone()) {
								fadeTemp = null;
								fading = false;
								if (!playing) {
									SongStoppedEvent event = new SongStoppedEvent(this);
									plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
									volume = fadeIn.getFadeTarget();
									continue;
								}
							} else {
								int fade = fadeTemp.calculateFade();
								if (fade != -1) {
									volume = (byte) fade;
								}
							}
						} else if (tick < fadeIn.getFadeDuration()) {
							int fade = fadeIn.calculateFade();
							if (fade != -1) {
								volume = (byte) fade;
							}
							CallUpdate("fadeDone", fadeIn.getFadeDone());
						} else if (tick >= currentPlaying.getLengthInTicks() - fadeOut.getFadeDuration()) {
							int fade = fadeOut.calculateFade();
							if (fade != -1) {
								volume = (byte) fade;
							}
						}

						tick++;
						if (tick > currentPlaying.getLengthInTicks()) {
							tick = -1;
							fadeIn.setFadeDone(0);
							CallUpdate("fadeDone", fadeIn.getFadeDone());
							fadeOut.setFadeDone(0);
							volume = fadeIn.getFadeTarget();
							if (repeat == RepeatMode.ONE) {
								SongLoopEvent event = new SongLoopEvent(this);
								plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));

								if (!event.isCancelled()) {
									continue;
								}
							} else {
								if (random) {
									songQueue.put(currentPlaying, true);
									checkPlaylistQueue();
									ArrayList<Playable> left = new ArrayList<>();
									for (Playable s : songQueue.keySet()) {
										if (!songQueue.get(s)) {
											left.add(s);
										}
									}

									if (left.size() == 0) {
										left.addAll(songQueue.keySet());
										for (Playable s : songQueue.keySet()) {
											songQueue.put(s, false);
										}
										currentPlaying = left.get(rng.nextInt(left.size()));
										actualSong = playlist.getIndex(currentPlaying);
										if (currentPlaying instanceof Song)
											CallUpdate("song", currentPlaying);
										if (repeat == RepeatMode.ALL) {
											SongLoopEvent event = new SongLoopEvent(this);
											plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));

											if (!event.isCancelled()) {
												continue;
											}
										}
									} else {
										currentPlaying = left.get(rng.nextInt(left.size()));
										actualSong = playlist.getIndex(currentPlaying);

										if (currentPlaying instanceof Song)
											CallUpdate("song", currentPlaying);
										SongNextEvent event = new SongNextEvent(this);
										plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
										continue;
									}
								} else {
									if (playlist.hasNext(actualSong)) {
										actualSong++;
										currentPlaying = playlist.get(actualSong);
										if (currentPlaying instanceof Song)
											CallUpdate("song", currentPlaying);
										SongNextEvent event = new SongNextEvent(this);
										plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
										continue;
									} else {
										actualSong = 0;
										currentPlaying = playlist.get(actualSong);
										if (currentPlaying instanceof Song)
											CallUpdate("song", currentPlaying);
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
							if (autoDestroy) {
								destroy();
							}
							continue;
						}
						CallUpdate("tick", tick);

						if (currentPlaying instanceof Song)
							for (UUID uuid : playerList.keySet()) {
								Player player = Bukkit.getPlayer(uuid);
								if (player == null) {
									// offline...
									continue;
								}
								playTick(player, tick);
							}
						else if (currentPlaying instanceof MidiSequence) {
							final Sequencer sequencer = MidiSystem.getSequencer(false);
							lastSequencer = sequencer;
							sequencer.getTransmitter().setReceiver(new Receiver() {

								private final MidiInstruments.MidiInstrument[] channelPrograms = new MidiInstruments.MidiInstrument[16];
								private final short[] channelPitchBends = new short[16];
								private final byte[][] channelPolyPressures = new byte[16][128];
								private final byte[] channelPressures = new byte[16];

								{
									reset();
								}

								private void reset() {
									Arrays.fill(channelPrograms, MidiInstruments.instrumentMapping.get(0));
									Arrays.fill(channelPitchBends, (short) 0);
									for (byte[] bytes : channelPolyPressures) {
										Arrays.fill(bytes, (byte) 127);
									}
									Arrays.fill(channelPressures, (byte) 127);
								}

								@Override
								public void send(MidiMessage midiMessage, long l) {
									try {
										if (midiMessage instanceof ShortMessage) {
											ShortMessage shortMessage = (ShortMessage) midiMessage.clone();
											if (shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2() == 0) {
												try {
													shortMessage.setMessage(ShortMessage.NOTE_OFF, shortMessage.getData1(), 64);
												} catch (InvalidMidiDataException e) {
													e.printStackTrace();
												}
											}
											switch (shortMessage.getCommand()) {
												case ShortMessage.NOTE_ON:
													final Note note;
													if (shortMessage.getChannel() != 9) {
														if (channelPrograms[shortMessage.getChannel()] == null) break;
														note = new Note(
																(byte) channelPrograms[shortMessage.getChannel()].mcInstrument,
																(short) (shortMessage.getData1() + (channelPrograms[shortMessage.getChannel()].octaveModifier * 12)),
																(byte) ((shortMessage.getData2() / 127.0 * 100) * (channelPolyPressures[shortMessage.getChannel()][shortMessage.getData1()] / 127.0 * 100) * (channelPressures[shortMessage.getChannel()] / 127.0 * 100) / 1_00_00),
																100,
																(short) (channelPitchBends[shortMessage.getChannel()] / 4096.0 * 100));
													} else {
														final MidiInstruments.MidiPercussion percussion = MidiInstruments.percussionMapping.get(shortMessage.getData1());
														if (percussion == null) break;
														note = new Note(
																(byte) percussion.mcInstrument,
																(short) percussion.midiKey,
																(byte) ((shortMessage.getData2() / 127.0 * 100) * (channelPolyPressures[shortMessage.getChannel()][shortMessage.getData1()] / 127.0 * 100) * (channelPressures[shortMessage.getChannel()] / 127.0 * 100) / 1_00_00),
																100,
																(short) 0);
													}
													for (UUID uuid : playerList.keySet()) {
														Player player = Bukkit.getPlayer(uuid);
														if (player == null) {
															// offline...
															continue;
														}
														playNote(player,
																note);
													}
													break;
												case ShortMessage.PROGRAM_CHANGE:
													channelPrograms[shortMessage.getChannel()] = MidiInstruments.instrumentMapping.get(shortMessage.getData1());
													break;
												case ShortMessage.PITCH_BEND:
													channelPitchBends[shortMessage.getChannel()] = (short) ((shortMessage.getData1() + shortMessage.getData2() * 128) - 8192);
													break;
												case ShortMessage.POLY_PRESSURE:
													channelPolyPressures[shortMessage.getChannel()][shortMessage.getData1()] = (byte) shortMessage.getData2();
													break;
												case ShortMessage.CHANNEL_PRESSURE:
													channelPressures[shortMessage.getChannel()] = (byte) shortMessage.getData1();
													break;
												case ShortMessage.SYSTEM_RESET:
													reset();
													break;
											}

										} else System.err.println("Invalid message: " + midiMessage);
									} catch (Throwable t) {
										t.printStackTrace();
									}
								}

								@Override
								public void close() {

								}
							});
							final Sequence sequence = ((MidiSequence) currentPlaying).sequence;
							sequencer.open();
							sequencer.setSequence(sequence);
							sequencer.start();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}

				if (destroyed) {
					break;
				}

				long duration = System.currentTimeMillis() - startTime;
				double delayMillis = currentPlaying instanceof Song ? ((Song) currentPlaying).getDelay() * 50 : 0;
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

	private void checkPlaylistQueue() {
		for (Playable s : songQueue.keySet()) {
			if (!playlist.contains(s)) {
				songQueue.remove(s);
			}
		}

		for (Playable s : playlist.getSongList()) {
			if (!songQueue.containsKey(s)) {
				songQueue.put(s, false);
			}
		}
	}

	/**
	 * Returns {@link Fade} for Fade in effect
	 * @return Fade
	 */
	public Fade getFadeIn(){
		return fadeIn;
	}
	
	/**
	 * Returns {@link Fade} for Fade out effect
	 * @return Fade
	 */
	public Fade getFadeOut(){
		return fadeOut;
	}
	
	/**
	 * Gets list of current Player UUIDs listening to this SongPlayer
	 * @return list of Player UUIDs
	 */
	public Set<UUID> getPlayerUUIDs() {
		Set<UUID> uuids = new HashSet<>();
		uuids.addAll(playerList.keySet());
		return Collections.unmodifiableSet(uuids);
	}

	/**
	 * Adds a Player to the list of Players listening to this SongPlayer
	 * @param player
	 */
	public void addPlayer(Player player) {
		addPlayer(player.getUniqueId());
	}
	
	/**
	 * Adds a Player to the list of Players listening to this SongPlayer
	 * @param player's uuid
	 */
	public void addPlayer(UUID player) {
		addPlayer(player, true);
	}
	
	private void addPlayer(UUID player, boolean notify){
		lock.lock();
		try {
			if (!playerList.containsKey(player)) {
				playerList.put(player, false);
				ArrayList<SongPlayer> songs = NoteBlockAPI.getSongPlayersByPlayer(player);
				if (songs == null) {
					songs = new ArrayList<SongPlayer>();
				}
				songs.add(this);
				NoteBlockAPI.setSongPlayersByPlayer(player, songs);
				if (notify){
					Player p = Bukkit.getPlayer(player);
					if (p != null){
						CallUpdate("addplayer", p);
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns whether the SongPlayer is set to destroy itself when no one is listening 
	 * or when the Song ends
	 * @return if autoDestroy is enabled
	 */
	public boolean getAutoDestroy() {
		lock.lock();
		try {
			return autoDestroy;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Sets whether the SongPlayer is going to destroy itself when no one is listening 
	 * or when the Song ends
	 * @param autoDestroy if autoDestroy is enabled
	 */
	public void setAutoDestroy(boolean autoDestroy) {
		lock.lock();
		try {
			this.autoDestroy = autoDestroy;
			CallUpdate("autoDestroy", autoDestroy);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Plays the Song for the specific player
	 *  @param player to play this SongPlayer for
	 * @param tick   to play at
	 */
	public abstract void playTick(Player player, long tick);

	public abstract void playNote(Player player, Note note);

	/**
	 * SongPlayer will destroy itself
	 */
	public void destroy() {
		lock.lock();
		try {
			SongDestroyingEvent event = new SongDestroyingEvent(this);
			plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
			//Bukkit.getScheduler().cancelTask(threadId);
			if (event.isCancelled()) {
				return;
			}
			destroyed = true;
			playing = false;
			setTick((short) -1);
			CallUpdate("destroyed", destroyed);
			CallUpdate("playing", playing);
		} finally {
			lock.unlock();
		}
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
	 * @param playing
	 */
	public void setPlaying(boolean playing) {
		setPlaying(playing, null);
	}

	/**
	 * Sets whether the SongPlayer is playing and whether it should fade if previous value was different
	 * @param playing
	 * @param fade
	 */
	public void setPlaying(boolean playing, boolean fade) {
		setPlaying(playing, fade ? (playing ? fadeIn : fadeOut) : null);
	}

	public void setPlaying(boolean playing, Fade fade) {
		if (this.playing == playing) return;

		this.playing = playing;
		if (fade != null && fade.getType() != FadeType.NONE) {
			fadeTemp = new Fade(fade.getType(), fade.getFadeDuration());
			fadeTemp.setFadeStart(playing ? 0 : volume);
			fadeTemp.setFadeTarget(playing ? volume : 0);
			fading = true;
		} else {
			fading = false;
			fadeTemp = null;
			volume = fadeIn.getFadeTarget();
			SongStoppedEvent event = new SongStoppedEvent(this);
			plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
		}

		CallUpdate("playing", playing);
	}

	/**
	 * Gets the current tick of this SongPlayer
	 * @return
	 */
	public long getTick() {
		return tick;
	}

	/**
	 * Sets the current tick of this SongPlayer
	 * @param tick
	 */
	public void setTick(short tick) {
		this.tick = tick;
		CallUpdate("tick", tick);
	}

	/**
	 * Removes a player from this SongPlayer
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		removePlayer(player.getUniqueId());
	}
	
	/**
	 * Removes a player from this SongPlayer
	 * @param uuid of player to remove
	 */
	public void removePlayer(UUID uuid) {
		removePlayer(uuid, true);
	}
	
	private void removePlayer(UUID player, boolean notify) {
		lock.lock();
		try {
			if (notify){
				Player p = Bukkit.getPlayer(player);
				if (p != null){
					CallUpdate("removeplayer", p);
				}
			}
			playerList.remove(player);
			if (NoteBlockAPI.getSongPlayersByPlayer(player) == null) {
				return;
			}
			ArrayList<SongPlayer> songs = new ArrayList<>(
					NoteBlockAPI.getSongPlayersByPlayer(player));
			songs.remove(this);
			NoteBlockAPI.setSongPlayersByPlayer(player, songs);
			if (playerList.isEmpty() && autoDestroy) {
				SongEndEvent event = new SongEndEvent(this);
				plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
				destroy();
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
		
		fadeIn.setFadeTarget(volume);
		fadeOut.setFadeStart(volume);
		if (fadeTemp != null) {
			if (playing) fadeTemp.setFadeTarget(volume);
			else fadeTemp.setFadeStart(volume);
		}

		CallUpdate("volume", volume);
	}

	/**
	 * Gets the Song being played by this SongPlayer
	 *
	 * @return
	 * @deprecated see {@link SongPlayer#getPlaying()}
	 */
	@Deprecated
	public Song getSong() {
		return currentPlaying instanceof Song ? (Song) currentPlaying : null;
	}

	/**
	 * Gets the Playable being played by this SongPlayer
	 *
	 * @return the playable
	 */
	public Playable getPlaying() {
		return currentPlaying;
	}

	/**
	 * Gets the Playlist being played by this SongPlayer
	 *
	 * @return
	 */
	public Playlist getPlaylist() {
		return playlist;
	}

	/**
	 * Sets the Playlist being played by this SongPlayer. Will affect next Song
	 */
	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	/**
	 * Get index of actually played {@link Song} in {@link Playlist}
	 * @return
	 */
	public int getPlayedSongIndex() {
		return actualSong;
	}

	/**
	 * Start playing {@link Song} at specified index in {@link Playlist}
	 * If there is no {@link Song} at this index, {@link SongPlayer} will continue playing current song
	 *
	 * @param index
	 */
	public void playSong(int index) {
		lock.lock();
		try {
			if (playlist.exist(index)) {
				currentPlaying = playlist.get(index);
				actualSong = index;
				tick = -1;
				fadeIn.setFadeDone(0);
				fadeOut.setFadeDone(0);
				if (currentPlaying instanceof Song)
				CallUpdate("song", currentPlaying);
				CallUpdate("fadeDone", fadeIn.getFadeDone());
				CallUpdate("tick", tick);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Start playing {@link Song} that is next in {@link Playlist} or random {@link Song} from {@link Playlist}
	 */
	public void playNextSong() {
		lock.lock();
		try {
			tick = currentPlaying.getLength();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Gets the SoundCategory of this SongPlayer
	 * @see SoundCategory
	 * @return SoundCategory of this SongPlayer
	 */
	public SoundCategory getCategory() {
		return soundCategory;
	}

	/**
	 * Sets the SoundCategory for this SongPlayer
	 * @param soundCategory
	 */
	public void setCategory(SoundCategory soundCategory) {
		this.soundCategory = soundCategory;
		CallUpdate("soundCategory", soundCategory.name());
	}
	
	/**
	 * Sets whether the SongPlayer will loop
	 * @deprecated
	 * @param loop
	 */
	public void setLoop(boolean loop){
		this.repeat = RepeatMode.ALL;
	}
	
	/**
	 * Gets whether the SongPlayer will loop
	 * @deprecated
	 * @return is loop
	 */
	public boolean isLoop(){
		return repeat == RepeatMode.ALL;
	}

	/**
	 * Sets SongPlayer's {@link RepeatMode}
	 * @param repeatMode
	 */
	public void setRepeatMode(RepeatMode repeatMode){
		this.repeat = repeatMode;
	}

	/**
	 * Gets SongPlayer's {@link RepeatMode}
	 * @return
	 */
	public RepeatMode getRepeatMode(){
		return repeat;
	}

	/**
	 * Sets whether the SongPlayer will choose next song from player randomly
	 * @param random
	 */
	public void setRandom(boolean random){
		this.random = random;
	}

	/**
	 * Gets whether the SongPlayer will choose next song from player randomly
	 * @return is random
	 */
	public boolean isRandom(){
		return random;
	}

	public ChannelMode getChannelMode(){
		return channelMode;
	}

	void CallUpdate(String key, Object value){
		if (oldSongPlayer == null){
			return;
		}
		try {
			Method m = com.xxmicloxx.NoteBlockAPI.SongPlayer.class.getDeclaredMethod("update", String.class, Object.class);
			m.setAccessible(true);
			m.invoke(oldSongPlayer, key, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
	void makeNewClone(Class newClass){
		try {
			Constructor c = newClass.getDeclaredConstructor(new Class[] { SongPlayer.class });
			c.setAccessible(true);
			oldSongPlayer = (com.xxmicloxx.NoteBlockAPI.SongPlayer) c.newInstance(new Object[]{this});
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

}
