package com.github.aruma256.lottweaks.client;

public class CompatibilityChecker {

	public static final CompatibilityChecker instance = new CompatibilityChecker();

	private String serverLTVersion = "0";

	private CompatibilityChecker() {}

	public void setServerLTVersion(String version) {
		serverLTVersion = version;
	}

	public String getServerLTVersion() {
		return serverLTVersion;
	}

	public void clearServerLTVersion() {
		setServerLTVersion("0");
	}

	public boolean requireServerLTVersion(String requiredVersion) {
		return (serverLTVersion.compareTo(requiredVersion) >= 0);
	}

}
