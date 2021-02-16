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
    public static float getPitch(short key, short pitch){
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
    public static float getPitchInOctave(short key, short pitch) {
        // Apply pitch to key
        key = applyPitchToKey(key, pitch);
        pitch %= 100;
        if(pitch < 0) pitch = (short) (100 + pitch);

        // -15 base_-2
        // 9 base_-1
        // 33 base
        // 57 base_1
        // 81 base_2
        // 105 base_3
        while (key < 33) key += 24;
        while (key > 56) key -= 24;

        key -= 33;

        return pitches[key * 100 + pitch];
    }

    public static short applyPitchToKey(short key, short pitch) {
        if(pitch == 0) return key;
        if(pitch < 0) return (short) (key - (-pitch / 100) - (Math.abs(pitch) % 100 != 0 ? 1 : 0));
        return (short) (key + (pitch / 100));
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
    public static float getPitchTransposed(short key, short pitch) {
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
    public static boolean isOutOfRange(short key, short pitch){
        key = applyPitchToKey(key, pitch);

        if(key < 33) return true;
        else if(key < 57) return false;
        else return true;
    }

}
