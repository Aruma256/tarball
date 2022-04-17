package com.github.lotqwerty.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ColumnItemSelector extends HorizontalItemSelector {

	public ColumnItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getMinecraft();
		int row = 0;
		for (ItemStack itemStack : stacks) {
			if (row == 0) {
				mc.player.inventory.setInventorySlotContents(slot, itemStack);
		        mc.playerController.sendSlotPacket(itemStack, 36+slot);
			} else {
				int slotId = slot + (4 - row) * InventoryPlayer.getHotbarSize();
				mc.player.inventory.setInventorySlotContents(slotId, itemStack);
		        mc.playerController.sendSlotPacket(itemStack, slotId);
			}
			row++;
		}
	}

}
