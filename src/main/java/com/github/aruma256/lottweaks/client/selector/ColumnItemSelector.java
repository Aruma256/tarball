package com.github.aruma256.lottweaks.client.selector;

import static com.github.aruma256.lottweaks.client.ClientUtil.getClient;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ColumnItemSelector extends HorizontalItemSelector {

	public ColumnItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	@Override
	protected void replaceInventory() {
		int row = 0;
		for (ItemStack itemStack : stacks) {
			if (row == 0) {
				getClient().player.inventory.setInventorySlotContents(slot, itemStack);
				getClient().playerController.sendSlotPacket(itemStack, 36+slot);
			} else {
				int slotId = slot + (4 - row) * InventoryPlayer.getHotbarSize();
				getClient().player.inventory.setInventorySlotContents(slotId, itemStack);
				getClient().playerController.sendSlotPacket(itemStack, slotId);
			}
			row++;
		}
	}

}
