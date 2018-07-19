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

public class NBSDecoder {

	public static Song parse(File decodeFile) {
		try {
			return parse(new FileInputStream(decodeFile), decodeFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Song parse(InputStream inputStream) {
		return parse(inputStream, null); // Source is unknown -> no file
	}

	private static Song parse(InputStream inputStream, File decodeFile) {
		HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
		byte biggestInstrumentIndex = -1;
		try {
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			short length = readShort(dataInputStream);
			short songHeight = readShort(dataInputStream);
			String title = readString(dataInputStream);
			String author = readString(dataInputStream);
			readString(dataInputStream);
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
					if (instrument > biggestInstrumentIndex) {
						biggestInstrumentIndex = instrument;
					}
					setNote(layer, tick, instrument /* instrument */, 
							dataInputStream.readByte() /* note */, layerHashMap);
				}
			}
			for (int i = 0; i < songHeight; i++) {
				Layer l = layerHashMap.get(i);

				String name = readString(dataInputStream);
				byte volume = dataInputStream.readByte();
				if (l != null) {
					l.setName(name);
					l.setVolume(volume);
				}
			}
			//count of custom instruments
			byte custom = dataInputStream.readByte();
			CustomInstrument[] customInstruments = new CustomInstrument[custom];

			for (int index = 0; index < custom; index++) {
				customInstruments[index] = new CustomInstrument((byte) index, 
						readString(dataInputStream), readString(dataInputStream));
			}

			if (Instrument.isCustomInstrument((byte) (biggestInstrumentIndex - custom))){
				ArrayList<CustomInstrument> ci = CompatibilityUtils.get1_12Instruments();
				ci.addAll(Arrays.asList(customInstruments));
				customInstruments = ci.toArray(customInstruments);
			}

			return new Song(speed, layerHashMap, songHeight, length, title, 
					author, description, decodeFile, customInstruments);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e){
			String file = "";
			if (decodeFile != null){
				file = decodeFile.getName();
			}
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Song is corrupted: " + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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
