package com.github.lotqwerty.lottweaks.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class LTTextRenderer {

	public static void showServerSideRequiredMessage(String requiredVersion) {
		showServerSideRequiredMessage(new ScaledResolution(Minecraft.getMinecraft()), requiredVersion);
	}

	public static void showServerSideRequiredMessage(ScaledResolution scaledResolution, String requiredVersion) {
		showMessage(scaledResolution, String.format("[LotTweaks] Server-side installation (%s or later) is required.", requiredVersion), 0xFF9090);
	}

	public static void showMessage(ScaledResolution scaledResolution, String msg) {
		showMessage(scaledResolution, msg, 0xFFFFFF);
	}

	private static void showMessage(ScaledResolution scaledResolution, String msg, int color) {
		Minecraft mc = Minecraft.getMinecraft();
		int x = (scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth(msg)) / 2;
		int y = scaledResolution.getScaledHeight() - 70;
		mc.fontRenderer.drawStringWithShadow(msg, x, y, color);
	}

}
