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

        // -15 base_-2
        // 9 base_-1
        // 33 base
        // 57 base_1
        // 81 base_2
        // 105 base_3
        if(key < 9) key -= -15;
        else if(key < 33) key -= 9;
        else if(key < 57) key -= 33;
        else if(key < 81) key -= 57;
        else if(key < 105) key -= 81;

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
        // Apply key to pitch
        pitch += key * 100;

        while (pitch < 3300) pitch += 1200;
        while (pitch > 5700) pitch -= 1200;

        pitch -= 3300;

        return pitches[pitch];
    }

    /**
     * Returns true if combination of specified key and pitch is outside Minecraft octave range
     * @param key
     * @param pitch
     * @return
     */
    public static boolean isOutOfRange(byte key, short pitch){
        key = applyPitchToKey(key, pitch);

        if(key < 33) return true;
        else if(key < 57) return false;
        else return true;
    }

}
