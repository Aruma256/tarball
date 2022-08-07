package com.github.aruma256.lottweaks.client;

import com.github.aruma256.lottweaks.VersionComparator;

public class CompatibilityChecker {

	public static final CompatibilityChecker instance = new CompatibilityChecker();
	private static final String SERVER_VERSION_UNKNOWN = "0.0.0";

	private String serverModVersion = SERVER_VERSION_UNKNOWN;

	private CompatibilityChecker() {}

	public void setServerModVersion(String version) {
		serverModVersion = version;
	}

	public String getServerModVersion() {
		return serverModVersion;
	}

	public void clearServerModVersion() {
		setServerModVersion(SERVER_VERSION_UNKNOWN);
	}

	public boolean isServerCompatibleWith(String requiredVersion) {
		return VersionComparator.isVersionNewerOrTheSame(serverModVersion, requiredVersion);
	}

}
