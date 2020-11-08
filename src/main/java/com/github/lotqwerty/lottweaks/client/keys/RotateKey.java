package com.github.lotqwerty.lottweaks.client.keys;

import java.util.List;

import com.github.lotqwerty.lottweaks.RotationHelper;
import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RotateKey extends AbstractItemSelectKey {

	public RotateKey(int keyCode, String category) {
		super("Rotate", keyCode, category);
	}
	
	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		candidates.clear();
		Minecraft mc = Minecraft.getInstance();
		if (!mc.player.isCreative()) {
			return;
		}
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty()) {
			return;
		}
		List<ItemStack> results = RotationHelper.getAllRotateResult(itemStack);
		if (results == null || results.size() <= 1) {
			return;
		}
		candidates.addAll(results);
	}

	@Override
	protected void onKeyReleased() {
		candidates.clear();
	}

	@SubscribeEvent
	public void onMouseEvent(final InputEvent.MouseScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (!mc.player.isCreative()) {
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
		if (candidates.isEmpty()) {
			return;
		}
		if (wheel > 0) {
			this.rotateCandidatesForward();
		}else {
			this.rotateCandidatesBackward();
		}
		this.updateCurrentItemStack(candidates.getFirst());
	}

	@SubscribeEvent
	public void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HOTBAR) {
			return;
		}
		if (this.pressTime == 0) {
			candidates.clear();
			return;
		}
		if (!Minecraft.getInstance().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		int x = event.getWindow().getScaledWidth() / 2 - 90 + Minecraft.getInstance().player.inventory.currentItem * 20 + 2;
		int y = event.getWindow().getScaledHeight() - 16 - 3;
		y -= 50 + (20 + candidates.size());
		LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
	}

}