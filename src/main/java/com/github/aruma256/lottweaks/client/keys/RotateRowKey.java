package com.github.aruma256.lottweaks.client.keys;

import java.util.ArrayList;
import java.util.List;

import com.github.aruma256.lottweaks.client.selector.ColumnItemSelector;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotateRowKey extends LTKeyBase {

	private List<ColumnItemSelector> rowSelectors;

	public RotateRowKey(int keyCode, String category) {
		super("RotateRow", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.isCreative()) {
			return;
		}
		//
		rowSelectors = new ArrayList<>();
		for (int slot=0; slot<InventoryPlayer.getHotbarSize(); slot++) {
			List<ItemStack> stacksInColumn = new ArrayList<>();
			stacksInColumn.add(mc.player.inventory.getStackInSlot(slot));
			for (int row=3; row>=1; row--) {
				stacksInColumn.add(mc.player.inventory.getStackInSlot(slot + row * InventoryPlayer.getHotbarSize()));
			}
			rowSelectors.add(new ColumnItemSelector(stacksInColumn, slot));
		}
	}

	@Override
	protected void onKeyReleased() {
		rowSelectors = null;
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.isCreative()) {
			return;
		}
		if (event.isCanceled()) {
			return;
		}
		if (rowSelectors == null) {
			return;
		}
		int wheel = event.getDwheel();
		if (wheel == 0) {
			return;
		}
		event.setCanceled(true);
		for (int slot=0; slot<InventoryPlayer.getHotbarSize(); slot++) {
			rowSelectors.get(slot).rotate(wheel);
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HOTBAR) {
			return;
		}
		if (this.pressTime == 0) {
			return;
		}
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		if (rowSelectors != null) {
			for (ColumnItemSelector selector : rowSelectors) {
				selector.render(event.getResolution());
			}
		}
	}

}