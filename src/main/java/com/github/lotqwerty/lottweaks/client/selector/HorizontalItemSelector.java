package com.github.lotqwerty.lottweaks.client.selector;

import java.util.List;

import net.minecraft.client.Minecraft;
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
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, cx, y);
			row++;
		}
		glItemRenderFinalize();
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.player.inventory.setInventorySlotContents(slot, stacks.get(0));
        mc.playerController.sendSlotPacket(stacks.get(0), 36+slot);
	}

}
