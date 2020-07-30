package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Note;

public class NoteUtils {

    private static float[] pitches = null;

    static {
        pitches = new float[2401];

        for (int i = 0; i < 2401; i++){
            pitches[i] = (float) Math.pow(2, (i - 1200d) / 1200d);
        }
    }

    @Deprecated
    public static float getPitch(Note note){
        return getPitch(note.getKey(), note.getPitch());
    }

    @Deprecated
    public static float getPitch(byte key, short pitch){
        return getPitchTransposed(key, pitch);
    }

    /**
     * Get pitch in specific octave range
     *
     * @param note note
     * @return pitch
     */
    public static float getPitchInOctave(Note note) {
        return getPitchInOctave(note.getKey(), note.getPitch());
    }

    /**
     * Get pitch in specific octave range
     *
     * @param key   sound key
     * @param pitch extra pitch
     * @return pitch
     */
    public static float getPitchInOctave(byte key, short pitch) {
        // Apply pitch to key
        key = applyPitchToKey(key, pitch);
        pitch %= 100;

        if (key < 33) key -= 9;
        else if (key > 57) key -= 57;
        else key -= 33;

        return pitches[key * 100 + pitch];
    }

    public static byte applyPitchToKey(byte key, short pitch) {
        key += pitch / 100;
        return key;
    }

    /**
     * Get pitch after transposed
     *
     * @param note note
     * @return pitch
     */
    public static float getPitchTransposed(Note note) {
        return getPitchTransposed(note.getKey(), note.getPitch());
    }

    /**
     * Get pitch after transposed
     *
     * @param key   sound key
     * @param pitch extra pitch
     * @return pitch
     */
    public static float getPitchTransposed(byte key, short pitch) {
        // Apply pitch to key
        key += pitch % 100;
        pitch /= 100;

        while (key < 33) key += 12;
        while (key > 57) key -= 12;

        return pitches[key * 100 + pitch];
    }

}
