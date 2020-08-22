package com.xxmicloxx.NoteBlockAPI.utils;

import com.google.common.base.Preconditions;

/**
 * Midi instrument to Minecraft instrument mapping
 * Used mapping from OpenNoteBlockStudio
 *
 * This file is licensed under MIT
 *
 * Copyright (c) 2019 Hielke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class MidiInstruments {

    public static MidiInstrument[] instruments = new MidiInstrument[]
            {
                    // Piano
                    new MidiInstrument("Acoustic Grand Piano", "Grand Piano", 0, 0),
                    new MidiInstrument("Bright Acoustic Piano", "Acoustic Piano", 0, 0),
                    new MidiInstrument("Electric Grand Piano", "E. Grand Piano", 0, 0),
                    new MidiInstrument("Honky - tonk Piano", "H.T. Piano", 0, 0),
                    new MidiInstrument("Electric Piano 1", "E. Piano 1", 0, 0),
                    new MidiInstrument("Electric Piano 2", "E. Piano 2", 0, 0),
                    new MidiInstrument("Harpsichord", null, 0, 1),
                    new MidiInstrument("Clavinet", null, 0, 0),
                    // Chromatic Percussion
                    new MidiInstrument("Celesta", null, 11, -1),
                    new MidiInstrument("Glockenspiel", null, 11, 0),
                    new MidiInstrument("Music Box", null, 11, 0),
                    new MidiInstrument("Vibraphone", null, 11, 0),
                    new MidiInstrument("Marimba", null, 11, 0),
                    new MidiInstrument("Xylophone", null, 9, 0),
                    new MidiInstrument("Tubular Bells", "T. Bells", 7, -1),
                    new MidiInstrument("Dulcimer", null, 7, 0),
                    // Organ
                    new MidiInstrument("Drawbar Organ", "D. Organ", 1, 1),
                    new MidiInstrument("Percussive Organ", "P. Organ", 1, 1),
                    new MidiInstrument("Rock Organ", null, 0, 0),
                    new MidiInstrument("Church Organ", null, 0, 0),
                    new MidiInstrument("Reed Organ", null, 0, 0),
                    new MidiInstrument("Accordion", null, 0, 0),
                    new MidiInstrument("Harmonica", null, 0, 0),
                    new MidiInstrument("Tango Accordion", null, 0, 0),
            };

    public static class MidiInstrument {

        public final String name;
        public final String shortenedName;
        public final int mcInstrument;
        public final int octaveDifference;

        public MidiInstrument(String name, String shortenedName, int mcInstrument, int octaveDifference) {
            Preconditions.checkNotNull(name);
            Preconditions.checkArgument(mcInstrument >= 0);
            Preconditions.checkArgument(mcInstrument <= 15);
            this.name = name;
            this.shortenedName = shortenedName;
            this.mcInstrument = mcInstrument;
            this.octaveDifference = octaveDifference;
        }
    }
}
