package com.xxmicloxx.NoteBlockAPI.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
	
	public static boolean checkUpdate(String resource, String actualVersion) throws MalformedURLException, IOException{		
		Float version = getVersionNumber(actualVersion);
		
		URLConnection con = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openConnection();
		String newVersionString = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		int first = newVersionString.indexOf("(");
		String newVersion = newVersionString.substring(0, first);
		
		Float newVer = getVersionNumber(newVersion);

		return newVer > version;
	}
	
	private static Float getVersionNumber(String version){
		String[] versionParts = version.split("\\.");
		String versionString = "0.";
		
		for (String vpart : versionParts){
			if (vpart.length() < 2){
				versionString += "0";
			}
			versionString += vpart;
		}
		
		return Float.parseFloat(versionString);
	}
	
}
