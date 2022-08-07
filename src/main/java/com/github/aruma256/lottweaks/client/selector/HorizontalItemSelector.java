package com.github.aruma256.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class HorizontalItemSelector extends AbstractItemSelector {

	public HorizontalItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	@Override
	public void render(MainWindow sr) {
		int cx = this.getCenterX(sr.getGuiScaledWidth());
		int cy = sr.getGuiScaledHeight() - 16 - 3;

		glItemRenderInitialize();
		int row = 0;
		for (ItemStack itemStack : stacks) {
			int y = cy - row * 20;
			Minecraft.getInstance().getItemRenderer().renderGuiItem(itemStack, cx, y);
			row++;
		}
		glItemRenderFinalize();
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getInstance();
		mc.player.inventory.setItem(slot, stacks.get(0));
		mc.gameMode.handleCreativeModeItemAdd(stacks.get(0), 36+slot);
	}

}
