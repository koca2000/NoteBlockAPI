package com.xxmicloxx.NoteBlockAPI.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

	/**
	 * Checks Spigot forums for plugin update
	 * @param resource Resource ID
	 * @param actualVersion Textual representation of currently installed version
	 * @return string|null Returns textual representation of the new version if available. Otherwise returns null
	 * @throws IOException if the connection fails
	 */
	public static String checkUpdate(String resource, String actualVersion) throws IOException{
		boolean snapshot = false;
		if (actualVersion.contains("-SNAPSHOT")){
			snapshot = true;
			actualVersion = actualVersion.replace("-SNAPSHOT","");
		}

		int version = getVersionNumber(actualVersion);
		
		URLConnection con = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openConnection();
		String newVersionString = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		int first = newVersionString.indexOf("(");
		String newVersion = first == -1 ? newVersionString : newVersionString.substring(0, first);

		int newVer = getVersionNumber(newVersion);

		return (snapshot && newVer >= version) || newVer > version ? newVersion : null;
	}
	
	private static int getVersionNumber(String version){
		String[] versionParts = version.split("\\.");

		int versionNumber = 0;
		for (int i = 0; i < 4; i++){
			versionNumber *= 100;
			if (i < versionParts.length)
				versionNumber += Integer.parseInt(versionParts[i]);
		}
		return versionNumber;
	}
	
}
