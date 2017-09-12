package com.xxmicloxx.NoteBlockAPI;

import java.io.File;
import java.util.HashMap;

public class Song {

    private HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
    private short songHeight;
    private short length;
    private String title;
    private File path;
    private String author;
    private String description;
    private float speed;
    private float delay;
    private CustomInstrument[] customInstrument;

    public Song(Song other) {
        this(other.getSpeed(), other.getLayerHashMap(), other.getSongHeight(), other.getLength(), other.getTitle(), other.getAuthor(), other.getDescription(), other.getPath(), other.getCustomInstruments());
    }
    
    public Song(float speed, HashMap<Integer, Layer> layerHashMap,
                short songHeight, final short length, String title, String author,
                String description, File path) {
        this(speed, layerHashMap, songHeight, length, title, author, description, path, new CustomInstrument[0]);
    }

	public Song(float speed, HashMap<Integer, Layer> layerHashMap, short songHeight, final short length, String title, String author, String description, File path, CustomInstrument[] customInstrument) {
    	this.speed = speed;
        delay = 20 / speed;
        this.layerHashMap = layerHashMap;
        this.songHeight = songHeight;
        this.length = length;
        this.title = title;
        this.author = author;
        this.description = description;
        this.path = path;
        this.customInstrument = customInstrument;
    }

    public HashMap<Integer, Layer> getLayerHashMap() {
        return layerHashMap;
    }

    public short getSongHeight() {
        return songHeight;
    }

    public short getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public File getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDelay() {
        return delay;
    }
    
    public CustomInstrument[] getCustomInstruments(){
    	return customInstrument;
    }
}
