package com.xxmicloxx.NoteBlockAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @Deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils}
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

	/**
	 * Returns whether the version of Bukkit is or is after 1.12
	 * @return version is after 1.12
	 */
	public static boolean isPost1_12() {
		if (!isSoundCategoryCompatible() || Bukkit.getVersion().contains("1.11")) {
			return false;
		}
		return true;
	}

	/**
	 * Returns if SoundCategory is able to be used
	 * @see org.bukkit.SoundCategory
	 * @see SoundCategory
	 * @return can use SoundCategory
	 */
	protected static boolean isSoundCategoryCompatible() {
		if (Bukkit.getVersion().contains("1.7") 
				|| Bukkit.getVersion().contains("1.8") 
				|| Bukkit.getVersion().contains("1.9") 
				|| Bukkit.getVersion().contains("1.10")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Plays a sound using NMS & reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 */
	public static void playSound(Player player, Location location, String sound, 
			SoundCategory category, float volume, float pitch) {
		try {
			if (isSoundCategoryCompatible()) {
				Method method = Player.class.getMethod("playSound", Location.class, String.class, 
						Class.forName("org.bukkit.SoundCategory"), float.class, float.class);
				Class<? extends Enum> soundCategory = 
						(Class<? extends Enum>) Class.forName("org.bukkit.SoundCategory");
				Enum<?> soundCategoryEnum = Enum.valueOf(soundCategory, category.name());
				method.invoke(player, location, sound, soundCategoryEnum, volume, pitch);
			} else {
				Method method = Player.class.getMethod("playSound", Location.class, 
						String.class, float.class, float.class);
				method.invoke(player, location, sound, volume, pitch);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Plays a sound using NMS & reflection
	 * @param player
	 * @param location
	 * @param sound
	 * @param category
	 * @param volume
	 * @param pitch
	 */
	public static void playSound(Player player, Location location, Sound sound, 
			SoundCategory category, float volume, float pitch) {
		try {
			if (isSoundCategoryCompatible()) {
				Method method = Player.class.getMethod("playSound", Location.class, Sound.class, 
						Class.forName("org.bukkit.SoundCategory"), float.class, float.class);
				Class<? extends Enum> soundCategory = 
						(Class<? extends Enum>) Class.forName("org.bukkit.SoundCategory");
				Enum<?> soundCategoryEnum = Enum.valueOf(soundCategory, category.name());
				method.invoke(player, location, sound, soundCategoryEnum, volume, pitch);
			} else {
				Method method = Player.class.getMethod("playSound", Location.class, 
						Sound.class, float.class, float.class);
				method.invoke(player, location, sound, volume, pitch);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets instruments which were added post-1.12
	 * @return ArrayList of instruments
	 */
	public static ArrayList<CustomInstrument> get1_12Instruments(){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();
		instruments.add(new CustomInstrument((byte) 0, "Guitar", "guitar.ogg"));
		instruments.add(new CustomInstrument((byte) 0, "Flute", "flute.ogg"));
		instruments.add(new CustomInstrument((byte) 0, "Bell", "bell.ogg"));
		instruments.add(new CustomInstrument((byte) 0, "Chime", "icechime.ogg"));
		instruments.add(new CustomInstrument((byte) 0, "Xylophone", "xylobone.ogg"));
		return instruments;
	}
	
}
