package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Note;

public class NoteUtils {

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
        if (key < 33) key -= 9;
        else if (key > 57) key -= 57;
        else key -= 33;

        return (float) (0.5 * (Math.pow(2, (key / 12.0))) + pitch);
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
        while (key < 33) key += 12;
        while (key > 57) key -= 12;

        return (float) (0.5 * (Math.pow(2, (key - 3 / 12.0))) + pitch);
    }

}
