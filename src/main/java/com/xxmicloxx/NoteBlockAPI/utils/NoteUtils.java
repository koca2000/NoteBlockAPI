package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.model.Note;

public class NoteUtils {

    private static float[] pitches = null;

    public static void generatePitchValues(){
        pitches = new float[2401];

        for (int i = 0; i < 2401; i++){
            pitches[i] = (float) Math.pow(2, (i - 1200d) / 1200d);
        }
    }

    public static float getPitch(Note note){
        return getPitch(note.getKey(), note.getPitch());
    }

    public static float getPitch(byte key, short pitch){
        pitch += (key - 33) * 100;
        if (pitch < 0) pitch = 0;

        if (pitch > 2400) pitch = 2400;

        if (pitches == null) generatePitchValues();

        return pitches[pitch];
    }

}
