package com.github.aruma256.lottweaks.client.selector;

import static com.github.aruma256.lottweaks.client.ClientUtil.getClient;

import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

public class HorizontalItemSelector extends AbstractItemSelector {

	public HorizontalItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	@Override
	public void render(ScaledResolution sr) {
		int cx = this.getCenterX(sr.getScaledWidth());
		int cy = sr.getScaledHeight() - 16 - 3;

		glItemRenderInitialize();
		int row = 0;
		for (ItemStack itemStack : stacks) {
			int y = cy - row * 20;
			getClient().getRenderItem().renderItemAndEffectIntoGUI(itemStack, cx, y);
			row++;
		}
		glItemRenderFinalize();
	}

	@Override
	protected void replaceInventory() {
		getClient().player.inventory.setInventorySlotContents(slot, stacks.get(0));
		getClient().playerController.sendSlotPacket(stacks.get(0), 36+slot);
	}

}
