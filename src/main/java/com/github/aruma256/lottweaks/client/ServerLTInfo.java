package com.github.aruma256.lottweaks.client;

public class ServerLTInfo {

	public static final ServerLTInfo instance = new ServerLTInfo();

	private String serverLTVersion = "0";

	private ServerLTInfo() {}

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
