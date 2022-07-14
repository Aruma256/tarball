package com.github.aruma256.lottweaks;

public class VersionComparator {

	public static boolean isClientNewerOrTheSame(String clientVersion, String serverVersion) {
		String[] clientVersionSplit = clientVersion.split("\\.");
		String[] serverVersionSplit = serverVersion.split("\\.");
		for (int i=0; i<3; i++) {
			int c = Integer.parseInt(clientVersionSplit[i], 16);
			int s;
			try {
				s = Integer.parseInt(serverVersionSplit[i], 16);
			} catch (Exception e) {
				s = 0;
			}
			if (c > s) return true;
			if (c < s) return false;
		}
		return true;
	}

}
