package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.NotNull;

public class TimeUtils {

	/**
	 * Returns {@link SongPlayer} song actual time in specified format
	 * @param format use:
	 * 		<ul>
	 * 			<li>hh for two-digit hours</li>
	 * 			<li>h for one-digit hours</li>
	 * 			<li>mm for two-digit minutes</li>
	 * 			<li>m for one-digit minutes</li>
	 *			<li>ss for two-digit seconds</li>
	 * 			<li>s for one-digit seconds</li>
	 * 			<li>m for milliseconds (do not use without seconds, would be more than 4 digits)</li>
	 * 		</ul>
	 * @param songPlayer Instance of SongPlayer to use
	 * @return formatted string
	 */
	@NotNull
	public static String getActualTime(@NotNull String format, @NotNull SongPlayer songPlayer){
		return getTime(format, songPlayer.getPlayingSong().getTimeInSecondsAtTick(songPlayer.getTick()));
	}
	
	/**
	 * Returns {@link SongPlayer} song length in specified format
	 * @param format use:
	 * 		<ul>
	 * 			<li>hh for two-digit hours</li>
	 * 			<li>h for one-digit hours</li>
	 * 			<li>mm for two-digit minutes</li>
	 * 			<li>m for one-digit minutes</li>
	 *			<li>ss for two-digit seconds</li>
	 * 			<li>s for one-digit seconds</li>
	 * 			<li>m for milliseconds (do not use without seconds, would be more than 4 digits)</li>
	 * 		</ul>
	 * @param songPlayer Instance of SongPlayer to use
	 * @return formatted string
	 */
	@NotNull
	public static String getLength(@NotNull String format, @NotNull SongPlayer songPlayer){
		return getTime(format, songPlayer.getPlayingSong().getSongLengthInSeconds());
	}

	@NotNull
	private static String getTime(@NotNull String format, double timeInSeconds){
		String time = format;
		long millisTotal = Math.round(timeInSeconds * 1000);
		
		long hours = 0;
		if (format.contains("h")){
			hours = millisTotal / (1000 * 60 * 60);
			millisTotal -= hours * 1000 * 60 * 60;
		}
		
		long minutes = 0;
		if (format.contains("m")){
			minutes = millisTotal / (1000 * 60);
			millisTotal -= minutes * 1000 * 60;
		}
		
		long seconds = 0;
		if (format.contains("s")){
			seconds = millisTotal / 1000;
			millisTotal -= seconds * 1000;
		}
		
		time = time.replace("hh", String.format("%02d", hours));
		time = time.replace("h", hours + "");
		
		time = time.replace("mm", String.format("%02d", minutes));
		time = time.replace("m", minutes + "");
		
		time = time.replace("ss", String.format("%02d", seconds));
		time = time.replace("s", seconds + "");
		
		time = time.replace("n", millisTotal + "");
		
		return time;
	}
}
