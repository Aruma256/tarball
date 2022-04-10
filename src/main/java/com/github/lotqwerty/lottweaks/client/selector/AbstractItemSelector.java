package com.github.lotqwerty.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemSelector {

	protected List<ItemStack> stacks;
	protected final int slot;

	public AbstractItemSelector(List<ItemStack> stacks, int slot) {
		this.stacks = stacks;
		this.slot = slot;
	}

	public abstract void render(ScaledResolution sr);

	protected abstract void replaceInventory();

	protected int getCenterX(int width) {
		return width / 2 - 90 + slot * 20 + 2;
	}


	protected static void glItemRenderInitialize() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
	}
	
	protected static void glItemRenderFinalize() {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
	}
}
