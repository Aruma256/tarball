package com.github.aruma256.lottweaks.client.keys;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.MouseDragEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class OpenPaletteKey extends LTKeyBase {

	private CircleItemSelector selector;

	public OpenPaletteKey(int keyCode, String category) {
		super("Open Palette", keyCode, category);
	}

	@Override
	protected int getMode() {
		if (LotTweaks.CONFIG.SNEAK_TO_SWITCH_GROUP.get()) {
			return (!Minecraft.getInstance().player.isShiftKeyDown()) ? 0 : 1;
		} else {
			return super.getMode() % ItemGroupManager.getSize();
		}
	}

	@Override
	public boolean isDown() {
		return InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), getKey().getValue());
	}

	@Override
	protected void onKeyPressStart() {
		GLFW.glfwSetCursorPosCallback(Minecraft.getInstance().getWindow().getWindow(), new GLFWCursorPosCallbackI() {
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
		Minecraft mc = Minecraft.getInstance();
		ItemStack itemStack = mc.player.getMainHandItem();
		if (!itemStack.isEmpty()) {
			List<ItemStack> results = ItemGroupManager.getInstance(getMode()).getVariantsList(itemStack);
			if (results != null && results.size() > 1) {
				selector = new CircleItemSelector(results, mc.player.inventory.selected);
				for (int i=0; i<results.size(); i++) {
					if (ItemStack.matches(itemStack, results.get(i))) {
						selector.overwriteSelectedIndex(i);
						return;
					}
				}
				for (int i=0; i<results.size(); i++) {
					if (ItemStack.isSame(itemStack, results.get(i))) {
						selector.overwriteSelectedIndex(i);
						return;
					}
				}
			}
		}
	}

	@Override
	protected void onKeyReleased() {
		selector = null;
		Minecraft.getInstance().mouseHandler.setup(Minecraft.getInstance().getWindow().getWindow());
	}

	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) throws Exception {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		if (this.pressTime == 0 || selector == null) {
			return;
		}
		if (!Minecraft.getInstance().isPaused()) {
			double nx = Minecraft.getInstance().mouseHandler.xpos();
			double ny = Minecraft.getInstance().mouseHandler.ypos();
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