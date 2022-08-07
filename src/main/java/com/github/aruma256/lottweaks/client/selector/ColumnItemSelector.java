package com.github.aruma256.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class ColumnItemSelector extends HorizontalItemSelector {

	public ColumnItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getInstance();
		int row = 0;
		for (ItemStack itemStack : stacks) {
			if (row == 0) {
				mc.player.inventory.setItem(slot, itemStack);
				Minecraft.getInstance().gameMode.handleCreativeModeItemAdd(itemStack, 36+slot);
			} else {
				int slotId = slot + (4 - row) * PlayerInventory.getSelectionSize();
				mc.player.inventory.setItem(slotId, itemStack);
				Minecraft.getInstance().gameMode.handleCreativeModeItemAdd(itemStack, slotId);
			}
			row++;
		}
	}

}
