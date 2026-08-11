package com.xxmicloxx.NoteBlockAPI.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoteUtilsTest {

	@Test
	public void negativeFinePitchMovesToPreviousKeyWithoutIndexUnderflow() {
		assertEquals(32, NoteUtils.applyPitchToKey((byte) 33, (short) -30));
		assertEquals(expectedPitch(2370), NoteUtils.getPitchInOctave((byte) 33, (short) -30), 0.000001f);
	}

	@Test
	public void negativeFinePitchSelectsThePreviousOctaveSample() {
		assertEquals("test_-1", InstrumentUtils.warpNameOutOfRange("test", (byte) 33, (short) -30));
		assertEquals("test", InstrumentUtils.warpNameOutOfRange("test", (byte) 57, (short) -30));
	}

	private float expectedPitch(int index) {
		return (float) Math.pow(2, (index - 1200d) / 1200d);
	}
}
