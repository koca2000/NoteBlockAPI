package com.xxmicloxx.NoteBlockAPI;

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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.xxmicloxx.NoteBlockAPI.utils.InstrumentUtils;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder}
 */
@Deprecated
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
	 * @param inputStream of a Note Block Studio project file (.nbs)
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
		HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
		byte biggestInstrumentIndex = -1;
		try {
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			short length = readShort(dataInputStream);
			int firstcustominstrument = 10; //Backward compatibility - most of songs with old structure are from 1.12
			int firstcustominstrumentdiff;
			int nbsversion = 0;
			if (length == 0) {
				nbsversion = dataInputStream.readByte();
				firstcustominstrument = dataInputStream.readByte();
				if (nbsversion >= 3) {
					length = readShort(dataInputStream);
				}
			}
			firstcustominstrumentdiff = InstrumentUtils.getCustomInstrumentFirstIndex() - firstcustominstrument;
			short songHeight = readShort(dataInputStream);
			String title = readString(dataInputStream);
			String author = readString(dataInputStream);
			readString(dataInputStream); // original author
			String description = readString(dataInputStream);
			float speed = readShort(dataInputStream) / 100f;
			dataInputStream.readBoolean(); // auto-save
			dataInputStream.readByte(); // auto-save duration
			dataInputStream.readByte(); // x/4ths, time signature
			readInt(dataInputStream); // minutes spent on project
			readInt(dataInputStream); // left clicks (why?)
			readInt(dataInputStream); // right clicks (why?)
			readInt(dataInputStream); // blocks added
			readInt(dataInputStream); // blocks removed
			readString(dataInputStream); // .mid/.schematic file name
			if (nbsversion >= 4) {
				dataInputStream.readByte(); // loop on/off
				dataInputStream.readByte(); // max loop count
				readShort(dataInputStream); // loop start tick
			}
			short tick = -1;
			while (true) {
				short jumpTicks = readShort(dataInputStream); // jumps till next tick
				//System.out.println("Jumps to next tick: " + jumpTicks);
				if (jumpTicks == 0) {
					break;
				}
				tick += jumpTicks;
				//System.out.println("Tick: " + tick);
				short layer = -1;
				while (true) {
					short jumpLayers = readShort(dataInputStream); // jumps till next layer
					if (jumpLayers == 0) {
						break;
					}
					layer += jumpLayers;
					//System.out.println("Layer: " + layer);
					byte instrument = dataInputStream.readByte();

					if (firstcustominstrumentdiff > 0 && instrument >= firstcustominstrument){
						instrument += firstcustominstrumentdiff;
					}

					byte key = dataInputStream.readByte();

					if (nbsversion >= 4) {
						dataInputStream.readByte(); // note block velocity
						dataInputStream.readByte(); // note block panning
						readShort(dataInputStream); // note block pitch
					}

					setNote(layer, tick, instrument /* instrument */,
							key/* note */, layerHashMap);
				}
			}

			if (nbsversion > 0 && nbsversion < 3) {
				length = tick;
			}

			for (int i = 0; i < songHeight; i++) {
				Layer layer = layerHashMap.get(i);

				String name = readString(dataInputStream);
				if (nbsversion >= 4){
					dataInputStream.readByte(); // layer lock
				}

				byte volume = dataInputStream.readByte();
				if (nbsversion >= 2){
					dataInputStream.readByte(); // layer stereo
				}
				if (layer != null) {
					layer.setName(name);
					layer.setVolume(volume);
				}
			}
			//count of custom instruments
			byte customAmnt = dataInputStream.readByte();
			CustomInstrument[] customInstrumentsArray = new CustomInstrument[customAmnt];

			for (int index = 0; index < customAmnt; index++) {
				customInstrumentsArray[index] = new CustomInstrument((byte) index,
						readString(dataInputStream), readString(dataInputStream));
				dataInputStream.readByte();//pitch
				dataInputStream.readByte();//key
			}

			if (firstcustominstrumentdiff < 0){
				ArrayList<CustomInstrument> customInstruments = CompatibilityUtils.getVersionCustomInstrumentsForSong(firstcustominstrument);
				customInstruments.addAll(Arrays.asList(customInstrumentsArray));
				customInstrumentsArray = customInstruments.toArray(customInstrumentsArray);
			} else {
				firstcustominstrument += firstcustominstrumentdiff;
			}

			return new Song(speed, layerHashMap, songHeight, length, title,
					author, description, songFile, firstcustominstrument, customInstrumentsArray);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

	/**
	 * Sets a note at a tick in a song
	 * @param layerIndex
	 * @param ticks
	 * @param instrument
	 * @param key
	 * @param layerHashMap
	 */
	private static void setNote(int layerIndex, int ticks, byte instrument,
								byte key, HashMap<Integer, Layer> layerHashMap) {
		Layer layer = layerHashMap.get(layerIndex);
		if (layer == null) {
			layer = new Layer();
			layerHashMap.put(layerIndex, layer);
		}
		layer.setNote(ticks, new Note(instrument, key));
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

}
