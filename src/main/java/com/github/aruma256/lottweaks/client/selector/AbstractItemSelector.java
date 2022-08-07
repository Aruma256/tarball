package com.github.aruma256.lottweaks.client.selector;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemSelector {

	protected List<ItemStack> stacks;
	protected final int slot;

	public AbstractItemSelector(List<ItemStack> stacks, int slot) {
		this.stacks = stacks;
		this.slot = slot;
	}

	public abstract void render(MainWindow sr);

	protected abstract void replaceInventory();

	protected int getCenterX(int width) {
		if (slot >= 0) {
			return width / 2 - 90 + slot * 20 + 2;
		} else {
			return width / 2;
		}
	}

	public void rotate(int direction) {
		if (direction > 0) {
			stacks.add(stacks.remove(0));
		} else {
			stacks.add(0, stacks.remove(stacks.size() - 1));
		}
		this.replaceInventory();
	}

	protected static void glItemRenderInitialize() {
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
	}
	
	protected static void glItemRenderFinalize() {
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableBlend();
	}
}
