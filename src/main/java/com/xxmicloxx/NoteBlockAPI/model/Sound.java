package com.xxmicloxx.NoteBlockAPI.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Version independent Spigot sounds.
 *
 * Enum mapping to note names for different
 * Minecraft versions.
 * 
 * @see <a href="https://gist.github.com/NiklasEi/7bd0ffd136f8459df0940e4501d47a8a">https://gist.github.com/NiklasEi/7bd0ffd136f8459df0940e4501d47a8a</a>
 * @author NiklasEi
 */
public enum Sound {

	NOTE_PIANO(0, "minecraft:block.note_block.harp", "NOTE_PIANO", "BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP"),
	NOTE_BASS(1, "minecraft:block.note_block.bass", "NOTE_BASS", "BLOCK_NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS"),
	NOTE_BASS_DRUM(2, "minecraft:block.note_block.basedrum", "NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM", "BLOCK_NOTE_BLOCK_BASEDRUM"),
	NOTE_SNARE_DRUM(3, "minecraft:block.note_block.snare", "NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE"),
	NOTE_STICKS(4, "minecraft:block.note_block.hat","NOTE_STICKS", "BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT"),
	NOTE_BASS_GUITAR(5, "minecraft:block.note_block.guitar", "NOTE_BASS_GUITAR", "BLOCK_NOTE_GUITAR", "BLOCK_NOTE_BLOCK_GUITAR"),
	NOTE_FLUTE(6, "minecraft:block.note_block.flute", "NOTE_FLUTE", "BLOCK_NOTE_FLUTE", "BLOCK_NOTE_BLOCK_FLUTE"),
	NOTE_BELL(7, "minecraft:block.note_block.bell", "NOTE_BELL", "BLOCK_NOTE_BELL", "BLOCK_NOTE_BLOCK_BELL"),
	NOTE_CHIME(8, "minecraft:block.note_block.chime", "NOTE_CHIME", "BLOCK_NOTE_CHIME", "BLOCK_NOTE_BLOCK_CHIME"),
	NOTE_XYLOPHONE(9, "minecraft:block.note_block.xylophone", "NOTE_XYLOPHONE", "BLOCK_NOTE_XYLOPHONE", "BLOCK_NOTE_BLOCK_XYLOPHONE"),
	NOTE_PLING(15, "minecraft:block.note_block.pling", "NOTE_PLING", "BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING"),
	NOTE_IRON_XYLOPHONE(10, "minecraft:block.note_block.iron_xylophone", "BLOCK_NOTE_BLOCK_IRON_XYLOPHONE"),
	NOTE_COW_BELL(11, "minecraft:block.note_block.cow_bell", "BLOCK_NOTE_BLOCK_COW_BELL"),
	NOTE_DIDGERIDOO(12, "minecraft:block.note_block.didgeridoo", "BLOCK_NOTE_BLOCK_DIDGERIDOO"),
	NOTE_BIT(13, "minecraft:block.note_block.bit", "BLOCK_NOTE_BLOCK_BIT"),
	NOTE_BANJO(14, "minecraft:block.note_block.banjo", "BLOCK_NOTE_BLOCK_BANJO");

	private static final Map<String, Sound> soundsByName = new HashMap<>();
	private static final Sound[] soundsByIndex = new Sound[values().length];

	private final int instrumentIndex;
	private final String resourcePackName;
	private final String[] versionDependentNames;

	private org.bukkit.Sound cached = null;

	Sound(int instrumentIndex, @NotNull String resourcePackName, @NotNull String... versionDependentNames) {
		this.instrumentIndex = instrumentIndex;
		this.resourcePackName = resourcePackName;
		this.versionDependentNames = versionDependentNames;
	}

	/**
	 * Attempts to retrieve the org.bukkit.Sound equivalent of a version dependent enum name
	 * @param bukkitSoundName
	 * @deprecated Use {@link #getByBukkitName(String)} and {@link #getBukkitSound()} to get org.bukkit.Sound
	 * @return org.bukkit.Sound enum
	 */
	@Deprecated
	public static org.bukkit.Sound getFromBukkitName(@NotNull String bukkitSoundName) {
		Sound sound = soundsByName.get(bukkitSoundName.toUpperCase());
		if (sound != null)
			return sound.getBukkitSound();

		return org.bukkit.Sound.valueOf(bukkitSoundName);
	}

	@Nullable
	public static Sound getByBukkitName(@NotNull String bukkitSoundName){
		return soundsByName.get(bukkitSoundName.toUpperCase());
	}

	@Nullable
	public static Sound getByIndex(int index){
		if (index < 0 || index >= soundsByIndex.length)
			return null;
		return soundsByIndex[index];
	}

	public int getInstrumentIndex(){
		return instrumentIndex;
	}

	@NotNull
	public String getResourcePackName(){
		return resourcePackName;
	}

	@Nullable
	public org.bukkit.Sound getBukkitSound() {
		return cached;
	}

	private void cacheBukkitSound(){
		org.bukkit.Sound[] bukkitSounds = org.bukkit.Sound.values();
		for (org.bukkit.Sound sound : bukkitSounds) {
			for (String name : versionDependentNames) {
				if (sound.name().equals(name)) {
					cached = sound;
					return;
				}
			}
		}
	}

	/**
	 * Get the bukkit sound for current server version
	 *
	 * Caches sound on first call
	 * @return corresponding {@link org.bukkit.Sound}
	 * @deprecated Use {@link #getBukkitSound()}
	 */
	@NotNull
	public org.bukkit.Sound bukkitSound() {
		if (getBukkitSound() != null) {
			return getBukkitSound();
		}
		throw new IllegalArgumentException("Found no valid sound name for " + this.name());
	}

	static {
		// Cache sound access.
		for (Sound sound : values()) {
			sound.cacheBukkitSound();
			soundsByIndex[sound.getInstrumentIndex()] = sound;
			for (String name : sound.versionDependentNames)
				soundsByName.put(name, sound);
		}
	}
}