package com.xxmicloxx.NoteBlockAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils}
 */
@Deprecated
public class CompatibilityUtils {

	public static final String OBC_DIR = Bukkit.getServer().getClass().getPackage().getName();
	public static final String NMS_DIR = OBC_DIR.replaceFirst("org.bukkit.craftbukkit", "net.minecraft.server");

	/**
	 * Gets NMS class from given name
	 * @param name of class (w/ package)
	 * @return Class of given name
	 */
	public static Class<?> getMinecraftClass(String name) {
		return com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getMinecraftClass(name);
	}

	/**
	 * Gets CraftBukkit class from given name
	 * @param name of class (w/ package)
	 * @return Class of given name
	 */
	public static Class<?> getCraftBukkitClass(String name) {
		return com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getCraftBukkitClass(name);
	}

	/**
	 * Returns whether the version of Bukkit is or is after 1.12
	 * @return version is after 1.12
	 */
	public static boolean isPost1_12() {
		return com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.isPost1_12();
	}

	/**
	 * Returns if SoundCategory is able to be used
	 * @see org.bukkit.SoundCategory
	 * @see SoundCategory
	 * @return can use SoundCategory
	 */
	protected static boolean isSoundCategoryCompatible() {
		return com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getServerVersion() >= 0.0111f;
	}

	/**
	 * Plays a sound using NMS and reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 */
	public static void playSound(Player player, Location location, String sound, 
			SoundCategory category, float volume, float pitch) {
		com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.playSound(player, location, sound, com.xxmicloxx.NoteBlockAPI.model.SoundCategory.valueOf(category.name()), volume, pitch);
	}

	/**
	 * Plays a sound using NMS and reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 */
	public static void playSound(Player player, Location location, Sound sound, 
			SoundCategory category, float volume, float pitch) {
		com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.playSound(player, location, sound, com.xxmicloxx.NoteBlockAPI.model.SoundCategory.valueOf(category.name()), volume, pitch);
	}

	/**
	 * Gets instruments which were added post-1.12
	 * @return ArrayList of instruments
	 * @deprecated Use {@link #getVersionCustomInstruments(float)}
	 */
	public static ArrayList<CustomInstrument> get1_12Instruments(){
		return getVersionCustomInstruments(0.0112f);
	}

	/**
	 * Return list of instuments which were added in specified version
	 * @param serverVersion 1.12 = 0.0112f, 1.14 = 0.0114f,...
	 * @return list of custom instruments, if no instuments were added in specified version returns empty list
	 */
	public static ArrayList<CustomInstrument> getVersionCustomInstruments(float serverVersion){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();
		if (serverVersion == 0.0112f){
			instruments.add(new CustomInstrument((byte) 0, "Guitar", "guitar.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Flute", "flute.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bell", "bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Chime", "icechime.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Xylophone", "xylobone.ogg"));
			return instruments;
		}

		if (serverVersion == 0.0114f){
			instruments.add(new CustomInstrument((byte) 0, "Iron Xylophone", "iron_xylophone.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Cow Bell", "cow_bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Didgeridoo", "didgeridoo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bit", "bit.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Banjo", "banjo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Pling", "pling.ogg"));
			return instruments;
		}
		return instruments;
	}

	/**
	 * Return list of custom instruments based on song first custom instrument index and server version
	 * @param firstCustomInstrumentIndex
	 * @return
	 */
	public static ArrayList<CustomInstrument> getVersionCustomInstrumentsForSong(int firstCustomInstrumentIndex){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();

		if (com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getServerVersion() < 0.0112f){
			if (firstCustomInstrumentIndex == 10) {
				instruments.addAll(getVersionCustomInstruments(0.0112f));
			} else if (firstCustomInstrumentIndex == 16){
				instruments.addAll(getVersionCustomInstruments(0.0112f));
				instruments.addAll(getVersionCustomInstruments(0.0114f));
			}
		} else if (com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils.getServerVersion() < 0.0114f){
			if (firstCustomInstrumentIndex == 16){
				instruments.addAll(getVersionCustomInstruments(0.0114f));
			}
		}

		return instruments;
	}
	
}
