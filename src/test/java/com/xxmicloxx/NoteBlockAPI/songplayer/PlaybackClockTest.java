package com.xxmicloxx.NoteBlockAPI.songplayer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaybackClockTest {

	@Test
	public void preservesEverySongTickAboveServerTickRate() {
		assertTicksPlayedAfterOneSecond(31.86f, 31);
		assertTicksPlayedAfterOneSecond(40.0f, 40);
		assertTicksPlayedAfterOneSecond(60.0f, 60);
	}

	@Test
	public void preservesFractionalTempoOverTime() {
		PlaybackClock clock = new PlaybackClock();
		int playedTicks = 0;
		for (int serverTick = 0; serverTick < 2000; serverTick++) {
			playedTicks += clock.advance(0.05, 31.86f);
		}

		assertEquals(3186, playedTicks);
	}

	@Test
	public void catchesUpAllSongTicksAfterDelayedServerTick() {
		PlaybackClock clock = new PlaybackClock();

		assertEquals(8, clock.advance(0.2, 40.0f));
	}

	@Test
	public void resetDiscardsPausedFractionWithoutSkippingFutureTicks() {
		PlaybackClock clock = new PlaybackClock();
		assertEquals(0, clock.advance(0.01, 31.86f));
		clock.reset();

		int playedTicks = 0;
		for (int serverTick = 0; serverTick < 20; serverTick++) {
			playedTicks += clock.advance(0.05, 20.0f);
		}
		assertEquals(20, playedTicks);
	}

	private void assertTicksPlayedAfterOneSecond(float tempo, int expectedTicks) {
		PlaybackClock clock = new PlaybackClock();
		int playedTicks = 0;
		for (int serverTick = 0; serverTick < 20; serverTick++) {
			playedTicks += clock.advance(0.05, tempo);
		}
		assertEquals(expectedTicks, playedTicks);
	}
}
