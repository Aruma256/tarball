package com.github.aruma256.lottweaks.client.selector;

import static com.github.aruma256.lottweaks.CommonUtil.*;
import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.List;

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
				getClientPlayer().inventory.setItem(slot, itemStack);
				getClient().gameMode.handleCreativeModeItemAdd(itemStack, 36+slot);
			} else {
				int slotId = slot + (4 - row) * HOTBAR_SIZE;
				getClientPlayer().inventory.setItem(slotId, itemStack);
				getClient().gameMode.handleCreativeModeItemAdd(itemStack, slotId);
			}
			row++;
		}
	}

}
