package com.github.aruma256.lottweaks.client.selector;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.List;

import net.minecraft.client.MainWindow;
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
			getClient().getItemRenderer().renderGuiItem(itemStack, cx, y);
			row++;
		}
		glItemRenderFinalize();
	}

	@Override
	protected void replaceInventory() {
		getClientPlayer().inventory.setItem(slot, stacks.get(0));
		getClient().gameMode.handleCreativeModeItemAdd(stacks.get(0), 36+slot);
	}

}
