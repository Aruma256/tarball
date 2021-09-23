package com.github.lotqwerty.lottweaks.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public class LTTextRenderer {

	public static void showServerSideRequiredMessage(PoseStack matrixStack, String requiredVersion) {
		showServerSideRequiredMessage(matrixStack, Minecraft.getInstance().getWindow(), requiredVersion);
	}

	public static void showServerSideRequiredMessage(PoseStack matrixStack, Window scaledResolution, String requiredVersion) {
		showMessage(matrixStack, scaledResolution, String.format("[LotTweaks] Server-side installation (%s or later) is required.", requiredVersion), 0xFF9090);
	}

	public static void showMessage(PoseStack matrixStack, Window scaledResolution, String msg) {
		showMessage(matrixStack, scaledResolution, msg, 0xFFFFFF);
	}

	private static void showMessage(PoseStack matrixStack, Window scaledResolution, String msg, int color) {
		Minecraft mc = Minecraft.getInstance();
		int x = (scaledResolution.getGuiScaledWidth() - mc.font.width(msg)) / 2;
		int y = scaledResolution.getGuiScaledHeight() - 70;
		mc.font.drawShadow(matrixStack, msg, x, y, color);
	}

}
