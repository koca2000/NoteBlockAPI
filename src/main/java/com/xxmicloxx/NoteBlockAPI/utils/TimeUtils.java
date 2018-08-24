package com.xxmicloxx.NoteBlockAPI.utils;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

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
	 * 			<li>m for miliseconds (do not use without seconds, would be more than 4 digits)</li>
	 * 		</ul>
	 * @param songPlayer
	 * @return formatted string
	 */
	public static String getActualTime(String format, SongPlayer songPlayer){
		return getTime(format, songPlayer.getTick(), songPlayer.getSong().getSpeed());
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
	 * 			<li>m for miliseconds (do not use without seconds, would be more than 4 digits)</li>
	 * 		</ul>
	 * @param songPlayer
	 * @return formatted string
	 */
	public static String getLength(String format, SongPlayer songPlayer){
		return getTime(format, songPlayer.getSong().getLength(), songPlayer.getSong().getSpeed());
	}
	
	private static String getTime(String format, short ticks, float speed){
		String time = format;
		long milisTotal = (long) ((ticks / speed) * 1000);
		
		int hours = 0;
		if (time.contains("h")){
			hours = (int) Math.floor(milisTotal / 1000 / 60 / 60);
			milisTotal -= hours * 1000 * 60 * 60;
		}
		
		int minutes = 0;
		if (time.contains("m")){
			minutes = (int) Math.floor(milisTotal / 1000 / 60);
			milisTotal -= minutes * 1000 * 60;
		}
		
		int seconds = 0;
		if (time.contains("s")){
			seconds = (int) Math.floor(milisTotal / 1000);
			milisTotal -= seconds * 1000;
		}
		
		time = time.replace("hh", String.format("%02", hours));
		time = time.replace("h", hours + "");
		
		time = time.replace("mm", String.format("%02", minutes));
		time = time.replace("m", minutes + "");
		
		time = time.replace("ss", String.format("%02", seconds));
		time = time.replace("s", seconds + "");
		
		time = time.replace("n", milisTotal + "");
		
		return time;
	}
}
