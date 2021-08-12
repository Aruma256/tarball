package com.github.lotqwerty.lottweaks.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

public class LTTextRenderer {

	public static void showServerSideRequiredMessage(MatrixStack matrixStack, String requiredVersion) {
		showServerSideRequiredMessage(matrixStack, Minecraft.getInstance().getMainWindow(), requiredVersion);
	}

	public static void showServerSideRequiredMessage(MatrixStack matrixStack, MainWindow scaledResolution, String requiredVersion) {
		showMessage(matrixStack, scaledResolution, String.format("[LotTweaks] Server-side installation (%s or later) is required.", requiredVersion), 0xFF9090);
	}

	public static void showMessage(MatrixStack matrixStack, MainWindow scaledResolution, String msg) {
		showMessage(matrixStack, scaledResolution, msg, 0xFFFFFF);
	}

	private static void showMessage(MatrixStack matrixStack, MainWindow scaledResolution, String msg, int color) {
		Minecraft mc = Minecraft.getInstance();
		int x = (scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth(msg)) / 2;
		int y = scaledResolution.getScaledHeight() - 70;
		mc.fontRenderer.drawStringWithShadow(matrixStack, msg, x, y, color);
	}

}
