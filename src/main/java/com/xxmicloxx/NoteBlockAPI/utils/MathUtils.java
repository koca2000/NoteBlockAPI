package com.xxmicloxx.NoteBlockAPI.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MathUtils {

	private static final double[] cos = new double[360];
	private static final double[] sin = new double[360];

	static {
		for (int deg = 0; deg < 360; deg++) {
			cos[deg] = Math.cos(Math.toRadians(deg));
			sin[deg] = Math.sin(Math.toRadians(deg));
		}
	}

	@NotNull
	public static Location stereoSourceLeft(@NotNull Location location, float distance) {
		return stereoPan(location, -distance);
	}

	@NotNull
	public static Location stereoSourceRight(@NotNull Location location, float distance) {
		return stereoPan(location, distance);
	}

	/**
	 * Calculate new location for stereo
	 * @param location origin location
	 * @param distance negative for left side, positive for right side
	 * @return
	 */
	public static Location stereoPan(@NotNull Location location, float distance){
		int angle = getAngle(location.getYaw());
		return location.clone().add( cos[angle] * distance, 0, sin[angle] * distance);
	}

	private static int getAngle(float yaw){
		int angle = (int) yaw;
		while (angle < 0) angle += 360;
		return angle % 360;
	}
	
}
