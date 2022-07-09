package com.github.aruma256.lottweaks.client.keys;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;
import com.github.aruma256.lottweaks.client.selector.ColumnItemSelector;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotateKey extends LTKeyBase {

	private CircleItemSelector selector;
	private List<ColumnItemSelector> rowSelectors;

	public RotateKey(int keyCode, String category) {
		super("Rotate", keyCode, category);
	}

	@Override
	protected int getMode() {
		if (LotTweaks.CONFIG.SNEAK_TO_SWITCH_GROUP) {
			return (!Minecraft.getMinecraft().player.isSneaking()) ? 0 : 1;
		} else {
			return super.getMode() % ItemGroupManager.getSize();
		}
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		selector = null;
		//
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.isCreative()) {
			return;
		}
		//
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (!itemStack.isEmpty()) {
			List<ItemStack> results = ItemGroupManager.getInstance(getMode()).getVariantsList(itemStack);
			if (results != null && results.size() > 1) {
				selector = new CircleItemSelector(results, mc.player.inventory.currentItem);
			}
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
		super.onKeyReleased();
		selector = null;
		rowSelectors = null;
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		if (this.pressTime == 0 || selector == null) {
			return;
		}
		boolean flag = Display.isActive(); // EntityRenderer#updateCameraAndRender
		if (flag && Minecraft.getMinecraft().inGameHasFocus) {
			selector.notifyMouseMovement(Mouse.getDX(), Mouse.getDY());
		}
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
		this.selector = null;
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
		if (selector != null) {
			selector.render(event.getResolution());
		}
		if (rowSelectors != null) {
			for (ColumnItemSelector selector : rowSelectors) {
				selector.render(event.getResolution());
			}
		}
	}

}