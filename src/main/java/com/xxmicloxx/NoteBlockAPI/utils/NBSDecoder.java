package com.xxmicloxx.NoteBlockAPI.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.xxmicloxx.NoteBlockAPI.model.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Utils for reading Note Block Studio data
 *
 */
public class NBSDecoder {

	/**
	 * Parses a Song from a Note Block Studio project file (.nbs)
	 * @see Song
	 * @param songFile .nbs file
	 * @return Song object representing a Note Block Studio project
	 */
	public static Song parse(File songFile) {
		try {
			return parse(new FileInputStream(songFile), songFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses a Song from an InputStream
	 * @see Song
	 * @param inputStream InputStream of a Note Block Studio project file (.nbs)
	 * @return Song object from the InputStream
	 */
	public static Song parse(InputStream inputStream) {
		return parse(inputStream, null); // Source is unknown -> no file
	}

	/**
	 * Parses a Song from an InputStream and a Note Block Studio project file (.nbs)
	 * @see Song
	 * @param inputStream of a .nbs file
	 * @param songFile representing a .nbs file
	 * @return Song object representing the given .nbs file
	 */
	private static Song parse(InputStream inputStream, File songFile) {
		SongBuilder builder = new SongBuilder();
		try {
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			builder.length = readShort(dataInputStream);
			builder.firstCustomInstrumentIndex = 10; //Backward compatibility - most of songs with old structure are from 1.12

			if (builder.length == 0) {
				builder.nbsVersion = dataInputStream.readByte();
				builder.firstCustomInstrumentIndex = dataInputStream.readByte();
				if (builder.nbsVersion >= 3) {
					builder.length = readShort(dataInputStream);
				}
			}
			builder.firstCustomInstrumentIndexDiff = InstrumentUtils.getCustomInstrumentFirstIndex() - builder.firstCustomInstrumentIndex;
			builder.songHeight = readShort(dataInputStream);
			builder.metadata = loadSongMetadata(dataInputStream);
			builder.metadata.setPath(songFile);
			builder.speed = readShort(dataInputStream) / 100f;
			loadUnusedMetadata(dataInputStream, builder.nbsVersion);

			loadNotes(dataInputStream, builder);
			loadLayers(dataInputStream, builder);

			loadCustomInstruments(dataInputStream,builder);

			return builder.toSong();
		} catch (EOFException e) {
			String file = "";
			if (songFile != null) {
				file = songFile.getName();
			}
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Song is corrupted: " + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SongMetadata loadSongMetadata(DataInputStream dataInputStream) throws IOException {
		SongMetadata metadata = new SongMetadata();
		metadata.setTitle(readString(dataInputStream));
		metadata.setAuthor(readString(dataInputStream));
		metadata.setOriginalAuthor(readString(dataInputStream));
		metadata.setDescription(readString(dataInputStream));
		return metadata;
	}

	private static void loadUnusedMetadata(DataInputStream dataInputStream, int nbsVersion) throws IOException {
		dataInputStream.readBoolean(); // auto-save
		dataInputStream.readByte(); // auto-save duration
		dataInputStream.readByte(); // x/4ths, time signature
		readInt(dataInputStream); // minutes spent on project
		readInt(dataInputStream); // left clicks (why?)
		readInt(dataInputStream); // right clicks (why?)
		readInt(dataInputStream); // blocks added
		readInt(dataInputStream); // blocks removed
		readString(dataInputStream); // .mid/.schematic file name
		if (nbsVersion >= 4) {
			dataInputStream.readByte(); // loop on/off
			dataInputStream.readByte(); // max loop count
			readShort(dataInputStream); // loop start tick
		}
	}

	private static void loadNotes(DataInputStream dataInputStream, SongBuilder builder) throws IOException {
		HashMap<Integer, HashMap<Integer, Note>> layers = new HashMap<>();
		short tick = -1;
		while (true) {
			short jumpTicks = readShort(dataInputStream); // jumps till next tick
			if (jumpTicks == 0) {
				break;
			}
			tick += jumpTicks;
			int layer = -1;
			while (true) {
				short jumpLayers = readShort(dataInputStream); // jumps till next layer
				if (jumpLayers == 0) {
					break;
				}
				layer += jumpLayers;

				Note note = loadNote(dataInputStream, builder);
				layers.computeIfAbsent(layer, l -> new HashMap<>()).put((int) tick, note);
			}
		}

		if (builder.nbsVersion > 0 && builder.nbsVersion < 3) {
			builder.length = tick;
		}

		HashMap<Integer, Layer> finishedLayers = new HashMap<>();
		layers.forEach((i, notes) -> finishedLayers.put(i, new Layer(notes)));
		builder.layers = finishedLayers;
	}

	private static Note loadNote(DataInputStream dataInputStream, SongBuilder builder) throws IOException {
		byte instrument = dataInputStream.readByte();

		if (builder.firstCustomInstrumentIndexDiff > 0 && instrument >= builder.firstCustomInstrumentIndex){
			instrument += builder.firstCustomInstrumentIndexDiff;
		}

		byte key = dataInputStream.readByte();
		byte velocity = 100;
		int panning = 100;
		short pitch = 0;
		if (builder.nbsVersion >= 4) {
			velocity = dataInputStream.readByte(); // note block velocity
			panning = 200 - dataInputStream.readUnsignedByte(); // note panning, 0 is right in nbs format
			pitch = readShort(dataInputStream); // note block pitch
		}

		if (panning != 100){
			builder.isStereo = true;
		}
		return new Note(instrument , key, velocity, panning, pitch);
	}

	private static void loadLayers(DataInputStream dataInputStream, SongBuilder builder) throws IOException {
		for (int i = 0; i < builder.songHeight; i++) {
			Layer layer = builder.layers.get(i);

			String name = readString(dataInputStream);
			if (builder.nbsVersion >= 4){
				dataInputStream.readByte(); // layer lock
			}

			byte volume = dataInputStream.readByte();
			int panning = 100;
			if (builder.nbsVersion >= 2){
				panning = 200 - dataInputStream.readUnsignedByte(); // layer stereo, 0 is right in nbs format
			}

			if (panning != 100){
				builder.isStereo = true;
			}

			if (layer != null) {
				layer.setName(name);
				layer.setVolume(volume);
				layer.setPanning(panning);
			}
		}
	}

	private static void loadCustomInstruments(DataInputStream dataInputStream, SongBuilder builder) throws IOException {
		//count of custom instruments
		byte customInstrumentsCount = dataInputStream.readByte();
		builder.customInstruments = new CustomInstrument[customInstrumentsCount];

		for (int index = 0; index < customInstrumentsCount; index++) {
			builder.customInstruments[index] = new CustomInstrument((byte) index,
					readString(dataInputStream), readString(dataInputStream));
			dataInputStream.readByte();//pitch
			dataInputStream.readByte();//key
		}

		if (builder.firstCustomInstrumentIndexDiff < 0){
			ArrayList<CustomInstrument> customInstruments = CompatibilityUtils.getVersionCustomInstrumentsForSong(builder.firstCustomInstrumentIndex);
			customInstruments.addAll(Arrays.asList(builder.customInstruments));
			builder.customInstruments = customInstruments.toArray(builder.customInstruments);
		} else {
			builder.firstCustomInstrumentIndex += builder.firstCustomInstrumentIndexDiff;
		}
	}

	private static short readShort(DataInputStream dataInputStream) throws IOException {
		int byte1 = dataInputStream.readUnsignedByte();
		int byte2 = dataInputStream.readUnsignedByte();
		return (short) (byte1 + (byte2 << 8));
	}

	private static int readInt(DataInputStream dataInputStream) throws IOException {
		int byte1 = dataInputStream.readUnsignedByte();
		int byte2 = dataInputStream.readUnsignedByte();
		int byte3 = dataInputStream.readUnsignedByte();
		int byte4 = dataInputStream.readUnsignedByte();
		return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
	}

	private static String readString(DataInputStream dataInputStream) throws IOException {
		int length = readInt(dataInputStream);
		StringBuilder builder = new StringBuilder(length);
		for (; length > 0; --length) {
			char c = (char) dataInputStream.readByte();
			if (c == (char) 0x0D) {
				c = ' ';
			}
			builder.append(c);
		}
		return builder.toString();
	}

	private static class SongBuilder {
		private HashMap<Integer, Layer> layers = new HashMap<>();
		private short songHeight = 0;
		private short length = 0;
		private float speed = 20;
		private CustomInstrument[] customInstruments = new CustomInstrument[0];
		private int firstCustomInstrumentIndex = 0;
		private int firstCustomInstrumentIndexDiff = 0;
		private boolean isStereo = false;

		private int nbsVersion = 0;

		private SongMetadata metadata = new SongMetadata();

		public Song toSong(){
			return new Song(speed, layers, songHeight, length, metadata, firstCustomInstrumentIndex, customInstruments, isStereo);
		}
	}
}
