package com.xxmicloxx.NoteBlockAPI.utils;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Midi instrument to Minecraft instrument mapping
 * Used mapping from OpenNoteBlockStudio
 * https://github.com/HielkeMinecraft/OpenNoteBlockStudio/blob/master/scripts/midi_instruments/midi_instruments.gml
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

    public static final Map<Integer, MidiInstrument> instrumentMapping;
    public static final Map<Integer, MidiPercussion> percussionMapping;

    static {
        Map<Integer, MidiInstrument> imap = new ConcurrentHashMap<>(128);

        // ------------------ Instrument  ------------------
        // Piano
        imap.put(0, new MidiInstrument(0, 0));
        imap.put(1, new MidiInstrument(0, 0));
        imap.put(2, new MidiInstrument(13, 0));
        imap.put(3, new MidiInstrument(0, 0));
        imap.put(4, new MidiInstrument(13, 0));
        imap.put(5, new MidiInstrument(13, 0));
        imap.put(6, new MidiInstrument(0, 1));
        imap.put(7, new MidiInstrument(0, 0));
        // Chromatic Percussion
        imap.put(8, new MidiInstrument(11, -1));
        imap.put(9, new MidiInstrument(11, 0));
        imap.put(10, new MidiInstrument(11, 0));
        imap.put(11, new MidiInstrument(11, 0));
        imap.put(12, new MidiInstrument(11, 0));
        imap.put(13, new MidiInstrument(9, 0));
        imap.put(14, new MidiInstrument(7, -1));
        imap.put(15, new MidiInstrument(7, 0));
        // Organ
        imap.put(16, new MidiInstrument(1, 1));
        imap.put(17, new MidiInstrument(1, 1));
        imap.put(18, new MidiInstrument(0, 0));
        imap.put(19, new MidiInstrument(0, 0));
        imap.put(20, new MidiInstrument(0, 0));
        imap.put(21, new MidiInstrument(0, 0));
        imap.put(22, new MidiInstrument(0, 0));
        imap.put(23, new MidiInstrument(0, 0));
        // Guitar
        imap.put(24, new MidiInstrument(5, 0));
        imap.put(25, new MidiInstrument(5, 0));
        imap.put(26, new MidiInstrument(5, 1));
        imap.put(27, new MidiInstrument(5, 0));
        // imap.put(28, new MidiInstrument(-1, 0));
        imap.put(29, new MidiInstrument(5, -1));
        imap.put(30, new MidiInstrument(5, -1));
        imap.put(31, new MidiInstrument(5, 0));
        // Bass
        imap.put(32, new MidiInstrument(1, 1));
        imap.put(33, new MidiInstrument(1, 2));
        imap.put(34, new MidiInstrument(1, 2));
        imap.put(35, new MidiInstrument(1, 2));
        imap.put(36, new MidiInstrument(1, 2));
        imap.put(37, new MidiInstrument(1, 2));
        imap.put(38, new MidiInstrument(1, 2));
        imap.put(39, new MidiInstrument(1, 2));
        // Strings
        imap.put(40, new MidiInstrument(6, 0));
        imap.put(41, new MidiInstrument(6, 0));
        imap.put(42, new MidiInstrument(6, 0));
        imap.put(43, new MidiInstrument(6, 0));
        imap.put(44, new MidiInstrument(0, 0));
        imap.put(45, new MidiInstrument(0, 0));
        imap.put(46, new MidiInstrument(8, 0));
        imap.put(47, new MidiInstrument(3, 1));
        // Ensemble
        imap.put(48, new MidiInstrument(0, 0));
        imap.put(49, new MidiInstrument(0, 0));
        imap.put(50, new MidiInstrument(0, 0));
        imap.put(51, new MidiInstrument(0, 0));
        imap.put(52, new MidiInstrument(0, 0));
        imap.put(53, new MidiInstrument(0, 0));
        imap.put(54, new MidiInstrument(0, 0));
        imap.put(55, new MidiInstrument(0, 0));
        // Brass
        imap.put(56, new MidiInstrument(0, 0));
        imap.put(57, new MidiInstrument(0, 0));
        imap.put(58, new MidiInstrument(0, 0));
        imap.put(59, new MidiInstrument(0, 0));
        imap.put(60, new MidiInstrument(0, 0));
        imap.put(61, new MidiInstrument(0, 0));
        imap.put(62, new MidiInstrument(1, 1));
        imap.put(63, new MidiInstrument(1, 1));
        // Reed
        imap.put(64, new MidiInstrument(6, 0));
        imap.put(65, new MidiInstrument(6, 0));
        imap.put(66, new MidiInstrument(6, 0));
        imap.put(67, new MidiInstrument(6, 0));
        imap.put(68, new MidiInstrument(6, 0));
        imap.put(69, new MidiInstrument(6, 0));
        imap.put(70, new MidiInstrument(6, -1));
        imap.put(71, new MidiInstrument(6, 0));
        // Pipe
        imap.put(72, new MidiInstrument(6, -1));
        imap.put(73, new MidiInstrument(6, -1));
        imap.put(74, new MidiInstrument(6, -1));
        imap.put(75, new MidiInstrument(6, -1));
        imap.put(76, new MidiInstrument(6, -1));
        imap.put(77, new MidiInstrument(6, -1));
        imap.put(78, new MidiInstrument(6, -1));
        imap.put(79, new MidiInstrument(6, -1));
        // Synth Lead
        imap.put(80, new MidiInstrument(0, 0));
        imap.put(81, new MidiInstrument(0, 0));
        imap.put(82, new MidiInstrument(0, 0));
        imap.put(83, new MidiInstrument(0, 0));
        imap.put(84, new MidiInstrument(0, 0));
        imap.put(85, new MidiInstrument(0, 0));
        imap.put(86, new MidiInstrument(0, 0));
        imap.put(87, new MidiInstrument(0, 1));
        imap.put(88, new MidiInstrument(0, 0));
        imap.put(89, new MidiInstrument(0, 0));
        imap.put(90, new MidiInstrument(0, 0));
        imap.put(91, new MidiInstrument(0, 0));
        imap.put(92, new MidiInstrument(0, 0));
        imap.put(93, new MidiInstrument(0, 0));
        imap.put(94, new MidiInstrument(0, 0));
        imap.put(95, new MidiInstrument(0, 0));
        // Synth Effects
        // imap.put(96, new MidiInstrument(0, 0));
        // imap.put(97, new MidiInstrument(0, 0));
        imap.put(98, new MidiInstrument(13, 0));
        imap.put(99, new MidiInstrument(0, 0));
        imap.put(100, new MidiInstrument(0, 0));
        // imap.put(101, new MidiInstrument(0, 0));
        // imap.put(102, new MidiInstrument(0, 0));
        // imap.put(103, new MidiInstrument(0, 0));
        // Ethnic
        imap.put(104, new MidiInstrument(14, 0));
        imap.put(105, new MidiInstrument(14, 0));
        imap.put(106, new MidiInstrument(14, 0));
        imap.put(107, new MidiInstrument(14, 0));
        imap.put(108, new MidiInstrument(1, 1));
        imap.put(109, new MidiInstrument(0, 0));
        imap.put(110, new MidiInstrument(0, 0));
        imap.put(111, new MidiInstrument(0, 0));
        // Percussive
        imap.put(112, new MidiInstrument(7, 0));
        imap.put(113, new MidiInstrument(0, 0));
        imap.put(114, new MidiInstrument(10, 0));
        imap.put(115, new MidiInstrument(4, 0));
        imap.put(116, new MidiInstrument(3, 0));
        imap.put(117, new MidiInstrument(3, 0));
        imap.put(118, new MidiInstrument(3, 0));
        // Sound Effects
        // imap.put(119, new MidiInstrument(0, 0));
        // imap.put(120, new MidiInstrument(0, 0));
        // imap.put(121, new MidiInstrument(0, 0));
        // imap.put(122, new MidiInstrument(0, 0));
        // imap.put(123, new MidiInstrument(0, 0));
        // imap.put(124, new MidiInstrument(0, 0));
        // imap.put(125, new MidiInstrument(0, 0));
        // imap.put(126, new MidiInstrument(0, 0));
        imap.put(127, new MidiInstrument(0, 0));
        instrumentMapping = Collections.unmodifiableMap(new ConcurrentHashMap<>(imap));


        // ------------------ Percussion  ------------------
        Map<Integer, MidiPercussion> pmap = new ConcurrentHashMap<>(64);
        // pmap.put(24, new MidiPercussion(0, 0));
        // pmap.put(25, new MidiPercussion(0, 0));
        // pmap.put(26, new MidiPercussion(0, 0));
        // pmap.put(27, new MidiPercussion(0, 0));
        // pmap.put(28, new MidiPercussion(0, 0));
        // pmap.put(29, new MidiPercussion(0, 0));
        // pmap.put(31, new MidiPercussion(0, 0));
        // pmap.put(32, new MidiPercussion(0, 0));
        // pmap.put(33, new MidiPercussion(0, 0));
        // pmap.put(34, new MidiPercussion(0, 0));

        pmap.put(35, new MidiPercussion(2, 10));
        pmap.put(36, new MidiPercussion(2, 6));
        pmap.put(37, new MidiPercussion(4, 6));
        pmap.put(38, new MidiPercussion(3, 8));
        pmap.put(39, new MidiPercussion(4, 6));
        pmap.put(40, new MidiPercussion(3, 4));
        pmap.put(41, new MidiPercussion(2, 6));

        pmap.put(42, new MidiPercussion(3, 22));
        pmap.put(43, new MidiPercussion(2, 13));
        pmap.put(44, new MidiPercussion(3, 22));
        pmap.put(45, new MidiPercussion(2, 15));
        pmap.put(46, new MidiPercussion(3, 18));
        pmap.put(47, new MidiPercussion(2, 20));
        pmap.put(48, new MidiPercussion(2, 23));

        pmap.put(49, new MidiPercussion(3, 17));
        pmap.put(50, new MidiPercussion(2, 23));
        pmap.put(51, new MidiPercussion(3, 24));
        pmap.put(52, new MidiPercussion(3, 8));
        pmap.put(53, new MidiPercussion(3, 13));
        pmap.put(54, new MidiPercussion(4, 18));
        pmap.put(55, new MidiPercussion(3, 18));

        pmap.put(56, new MidiPercussion(4, 1));
        pmap.put(57, new MidiPercussion(3, 13));
        pmap.put(58, new MidiPercussion(4, 2));
        pmap.put(59, new MidiPercussion(3, 13));
        pmap.put(60, new MidiPercussion(4, 9));
        pmap.put(61, new MidiPercussion(4, 2));
        pmap.put(62, new MidiPercussion(4, 8));

        pmap.put(63, new MidiPercussion(2, 22));
        pmap.put(64, new MidiPercussion(2, 15));
        pmap.put(65, new MidiPercussion(3, 13));
        pmap.put(66, new MidiPercussion(3, 8));
        pmap.put(67, new MidiPercussion(4, 8));
        pmap.put(68, new MidiPercussion(4, 3));
        pmap.put(69, new MidiPercussion(4, 20));

        pmap.put(70, new MidiPercussion(4, 23));
        // pmap.put(71, new MidiPercussion(0, 0));
        // pmap.put(72, new MidiPercussion(0, 0));
        pmap.put(73, new MidiPercussion(4, 17));
        pmap.put(74, new MidiPercussion(4, 11));
        pmap.put(75, new MidiPercussion(4, 18));
        pmap.put(76, new MidiPercussion(4, 9));

        pmap.put(77, new MidiPercussion(4, 5));
        // pmap.put(78, new MidiPercussion(0, 0));
        // pmap.put(79, new MidiPercussion(0, 0));
        pmap.put(80, new MidiPercussion(4, 17));
        pmap.put(81, new MidiPercussion(4, 22));
        pmap.put(82, new MidiPercussion(3, 22));
        // pmap.put(83, new MidiPercussion(0, 0));

        // pmap.put(84, new MidiPercussion(0, 0));
        pmap.put(85, new MidiPercussion(4, 21));
        pmap.put(86, new MidiPercussion(2, 14));
        pmap.put(87, new MidiPercussion(2, 7));

        percussionMapping = Collections.unmodifiableMap(new ConcurrentHashMap<>(pmap));
    }

    public static class MidiInstrument {

        public final int mcInstrument;
        public final int octaveModifier;

        public MidiInstrument(int mcInstrument, int octaveModifier) {
            Preconditions.checkArgument(mcInstrument >= 0);
            Preconditions.checkArgument(mcInstrument <= 15);
            this.mcInstrument = mcInstrument;
            this.octaveModifier = octaveModifier;
        }
    }

    public static class MidiPercussion {

        public final int mcInstrument;
        public final int midiKey;

        public MidiPercussion(int mcInstrument, int mcKey) {
            Preconditions.checkArgument(mcInstrument >= 0);
            Preconditions.checkArgument(mcInstrument <= 15);
            this.mcInstrument = mcInstrument;
            this.midiKey = mcKey + 33;
        }
    }
}
