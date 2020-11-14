package com.github.lotqwerty.lottweaks.client.renderer;

import java.util.Collection;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public final class LTRenderer {

	public static void renderItemStacks(Collection<ItemStack> stacks, int x, int y, int t, float pt, int lt, byte direction) {
		if (stacks.isEmpty()) {
			return;
		}
		glInitialize();
		circular(stacks, x, y, t, pt, lt, direction);
		glFinalize();
	}

	private static void circular(Collection<ItemStack> stacks, int x, int y, int t, float pt, int lt, byte direction) {
		if (LotTweaks.CONFIG.DISABLE_ANIMATION) {
			t = Integer.MAX_VALUE;
			pt = 0;
		}
		double max_r = 20 + stacks.size() * 1.2;
		double r = max_r * Math.tanh((t + pt) / 6);
		double afterimage = 1 - Math.tanh((t + pt - lt)/1.5);
		//
		int i = 0;
		for (ItemStack c: stacks) {
			double theta = -((double)i - afterimage*direction) / stacks.size() * 2 * Math.PI + Math.PI / 2;
			double dx = r * Math.cos(theta);
			double dy = r * Math.sin(theta);
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
