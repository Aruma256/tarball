package com.github.aruma256.lottweaks.client.keys;

import java.util.ArrayList;
import java.util.List;

import com.github.aruma256.lottweaks.client.selector.ColumnItemSelector;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RotateRowKey extends LTKeyBase {

	private List<ColumnItemSelector> rowSelectors;

	public RotateRowKey(int keyCode, String category) {
		super("RotateRow", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		if (!isPlayerCreative()) {
			return;
		}
		//
		Minecraft mc = Minecraft.getInstance();
		rowSelectors = new ArrayList<>();
		for (int slot=0; slot<PlayerInventory.getSelectionSize(); slot++) {
			List<ItemStack> stacksInColumn = new ArrayList<>();
			stacksInColumn.add(mc.player.inventory.getItem(slot));
			for (int row=3; row>=1; row--) {
				stacksInColumn.add(mc.player.inventory.getItem(slot + row * PlayerInventory.getSelectionSize()));
			}
			rowSelectors.add(new ColumnItemSelector(stacksInColumn, slot));
		}
	}

	@Override
	protected void onKeyReleased() {
		rowSelectors = null;
	}

	@SubscribeEvent
	public void onMouseEvent(final InputEvent.MouseScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!isPlayerCreative()) {
			return;
		}
		if (event.isCanceled()) {
			return;
		}
		if (rowSelectors == null) {
			return;
		}
		double wheel = event.getScrollDelta();
		event.setCanceled(true);
		for (int slot=0; slot<PlayerInventory.getSelectionSize(); slot++) {
			rowSelectors.get(slot).rotate(wheel > 0 ? 1 : -1);
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
		if (!isPlayerCreative()) {
			return;
		}
		if (rowSelectors != null) {
			for (ColumnItemSelector selector : rowSelectors) {
				selector.render(event.getWindow());
			}
		}
	}

}