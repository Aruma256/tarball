package com.github.aruma256.lottweaks.client.keys;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OpenPaletteKey extends LTKeyBase {

	private CircleItemSelector selector;

	public OpenPaletteKey(int keyCode, String category) {
		super("Open Palette", keyCode, category);
	}

	@Override
	protected int getMode() {
		if (LotTweaks.CONFIG.SNEAK_TO_SWITCH_GROUP) {
			return (!getClientPlayer().isSneaking()) ? 0 : 1;
		} else {
			return super.getMode() % ItemGroupManager.getSize();
		}
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		selector = null;
		//
		if (!isPlayerCreative()) {
			return;
		}
		//
		ItemStack itemStack = getClientPlayer().inventory.getCurrentItem();
		if (!itemStack.isEmpty()) {
			List<ItemStack> results = ItemGroupManager.getInstance(getMode()).getVariantsList(itemStack);
			if (results != null && results.size() > 1) {
				selector = new CircleItemSelector(results, getClientPlayer().inventory.currentItem);
				selector.overwriteSelectedIndex(searchIndexOfMatchedItem(results, itemStack));
			}
		}
	}

	private static int searchIndexOfMatchedItem(List<ItemStack> listedItems, ItemStack itemStack) {
		for (int i=0; i<listedItems.size(); i++) {
			if (ItemStack.areItemStacksEqual(itemStack, listedItems.get(i))) {
				return i;
			}
		}
		for (int i=0; i<listedItems.size(); i++) {
			if (ItemStack.areItemsEqual(itemStack, listedItems.get(i))) {
				return i;
			}
		}
		throw new RuntimeException();
	}

	@Override
	protected void onKeyReleased() {
		selector = null;
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
		if (flag && getClient().inGameHasFocus) {
			selector.notifyMouseMovement(Mouse.getDX(), Mouse.getDY());
		}
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!isPlayerCreative()) {
			return;
		}
		if (event.isCanceled()) {
			return;
		}
		int wheel = event.getDwheel();
		if (wheel == 0) {
			return;
		}
		event.setCanceled(true);
		if (selector == null) return; //TODO <- temporary NPE workaround
		selector.rotate(-wheel);
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
		if (selector != null) {
			selector.render(event.getResolution());
		}
	}

}