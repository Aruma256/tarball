package com.github.aruma256.lottweaks.client.renderer;

import static com.github.aruma256.lottweaks.client.ClientUtil.getClient;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;

public class LTTextRenderer {

	public static void showServerSideRequiredMessage(MatrixStack matrixStack, String requiredVersion) {
		showServerSideRequiredMessage(matrixStack, getClient().getWindow(), requiredVersion);
	}

	public static void showServerSideRequiredMessage(MatrixStack matrixStack, MainWindow scaledResolution, String requiredVersion) {
		showMessage(matrixStack, scaledResolution, String.format("[LotTweaks] Server-side installation (%s or later) is required.", requiredVersion), 0xFF9090);
	}

	public static void showMessage(MatrixStack matrixStack, MainWindow scaledResolution, String msg) {
		showMessage(matrixStack, scaledResolution, msg, 0xFFFFFF);
	}

	private static void showMessage(MatrixStack matrixStack, MainWindow scaledResolution, String msg, int color) {
		int x = (scaledResolution.getGuiScaledWidth() - getClient().font.width(msg)) / 2;
		int y = scaledResolution.getGuiScaledHeight() - 70;
		getClient().font.draw(matrixStack, msg, x, y, color);
	}

}
