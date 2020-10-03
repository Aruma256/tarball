package com.github.lotqwerty.lottweaks.client.renderer;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public final class LTRenderer {

	public static void renderItemStacks(Collection<ItemStack> stacks, int x, int y, float rt) {
		if (stacks.isEmpty()) {
			return;
		}
		glInitialize();
		circular(stacks, x, y, rt);
		glFinalize();
	}

	private static void circular(Collection<ItemStack> stacks, int x, int y, float rt) {
		double max_r = 20 + stacks.size();
		double r = max_r * Math.tanh((rt) / 6);
		//
		int i = 0;
		for (ItemStack c: stacks) {
			double t = -((double)i) / stacks.size() * 2 * Math.PI + Math.PI / 2;
			double dx = r * Math.cos(t);
			double dy = r * Math.sin(t);
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(c, (int)Math.round(x + dx), (int)Math.round(y + dy));
			i++;
		}
	}

	private static void glInitialize() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
	}
	
	private static void glFinalize() {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
	}
	
}
