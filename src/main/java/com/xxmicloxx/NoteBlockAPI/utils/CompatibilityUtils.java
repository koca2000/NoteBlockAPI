package com.xxmicloxx.NoteBlockAPI.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;

/**
 * Fields/methods for reflection &amp; version checking
 */
public class CompatibilityUtils {

	public static final String OBC_DIR = Bukkit.getServer().getClass().getPackage().getName();
	public static final String NMS_DIR = OBC_DIR.replaceFirst("org.bukkit.craftbukkit", "net.minecraft.server");

	private static Class<? extends Enum> soundCategoryClass;
	private static final HashMap<String, Method> playSoundMethod = new HashMap<>();

	private static float serverVersion = -1;

	/**
	 * Gets NMS class from given name
	 * @param name of class (w/ package)
	 * @return Class of given name
	 */
	public static Class<?> getMinecraftClass(String name) {
		try {
			return Class.forName(NMS_DIR + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets CraftBukkit class from given name
	 * @param name of class (w/ package)
	 * @return Class of given name
	 */
	public static Class<?> getCraftBukkitClass(String name) {
		try {
			return Class.forName(OBC_DIR + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Class<? extends Enum> getSoundCategoryClass() throws ClassNotFoundException {
		if (isSoundCategoryCompatible() && soundCategoryClass == null){
			soundCategoryClass = (Class<? extends Enum>) Class.forName("org.bukkit.SoundCategory");
		}
		return soundCategoryClass;
	}

	private static Method getPlaySoundMethod(Class sound, boolean soundCategory) throws ClassNotFoundException, NoSuchMethodException {
		Method method = playSoundMethod.get(sound.getName() + soundCategory);
		if (method == null){
			if (soundCategory) {
				method = Player.class.getMethod("playSound", Location.class, sound,
						getSoundCategoryClass(), float.class, float.class);
			} else {
				method = Player.class.getMethod("playSound", Location.class, sound, float.class, float.class);
			}
			playSoundMethod.put(sound.getName() + soundCategory, method);
		}
		return method;
	}

	/**
	 * Returns if SoundCategory is able to be used
	 * @see org.bukkit.SoundCategory
	 * @see SoundCategory
	 * @return can use SoundCategory
	 */
	protected static boolean isSoundCategoryCompatible() {
		return getServerVersion() >= 0.0111f;
	}

	/**
	 * Plays a sound using NMS &amp; reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 * @param distance
	 */
	public static void playSound(Player player, Location location, String sound,
								 SoundCategory category, float volume, float pitch, float distance) {
		playSoundUniversal(player, location, sound, category, volume, pitch, distance);
	}

	/**
	 * Plays a sound using NMS &amp; reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 * @param distance
	 */
	public static void playSound(Player player, Location location, Sound sound,
								 SoundCategory category, float volume, float pitch, float distance) {
		playSoundUniversal(player, location, sound, category, volume, pitch, distance);
	}

	private static void playSoundUniversal(Player player, Location location, Object sound,
								 SoundCategory category, float volume, float pitch, float distance) {
		try {
			if (isSoundCategoryCompatible()) {
				Method method = getPlaySoundMethod(sound.getClass(), true);
				Enum<?> soundCategoryEnum = Enum.valueOf(getSoundCategoryClass(), category.name());
				method.invoke(player, MathUtils.stereoPan(location, distance), sound, soundCategoryEnum, volume, pitch);
			} else {
				Method method = getPlaySoundMethod(sound.getClass(), false);
				method.invoke(player, MathUtils.stereoPan(location, distance), sound, volume, pitch);
			}
		} catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return list of instuments which were added in specified version
	 * @param serverVersion 1.12 = 0.0112f, 1.14 = 0.0114f,...
	 * @return list of custom instruments, if no instuments were added in specified version returns empty list
	 */
	public static ArrayList<CustomInstrument> getVersionCustomInstruments(float serverVersion){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();
		if (serverVersion == 0.0112f){
			instruments.add(new CustomInstrument((byte) 0, "Guitar", "block.note_block.guitar.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Flute", "block.note_block.flute.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bell", "block.note_block.bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Chime", "block.note_block.icechime.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Xylophone", "block.note_block.xylobone.ogg"));
			return instruments;
		}

		if (serverVersion == 0.0114f){
			instruments.add(new CustomInstrument((byte) 0, "Iron Xylophone", "block.note_block.iron_xylophone.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Cow Bell", "block.note_block.cow_bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Didgeridoo", "block.note_block.didgeridoo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bit", "block.note_block.bit.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Banjo", "block.note_block.banjo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Pling", "block.note_block.pling.ogg"));
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

		if (getServerVersion() < 0.0112f){
			if (firstCustomInstrumentIndex == 10) {
				instruments.addAll(getVersionCustomInstruments(0.0112f));
			} else if (firstCustomInstrumentIndex == 16){
				instruments.addAll(getVersionCustomInstruments(0.0112f));
				instruments.addAll(getVersionCustomInstruments(0.0114f));
			}
		} else if (getServerVersion() < 0.0114f){
			if (firstCustomInstrumentIndex == 16){
				instruments.addAll(getVersionCustomInstruments(0.0114f));
			}
		}

		return instruments;
	}

	/**
	 * Returns server version as float less than 1 with two digits for each version part
	 * @return e.g. 0.011401f for 1.14.1
	 */
	public static float getServerVersion(){
		if (serverVersion != -1){
			return serverVersion;
		}

		String versionInfo = Bukkit.getServer().getVersion();
		int start = versionInfo.lastIndexOf('(');
		int end = versionInfo.lastIndexOf(')');

		String[] versionParts = versionInfo.substring(start + 5, end).split("\\.");

		String versionString = "0.";
		for (String part : versionParts){
			if (part.length() == 1){
				versionString += "0";
			}

			versionString += part;
		}
		serverVersion = Float.parseFloat(versionString);
		return serverVersion;
	}

	public static Material getNoteBlockMaterial(){
		return Material.valueOf("NOTE_BLOCK");
	}

}
