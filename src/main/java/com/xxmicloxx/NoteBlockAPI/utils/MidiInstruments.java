package com.xxmicloxx.NoteBlockAPI.utils;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.HashMap;
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

    static {
        Map<Integer, MidiInstrument> mapping = new ConcurrentHashMap<>(128);
        // Piano
        mapping.put(0, new MidiInstrument(0, 0));
        mapping.put(1, new MidiInstrument(0, 0));
        mapping.put(2, new MidiInstrument(13, 0));
        mapping.put(3, new MidiInstrument(0, 0));
        mapping.put(4, new MidiInstrument(13, 0));
        mapping.put(5, new MidiInstrument(13, 0));
        mapping.put(6, new MidiInstrument(0, 1));
        mapping.put(7, new MidiInstrument(0, 0));
        // Chromatic Percussion
        mapping.put(8, new MidiInstrument(11, 0));
        mapping.put(9, new MidiInstrument(11, 0));
        mapping.put(10, new MidiInstrument(11, 0));
        mapping.put(11, new MidiInstrument(11, 0));
        mapping.put(12, new MidiInstrument(11, 0));
        mapping.put(13, new MidiInstrument(9, 0));
        mapping.put(14, new MidiInstrument(7, -1));
        mapping.put(15, new MidiInstrument(7, 0));
        // Organ
        mapping.put(16, new MidiInstrument(1, 1));
        mapping.put(17, new MidiInstrument(1, 1));
        mapping.put(18, new MidiInstrument(0, 0));
        mapping.put(19, new MidiInstrument(0, 0));
        mapping.put(20, new MidiInstrument(0, 0));
        mapping.put(21, new MidiInstrument(0, 0));
        mapping.put(22, new MidiInstrument(0, 0));
        mapping.put(23, new MidiInstrument(0, 0));
        // Guitar
        mapping.put(24, new MidiInstrument(5, 0));
        mapping.put(25, new MidiInstrument(5, 0));
        mapping.put(26, new MidiInstrument(5, 1));
        mapping.put(27, new MidiInstrument(5, 0));
        // mapping.put(28, new MidiInstrument(-1, 0));
        mapping.put(29, new MidiInstrument(5, -1));
        mapping.put(30, new MidiInstrument(5, -1));
        mapping.put(31, new MidiInstrument(5, 0));
        // Bass
        mapping.put(32, new MidiInstrument(1, 1));
        mapping.put(33, new MidiInstrument(1, 2));
        mapping.put(34, new MidiInstrument(1, 2));
        mapping.put(35, new MidiInstrument(1, 2));
        mapping.put(36, new MidiInstrument(1, 2));
        mapping.put(37, new MidiInstrument(1, 2));
        mapping.put(38, new MidiInstrument(1, 2));
        mapping.put(39, new MidiInstrument(1, 2));
        // Strings
        mapping.put(40, new MidiInstrument(6, 0));
        mapping.put(41, new MidiInstrument(6, 0));
        mapping.put(42, new MidiInstrument(6, 0));
        mapping.put(43, new MidiInstrument(6, 0));
        mapping.put(44, new MidiInstrument(0, 0));
        mapping.put(45, new MidiInstrument(0, 0));
        mapping.put(46, new MidiInstrument(8, 0));
        mapping.put(47, new MidiInstrument(3, 1));
        // Ensemble
        mapping.put(48, new MidiInstrument(0, 0));
        mapping.put(49, new MidiInstrument(0, 0));
        mapping.put(50, new MidiInstrument(0, 0));
        mapping.put(51, new MidiInstrument(0, 0));
        mapping.put(52, new MidiInstrument(0, 0));
        mapping.put(53, new MidiInstrument(0, 0));
        mapping.put(54, new MidiInstrument(0, 0));
        mapping.put(55, new MidiInstrument(0, 0));
        // Brass
        mapping.put(56, new MidiInstrument(0, 0));
        mapping.put(57, new MidiInstrument(0, 0));
        mapping.put(58, new MidiInstrument(0, 0));
        mapping.put(59, new MidiInstrument(0, 0));
        mapping.put(60, new MidiInstrument(0, 0));
        mapping.put(61, new MidiInstrument(0, 0));
        mapping.put(62, new MidiInstrument(1, 1));
        mapping.put(63, new MidiInstrument(1, 1));
        // Reed
        mapping.put(64, new MidiInstrument(6, 0));
        mapping.put(65, new MidiInstrument(6, 0));
        mapping.put(66, new MidiInstrument(6, 0));
        mapping.put(67, new MidiInstrument(6, 0));
        mapping.put(68, new MidiInstrument(6, 0));
        mapping.put(69, new MidiInstrument(6, 0));
        mapping.put(70, new MidiInstrument(6, -1));
        mapping.put(71, new MidiInstrument(6, 0));
        // Pipe
        mapping.put(72, new MidiInstrument(6, -1));
        mapping.put(73, new MidiInstrument(6, -1));
        mapping.put(74, new MidiInstrument(6, -1));
        mapping.put(75, new MidiInstrument(6, -1));
        mapping.put(76, new MidiInstrument(6, -1));
        mapping.put(77, new MidiInstrument(6, -1));
        mapping.put(78, new MidiInstrument(6, -1));
        mapping.put(79, new MidiInstrument(6, -1));
        // Synth Lead
        mapping.put(80, new MidiInstrument(0, 0));
        mapping.put(81, new MidiInstrument(0, 0));
        mapping.put(82, new MidiInstrument(0, 0));
        mapping.put(83, new MidiInstrument(0, 0));
        mapping.put(84, new MidiInstrument(0, 0));
        mapping.put(85, new MidiInstrument(0, 0));
        mapping.put(86, new MidiInstrument(0, 0));
        mapping.put(87, new MidiInstrument(0, 1));
        mapping.put(88, new MidiInstrument(0, 0));
        mapping.put(89, new MidiInstrument(0, 0));
        mapping.put(90, new MidiInstrument(0, 0));
        mapping.put(91, new MidiInstrument(0, 0));
        mapping.put(92, new MidiInstrument(0, 0));
        mapping.put(93, new MidiInstrument(0, 0));
        mapping.put(94, new MidiInstrument(0, 0));
        mapping.put(95, new MidiInstrument(0, 0));
        // Synth Effects
        // mapping.put(96, new MidiInstrument(0, 0));
        // mapping.put(97, new MidiInstrument(0, 0));
        mapping.put(98, new MidiInstrument(13, 0));
        mapping.put(99, new MidiInstrument(0, 0));
        mapping.put(100, new MidiInstrument(0, 0));
        // mapping.put(101, new MidiInstrument(0, 0));
        // mapping.put(102, new MidiInstrument(0, 0));
        // mapping.put(103, new MidiInstrument(0, 0));
        // Ethnic
        mapping.put(104, new MidiInstrument(14, 0));
        mapping.put(105, new MidiInstrument(14, 0));
        mapping.put(106, new MidiInstrument(14, 0));
        mapping.put(107, new MidiInstrument(14, 0));
        mapping.put(108, new MidiInstrument(1, 1));
        mapping.put(109, new MidiInstrument(0, 0));
        mapping.put(110, new MidiInstrument(0, 0));
        mapping.put(111, new MidiInstrument(0, 0));
        // Percussive
        mapping.put(112, new MidiInstrument(7, 0));
        mapping.put(113, new MidiInstrument(0, 0));
        mapping.put(114, new MidiInstrument(10, 0));
        mapping.put(115, new MidiInstrument(4, 0));
        mapping.put(116, new MidiInstrument(3, 0));
        mapping.put(117, new MidiInstrument(3, 0));
        mapping.put(118, new MidiInstrument(3, 0));
        // Sound Effects
        // mapping.put(119, new MidiInstrument(0, 0));
        // mapping.put(120, new MidiInstrument(0, 0));
        // mapping.put(121, new MidiInstrument(0, 0));
        // mapping.put(122, new MidiInstrument(0, 0));
        // mapping.put(123, new MidiInstrument(0, 0));
        // mapping.put(124, new MidiInstrument(0, 0));
        // mapping.put(125, new MidiInstrument(0, 0));
        // mapping.put(126, new MidiInstrument(0, 0));
        mapping.put(127, new MidiInstrument(0, 0));
        instrumentMapping = Collections.unmodifiableMap(mapping);
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
}
