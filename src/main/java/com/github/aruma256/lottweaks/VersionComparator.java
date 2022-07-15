package com.github.aruma256.lottweaks;

public class VersionComparator {

	public static boolean isVersionNewerOrTheSame(String testedVersion, String referenceVersion) {
		String[] testedVersionSplit = testedVersion.split("\\.");
		String[] referenceVersionSplit = referenceVersion.split("\\.");
		for (int i=0; i<3; i++) {
			int t = Integer.parseInt(testedVersionSplit[i], 16);
			int r;
			try {
				r = Integer.parseInt(referenceVersionSplit[i], 16);
			} catch (Exception e) {
				r = 0;
			}
			if (t > r) return true;
			if (t < r) return false;
		}
		return true;
	}

}
