package com.xxmicloxx.NoteBlockAPI.model;

import com.xxmicloxx.NoteBlockAPI.utils.CompatibilityUtils;
import org.bukkit.Bukkit;

/**
 * Represents pitches of every noteblock note pre- &amp; post-1.9
 * @deprecated Use NoteUtils
 */
@Deprecated()
public enum NotePitch {

	NOTE_0(0, 0.5F, 0.50000F),
	NOTE_1(1, 0.53F, 0.52973F),
	NOTE_2(2, 0.56F, 0.56123F),
	NOTE_3(3, 0.6F, 0.59461F),
	NOTE_4(4, 0.63F, 0.62995F),
	NOTE_5(5, 0.67F, 0.66741F),
	NOTE_6(6, 0.7F, 0.70711F),
	NOTE_7(7, 0.76F, 0.74916F),
	NOTE_8(8, 0.8F, 0.79370F),
	NOTE_9(9, 0.84F, 0.84089F),
	NOTE_10(10, 0.9F, 0.89091F),
	NOTE_11(11, 0.94F, 0.94386F),
	NOTE_12(12, 1.0F, 1.00000F),
	NOTE_13(13, 1.06F, 1.05945F),
	NOTE_14(14, 1.12F, 1.12245F),
	NOTE_15(15, 1.18F, 1.18920F),
	NOTE_16(16, 1.26F, 1.25993F),
	NOTE_17(17, 1.34F, 1.33484F),
	NOTE_18(18, 1.42F, 1.41420F),
	NOTE_19(19, 1.5F, 1.49832F),
	NOTE_20(20, 1.6F, 1.58741F),
	NOTE_21(21, 1.68F, 1.68180F),
	NOTE_22(22, 1.78F, 1.78180F),
	NOTE_23(23, 1.88F, 1.88775F),
	NOTE_24(24, 2.0F, 2.00000F);

	public int note;
	public float pitchPre1_9;
	public float pitchPost1_9;

	NotePitch(int note, float pitchPre1_9, float pitchPost1_9) {
		this.note = note;
		this.pitchPre1_9 = pitchPre1_9;
		this.pitchPost1_9 = pitchPost1_9;
	}

	public static float getPitch(int note) {
		boolean pre1_9 = CompatibilityUtils.getServerVersion() < 0.0109f;
		for (NotePitch notePitch : values()) {
			if (notePitch.note == note) {
				return pre1_9 ? notePitch.pitchPre1_9 : notePitch.pitchPost1_9;
			}
		}

		return 0.0F;
	}

}