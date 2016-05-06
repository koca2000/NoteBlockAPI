package com.xxmicloxx.NoteBlockAPI;

public enum NotePitch {

	NOTE_0(0, 0.50000F),
    NOTE_1(1, 0.52973F),
    NOTE_2(2, 0.56123F),
    NOTE_3(3, 0.59461F),
    NOTE_4(4, 0.62995F),
    NOTE_5(5, 0.66741F),
    NOTE_6(6, 0.70711F),
    NOTE_7(7, 0.74916F),
    NOTE_8(8, 0.79370F),
    NOTE_9(9, 0.84089F),
    NOTE_10(10, 0.89091F),
    NOTE_11(11, 0.94386F),
    NOTE_12(12, 1.00000F),
    NOTE_13(13, 1.05945F),
    NOTE_14(14, 1.12245F),
    NOTE_15(15, 1.18920F),
    NOTE_16(16, 1.25993F),
    NOTE_17(17, 1.33484F),
    NOTE_18(18, 1.41420F),
    NOTE_19(19, 1.49832F),
    NOTE_20(20, 1.58741F),
    NOTE_21(21, 1.68180F),
    NOTE_22(22, 1.78180F),
    NOTE_23(23, 1.88775F),
    NOTE_24(24, 2.00000F);

    public int note;
    public float pitch;

    private NotePitch(int note, float pitch) {
        this.note = note;
        this.pitch = pitch;
    }

    public static float getPitch(int note) {
        for (NotePitch notePitch : values()) {
            if (notePitch.note == note) {
                return notePitch.pitch;
            }
        }

        return 0.0F;
    }
}