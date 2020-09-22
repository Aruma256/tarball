package com.github.lotqwerty.lotblocks.keys;

import com.github.lotqwerty.lotblocks.LotBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class RotateMetaKey extends KeyBinding {

	private boolean pressed = false;
	
	public RotateMetaKey(int keyCode, String category) {
		super("Rotate Meta", keyCode, category);
	}

	@SubscribeEvent
	public void onKeyInput(final KeyInputEvent event) {
		pressed = this.isPressed();
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseEvent event) {
		if (!pressed) {
			return;
		}
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		int wheel = event.getDwheel();
		if (wheel == 0) {
			return;
		} else {
			changeCurrentItemMeta(wheel > 0 ? -1 : 1);
		}
		event.setCanceled(true);
	}
	
	private static void changeCurrentItemMeta(int diff) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty()) {
			return;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (LotBlocks.META_RANGE.containsKey(block)) {
			int meta_range = LotBlocks.META_RANGE.get(block);
			itemStack = itemStack.copy();
			itemStack.setItemDamage((itemStack.getItemDamage() + diff + meta_range) % meta_range);
			mc.player.inventory.setInventorySlotContents(mc.player.inventory.currentItem, itemStack);
            mc.playerController.sendSlotPacket(mc.player.getHeldItem(EnumHand.MAIN_HAND), 36 + mc.player.inventory.currentItem);
		}
		
	}

}