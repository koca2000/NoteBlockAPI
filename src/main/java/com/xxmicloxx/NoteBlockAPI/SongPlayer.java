package com.xxmicloxx.NoteBlockAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SongPlayer {

    protected Song song;
    protected boolean playing = false;
    protected short tick = -1;
    //protected ArrayList<String> playerList = new ArrayList<String>();
    protected HashMap<String, Boolean> playerList = new HashMap<String, Boolean>();
    protected boolean autoDestroy = false;
    protected boolean destroyed = false;
    protected Thread playerThread;
    protected byte fadeTarget = 100;
    protected byte volume = 100;
    protected byte fadeStart = volume;
    protected int fadeDuration = 60;
    protected int fadeDone = 0;
    protected FadeType fadeType = FadeType.FADE_LINEAR;

    public SongPlayer(Song song) {
        this.song = song;
        createThread();
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
        double targetVolume = Interpolator.interpLinear(new double[]{0, fadeStart, fadeDuration, fadeTarget}, fadeDone);
        setVolume((byte)targetVolume);
        fadeDone++;
    }

    protected void createThread() {
    	playerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!destroyed) {
                    long startTime = System.currentTimeMillis();
                    /*synchronized (SongPlayer.this) {
                        if (playing) {
                            calculateFade();
                            tick++;
                            if (tick > song.getLength()) {
                                playing = false;
                                tick = -1;
                                SongEndEvent event = new SongEndEvent(SongPlayer.this);
                                Bukkit.getPluginManager().callEvent(event);
                                if (autoDestroy) {
                                    destroy();
                                    return;
                                }
                            }
                            for (String s : playerList) {
                                @SuppressWarnings("deprecation")
								Player p = Bukkit.getPlayerExact(s);
                                if (p == null) {
                                    // offline...
                                    continue;
                                }
                                playTick(p, tick);
                            }
                        }
                    }*/
                    
                    try {
                        synchronized (Bukkit.getScheduler()) {
                        	if (destroyed || NoteBlockPlayerMain.plugin.disabling){
                            	break;
                            }
                            Bukkit.getScheduler().callSyncMethod(NoteBlockPlayerMain.plugin, new Callable<Boolean>() {
                                public Boolean call() {
                                	if (playing) {
                                        calculateFade();
                                        tick++;
                                        if (tick > song.getLength()) {
                                            playing = false;
                                            tick = -1;
                                            SongEndEvent event = new SongEndEvent(SongPlayer.this);
                                            Bukkit.getPluginManager().callEvent(event);
                                            if (autoDestroy) {
                                                destroy();
                                                return false;
                                            }
                                        }
                                        for (String s : playerList.keySet()) {
                                            @SuppressWarnings("deprecation")
            								Player p = Bukkit.getPlayerExact(s);
                                            if (p == null) {
                                                // offline...
                                                continue;
                                            }
                                            playTick(p, tick);
                                        }
                                    }
                                    return true;
                                }
                            });
                        }
                    } catch (java.lang.IllegalStateException e) {
                        e.printStackTrace();
                    }
                    
                    if (destroyed){
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
            }
        });
        playerThread.setPriority(Thread.MAX_PRIORITY);
        playerThread.start();
    }

    public List<String> getPlayerList() {
    	List<String> list = new ArrayList<String>();
    	list.addAll(playerList.keySet());
        return Collections.unmodifiableList(list);
    }

    public void addPlayer(Player p) {
        synchronized (this) {
            if (!playerList.containsKey(p.getName())) {
                playerList.put(p.getName(), false);
                ArrayList<SongPlayer> songs = NoteBlockPlayerMain.plugin.playingSongs
                        .get(p.getName());
                if (songs == null) {
                    songs = new ArrayList<SongPlayer>();
                }
                songs.add(this);
                NoteBlockPlayerMain.plugin.playingSongs.put(p.getName(), songs);
            }
        }
    }

    
    
    public boolean getAutoDestroy() {
        synchronized (this) {
            return autoDestroy;
        }
    }

    public void setAutoDestroy(boolean value) {
        synchronized (this) {
            autoDestroy = value;
        }
    }

    public abstract void playTick(Player p, int tick);

    public void destroy() {
        synchronized (this) {
            SongDestroyingEvent event = new SongDestroyingEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            //Bukkit.getScheduler().cancelTask(threadId);
            if (event.isCancelled()) {
                return;
            }
            destroyed = true;
            playing = false;
            setTick((short) -1);
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
        synchronized (this) {
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
}
