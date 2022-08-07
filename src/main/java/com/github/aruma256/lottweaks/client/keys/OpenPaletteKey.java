package com.github.aruma256.lottweaks.client.keys;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;

import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class OpenPaletteKey extends LTKeyBase {

	private CircleItemSelector selector;

	public OpenPaletteKey(int keyCode, String category) {
		super("Open Palette", keyCode, category);
	}

	@Override
	protected int getMode() {
		if (LotTweaks.CONFIG.SNEAK_TO_SWITCH_GROUP.get()) {
			return (!getClientPlayer().isShiftKeyDown()) ? 0 : 1;
		} else {
			return super.getMode() % ItemGroupManager.getSize();
		}
	}

	@Override
	public boolean isDown() {
		return InputMappings.isKeyDown(getClient().getWindow().getWindow(), getKey().getValue());
	}

	@Override
	protected void onKeyPressStart() {
		GLFW.glfwSetCursorPosCallback(getClient().getWindow().getWindow(), new GLFWCursorPosCallbackI() {
			boolean isFirstCall = true;
			double prevX;
			double prevY;
			@Override
			public void invoke(long window, double x, double y) {
				if (isFirstCall) {
					isFirstCall = false;
					prevX = x;
					prevY = y;
					return;
				}
				if (selector != null) {
					selector.notifyMouseMovement(x - prevX, -(y - prevY));
				}
				prevX = x;
				prevY = y;
			}
		});
		super.onKeyPressStart();
		selector = null;
		//
		if (!isPlayerCreative()) {
			return;
		}
		//
		ItemStack itemStack = getClientPlayer().getMainHandItem();
		if (!itemStack.isEmpty()) {
			List<ItemStack> results = ItemGroupManager.getInstance(getMode()).getVariantsList(itemStack);
			if (results != null && results.size() > 1) {
				selector = new CircleItemSelector(results, getClientPlayer().inventory.selected);
				selector.overwriteSelectedIndex(searchIndexOfMatchedItem(results, itemStack));
			}
		}
	}

	private static int searchIndexOfMatchedItem(List<ItemStack> listedItems, ItemStack itemStack) {
		for (int i=0; i<listedItems.size(); i++) {
			if (ItemStack.matches(itemStack, listedItems.get(i))) {
				return i;
			}
		}
		for (int i=0; i<listedItems.size(); i++) {
			if (ItemStack.isSame(itemStack, listedItems.get(i))) {
				return i;
			}
		}
		throw new RuntimeException();
	}

	@Override
	protected void onKeyReleased() {
		selector = null;
		getClient().mouseHandler.setup(getClient().getWindow().getWindow());
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) throws Exception {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		if (this.pressTime == 0 || selector == null) {
			return;
		}
		if (!getClient().isPaused()) {
			double nx = getClient().mouseHandler.xpos();
			double ny = getClient().mouseHandler.ypos();
		}
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!isPlayerCreative()) {
			return;
		}
		if (event.isCanceled()) {
			return;
		}
		double wheel = event.getScrollDelta();
		if (wheel == 0) {
			return;
		}
		event.setCanceled(true);
		if (selector == null) return; //TODO <- temporary NPE workaround
		selector.rotate(wheel > 0 ? 1 : -1);
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
			selector.render(event.getWindow());
		}
	}

}