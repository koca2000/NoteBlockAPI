package com.xxmicloxx.NoteBlockAPI.songplayer;

final class PlaybackClock {

	private static final double ROUNDING_EPSILON = 1.0e-9;
	private double pendingTicks;

	int advance(double elapsedSeconds, float ticksPerSecond) {
		if (elapsedSeconds <= 0 || ticksPerSecond <= 0) {
			return 0;
		}

		pendingTicks += elapsedSeconds * ticksPerSecond;
		int ticksToPlay = (int) Math.floor(pendingTicks + ROUNDING_EPSILON);
		pendingTicks -= ticksToPlay;
		return ticksToPlay;
	}

	void reset() {
		pendingTicks = 0;
	}
}
