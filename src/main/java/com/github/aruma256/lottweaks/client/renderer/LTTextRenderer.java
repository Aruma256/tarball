package com.github.aruma256.lottweaks.client.renderer;

import static com.github.aruma256.lottweaks.client.ClientUtil.getClient;

import net.minecraft.client.gui.ScaledResolution;

public class LTTextRenderer {

	public static void showServerSideRequiredMessage(String requiredVersion) {
		showServerSideRequiredMessage(new ScaledResolution(getClient()), requiredVersion);
	}

	public static void showServerSideRequiredMessage(ScaledResolution scaledResolution, String requiredVersion) {
		showMessage(scaledResolution, String.format("[LotTweaks] Server-side installation (%s or later) is required.", requiredVersion), 0xFF9090);
	}

	public static void showMessage(ScaledResolution scaledResolution, String msg) {
		showMessage(scaledResolution, msg, 0xFFFFFF);
	}

	private static void showMessage(ScaledResolution scaledResolution, String msg, int color) {
		int x = (scaledResolution.getScaledWidth() - getClient().fontRenderer.getStringWidth(msg)) / 2;
		int y = scaledResolution.getScaledHeight() - 70;
		getClient().fontRenderer.drawStringWithShadow(msg, x, y, color);
	}

}
