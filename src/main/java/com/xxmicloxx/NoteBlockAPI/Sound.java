package com.xxmicloxx.NoteBlockAPI;

/**
 * Version independent Spigot sounds.
 *
 * Enum mapping to sound names for different
 * Minecraft versions.
 * 
 * @see https://gist.github.com/NiklasEi/7bd0ffd136f8459df0940e4501d47a8a
 * @author NiklasEi
 */
public enum Sound {

	NOTE_BASS_DRUM("NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM", "BLOCK_NOTE_BLOCK_BASEDRUM"),
	NOTE_BASS("NOTE_BASS", "BLOCK_NOTE_BASS", "BLOCK_NOTE_BLOCK_BASS"),
	NOTE_BELL("NOTE_BELL", "BLOCK_NOTE_BELL", "BLOCK_NOTE_BLOCK_BELL"),
	NOTE_CHIME("NOTE_CHIME", "BLOCK_NOTE_CHIME", "BLOCK_NOTE_BLOCK_CHIME"),
	NOTE_FLUTE("NOTE_FLUTE", "BLOCK_NOTE_FLUTE", "BLOCK_NOTE_BLOCK_FLUTE"),
	NOTE_BASS_GUITAR("NOTE_BASS_GUITAR", "BLOCK_NOTE_GUITAR", "BLOCK_NOTE_BLOCK_GUITAR"),
	NOTE_PIANO("NOTE_PIANO", "BLOCK_NOTE_HARP", "BLOCK_NOTE_BLOCK_HARP"),
	NOTE_STICKS("NOTE_STICKS", "BLOCK_NOTE_HAT", "BLOCK_NOTE_BLOCK_HAT"),
	NOTE_PLING("NOTE_PLING", "BLOCK_NOTE_PLING", "BLOCK_NOTE_BLOCK_PLING"),
	NOTE_SNARE_DRUM("NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE", "BLOCK_NOTE_BLOCK_SNARE"),
	NOTE_XYLOPHONE("NOTE_XYLOPHONE", "BLOCK_NOTE_XYLOPHONE", "BLOCK_NOTE_BLOCK_XYLOPHONE");

	private String[] versionDependentNames;
	private org.bukkit.Sound cached = null;

	Sound(String... versionDependentNames) {
		this.versionDependentNames = versionDependentNames;
	}

	public static org.bukkit.Sound getFromBukkitName(String bukkitSoundName) {
		for (Sound sound : values()) {
			org.bukkit.Sound bukkitSound = sound.getSound();
			if (bukkitSound != null) return bukkitSound;
		}
		return org.bukkit.Sound.valueOf(bukkitSoundName);
	}

	private org.bukkit.Sound getSound() {
		if (cached != null) return cached;
		for (String name : versionDependentNames) {
			try {
				return cached = org.bukkit.Sound.valueOf(name);
			} catch (IllegalArgumentException ignore2) {
				// try next
			}
		}
		return null;
	}

	/**
	 * Get the bukkit sound for current server version
	 *
	 * Caches sound on first call
	 * @return corresponding {@link org.bukkit.Sound}
	 */
	public org.bukkit.Sound bukkitSound() {
		if (getSound() != null) {
			return getSound();
		}
		throw new IllegalArgumentException("Found no valid sound name for " + this.name());
	}

}