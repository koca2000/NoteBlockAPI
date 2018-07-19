package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SongPlayer {

	protected Song song;
	protected boolean playing = false;
	protected short tick = -1;
	protected Map<String, Boolean> playerList = Collections.synchronizedMap(new HashMap<String, Boolean>());
	protected boolean autoDestroy = false;
	protected boolean destroyed = false;
	protected Thread playerThread;
	protected byte fadeTarget = 100;
	protected byte volume = 100;
	protected byte fadeStart = volume;
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

	public FadeType getFadeType() {
		return fadeType;
	}

	public void setFadeType(FadeType fadeType) {
		this.fadeType = fadeType;
	}

	public byte getFadeTarget() {
		return fadeTarget;
	}

	public void setFadeTarget(byte fadeTarget) {
		this.fadeTarget = fadeTarget;
	}

	public byte getFadeStart() {
		return fadeStart;
	}

	public void setFadeStart(byte fadeStart) {
		this.fadeStart = fadeStart;
	}

	public int getFadeDuration() {
		return fadeDuration;
	}

	public void setFadeDuration(int fadeDuration) {
		this.fadeDuration = fadeDuration;
	}

	public int getFadeDone() {
		return fadeDone;
	}

	public void setFadeDone(int fadeDone) {
		this.fadeDone = fadeDone;
	}

	protected void calculateFade() {
		if (fadeDone == fadeDuration) {
			return; // no fade today
		}
		double targetVolume = Interpolator.interpLinear(
				new double[]{0, fadeStart, fadeDuration, fadeTarget}, fadeDone);
		setVolume((byte)targetVolume);
		fadeDone++;
	}

	@SuppressWarnings("deprecation")
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
							for (String string : playerList.keySet()) {
								Player player = Bukkit.getPlayerExact(string);
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

	public List<String> getPlayerList() {
		List<String> list = new ArrayList<String>();
		list.addAll(playerList.keySet());
		return Collections.unmodifiableList(list);
	}

	public void addPlayer(Player player) {
		lock.lock();
		try {
			if (!playerList.containsKey(player.getName())) {
				playerList.put(player.getName(), false);
				ArrayList<SongPlayer> songs = NoteBlockPlayerMain.plugin.playingSongs
						.get(player.getName());
				if (songs == null) {
					songs = new ArrayList<SongPlayer>();
				}
				songs.add(this);
				NoteBlockPlayerMain.plugin.playingSongs.put(player.getName(), songs);
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean getAutoDestroy() {
		lock.lock();
		try {
			return autoDestroy;
		} finally {
			lock.unlock();
		}
	}

	public void setAutoDestroy(boolean value) {
		lock.lock();
		try {
			autoDestroy = value;
		} finally {
			lock.unlock();
		}
	}

	public abstract void playTick(Player p, int tick);

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

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
		if (!playing) {
			SongStoppedEvent event = new SongStoppedEvent(this);
			Bukkit.getPluginManager().callEvent(event);
		}
	}

	public short getTick() {
		return tick;
	}

	public void setTick(short tick) {
		this.tick = tick;
	}

	public void removePlayer(Player p) {
		lock.lock();
		try {
			playerList.remove(p.getName());
			if (NoteBlockPlayerMain.plugin.playingSongs.get(p.getName()) == null) {
				return;
			}
			ArrayList<SongPlayer> songs = new ArrayList<SongPlayer>(
					NoteBlockPlayerMain.plugin.playingSongs.get(p.getName()));
			songs.remove(this);
			NoteBlockPlayerMain.plugin.playingSongs.put(p.getName(), songs);
			if (playerList.isEmpty() && autoDestroy) {
				SongEndEvent event = new SongEndEvent(this);
				Bukkit.getPluginManager().callEvent(event);
				destroy();
			}
		} finally {
			lock.unlock();
		}
	}

	public byte getVolume() {
		return volume;
	}

	public void setVolume(byte volume) {
		this.volume = volume;
	}

	public Song getSong() {
		return song;
	}

	public SoundCategory getCategory() {
		return soundCategory;
	}

	public void setCategory(SoundCategory soundCategory) {
		this.soundCategory = soundCategory;
	}
}
