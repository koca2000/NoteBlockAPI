package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Note;

public class NoteUtils {

    public static float getPitch(Note note){
        return getPitch(note.getKey(), note.getPitch());
    }

    public static float getPitch(byte key, short pitch){
        if (key < 33) key -= 9;
        else if (key > 57) key -= 57;
        else key -= 33;

        return (float) (0.5 * (Math.pow(2, (key / 12.0))) + pitch);
    }

}
