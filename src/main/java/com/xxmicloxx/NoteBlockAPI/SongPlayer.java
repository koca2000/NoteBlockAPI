package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Plays a Song for a list of Players
 *
 */
public abstract class SongPlayer {

	protected Song song;

	protected boolean playing = false;
	protected short tick = -1;
	protected Map<UUID, Boolean> playerList = Collections.synchronizedMap(new HashMap<UUID, Boolean>());

	protected boolean autoDestroy = false;
	protected boolean destroyed = false;

	protected Thread playerThread;

	protected byte volume = 100;
	protected byte fadeStart = volume;
	protected byte fadeTarget = 100;
	protected int fadeDuration = 60;
	protected int fadeDone = 0;
	protected FadeType fadeType = FadeType.FADE_LINEAR;

	private final Lock lock = new ReentrantLock();

	protected NoteBlockPlayerMain plugin;

	protected SoundCategory soundCategory;

	public SongPlayer(Song song) {
		this(song, SoundCategory.MASTER);
	}

	public SongPlayer(Song song, SoundCategory soundCategory) {
		this.song = song;
		this.soundCategory = soundCategory;
		plugin = NoteBlockPlayerMain.plugin;
		start();
	}

	/**
	 * Gets the FadeType for this SongPlayer (unused)
	 * @return FadeType
	 */
	public FadeType getFadeType() {
		return fadeType;
	}

	/**
	 * Sets the FadeType for this SongPlayer
	 * @param fadeType
	 */
	public void setFadeType(FadeType fadeType) {
		this.fadeType = fadeType;
	}

	/**
	 * Target volume for fade
	 * @return byte representing fade target
	 */
	public byte getFadeTarget() {
		return fadeTarget;
	}

	/**
	 * Set target volume for fade
	 * @param fadeTarget
	 */
	public void setFadeTarget(byte fadeTarget) {
		this.fadeTarget = fadeTarget;
	}

	/**
	 * Gets the starting time for the fade
	 * @return
	 */
	public byte getFadeStart() {
		return fadeStart;
	}

	/**
	 * Sets the starting time for the fade
	 * @param fadeStart
	 */
	public void setFadeStart(byte fadeStart) {
		this.fadeStart = fadeStart;
	}

	/**
	 * Gets the duration of the fade
	 * @return duration of the fade
	 */
	public int getFadeDuration() {
		return fadeDuration;
	}

	/**
	 * Sets the duration of the fade
	 * @param fadeDuration
	 */
	public void setFadeDuration(int fadeDuration) {
		this.fadeDuration = fadeDuration;
	}

	/**
	 * Gets the tick when fade will be finished
	 * @return tick
	 */
	public int getFadeDone() {
		return fadeDone;
	}

	/**
	 * Sets the tick when fade will be finished
	 * @param fadeDone
	 */
	public void setFadeDone(int fadeDone) {
		this.fadeDone = fadeDone;
	}

	/**
	 * Calculates the fade at the given time and sets the current volume
	 */
	protected void calculateFade() {
		if (fadeDone == fadeDuration) {
			return; // no fade today
		}
		double targetVolume = Interpolator.interpLinear(
				new double[]{0, fadeStart, fadeDuration, fadeTarget}, fadeDone);
		setVolume((byte) targetVolume);
		fadeDone++;
	}

	/**
	 * Starts this SongPlayer
	 */
	private void start() {
		plugin.doAsync(() -> {
			while (!destroyed) {
				long startTime = System.currentTimeMillis();
				lock.lock();
				try {
					if (destroyed || NoteBlockPlayerMain.plugin.isDisabling()){
						break;
					}

					if (playing) {
						calculateFade();
						tick++;
						if (tick > song.getLength()) {
							playing = false;
							tick = -1;
							SongEndEvent event = new SongEndEvent(SongPlayer.this);
							plugin.doSync(() -> Bukkit.getPluginManager().callEvent(event));
							if (autoDestroy) {
								destroy();
							}
							return;
						}

						plugin.doSync(() -> {
							for (UUID uuid : playerList.keySet()) {
								Player player = Bukkit.getPlayer(uuid);
								if (player == null) {
									// offline...
									continue;
								}
								playTick(player, tick);
							}
						});
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
				float delayMillis = song.getDelay() * 50;
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

	/**
	 * Gets list of current Player usernames listening to this SongPlayer
	 * @return list of Player usernames
	 * @Deprecated use getPlayerUUIDs
	 */
	public List<String> getPlayerList() {
		List<String> list = new ArrayList<>();
		for (UUID uuid : playerList.keySet()) {
			list.add(Bukkit.getPlayer(uuid).getName());
		}
		return Collections.unmodifiableList(list);
	}

	/**
	 * Gets list of current Player usernames listening to this SongPlayer
	 * @return list of Player usernames
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
		lock.lock();
		try {
			if (!playerList.containsKey(player.getUniqueId())) {
				playerList.put(player.getUniqueId(), false);
				ArrayList<SongPlayer> songs = NoteBlockPlayerMain.plugin.playingSongs
						.get(player.getUniqueId());
				if (songs == null) {
					songs = new ArrayList<SongPlayer>();
				}
				songs.add(this);
				NoteBlockPlayerMain.plugin.playingSongs.put(player.getUniqueId(), songs);
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
	 * Sets autoDestroy
	 * @param if autoDestroy is enabled
	 */
	public void setAutoDestroy(boolean autoDestroy) {
		lock.lock();
		try {
			this.autoDestroy = autoDestroy;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Plays the Song for the specific player
	 * @param player to play this SongPlayer for
	 * @param tick to play at
	 */
	public abstract void playTick(Player player, int tick);

	public void destroy() {
		lock.lock();
		try {
			SongDestroyingEvent event = new SongDestroyingEvent(this);
			Bukkit.getPluginManager().callEvent(event);
			//Bukkit.getScheduler().cancelTask(threadId);
			if (event.isCancelled()) {
				return;
			}
			destroyed = true;
			playing = false;
			setTick((short) -1);
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
		this.playing = playing;
		if (!playing) {
			SongStoppedEvent event = new SongStoppedEvent(this);
			Bukkit.getPluginManager().callEvent(event);
		}
	}

	/**
	 * Gets the current tick of this SongPlayer
	 * @return
	 */
	public short getTick() {
		return tick;
	}

	/**
	 * Sets the current tick of this SongPlayer
	 * @param tick
	 */
	public void setTick(short tick) {
		this.tick = tick;
	}

	/**
	 * Removes a player from this SongPlayer
	 * @param player to remove
	 */
	public void removePlayer(Player player) {
		lock.lock();
		try {
			playerList.remove(player.getUniqueId());
			if (NoteBlockPlayerMain.plugin.playingSongs.get(player.getUniqueId()) == null) {
				return;
			}
			ArrayList<SongPlayer> songs = new ArrayList<>(
					NoteBlockPlayerMain.plugin.playingSongs.get(player.getUniqueId()));
			songs.remove(this);
			NoteBlockPlayerMain.plugin.playingSongs.put(player.getUniqueId(), songs);
			if (playerList.isEmpty() && autoDestroy) {
				SongEndEvent event = new SongEndEvent(this);
				Bukkit.getPluginManager().callEvent(event);
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
		this.volume = volume;
	}

	/**
	 * Gets the Song being played by this SongPlayer
	 * @return
	 */
	public Song getSong() {
		return song;
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
	}

}
