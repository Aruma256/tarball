package com.github.lotqwerty.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class HorizontalItemSelector extends AbstractItemSelector {

	public HorizontalItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	public void rotate(int direction) {
		if (direction > 0) {
			stacks.add(stacks.remove(0));
		} else {
			stacks.add(0, stacks.remove(stacks.size() - 1));
		}
		this.replaceInventory();
	}

	@Override
	public void render(ScaledResolution sr) {
		int cx = this.getCenterX(sr.getScaledWidth());
		int cy = sr.getScaledHeight() - 16 - 3;

		glItemRenderInitialize();
		for (int row = 1; row < 4; row++) {
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stacks.get(row), cx, cy - row * 20);
		}
		glItemRenderFinalize();
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getMinecraft();
		int row = 0;
		for (ItemStack stack : stacks) {
			if (row == 0) {
				mc.player.inventory.setInventorySlotContents(slot, stack);
		        mc.playerController.sendSlotPacket(stack, 36+slot);
			} else {
				int slotId = slot + (4 - row) * InventoryPlayer.getHotbarSize();
				mc.player.inventory.setInventorySlotContents(slotId, stack);
		        mc.playerController.sendSlotPacket(stack, slotId);
			}
			row++;
		}
	}

}
