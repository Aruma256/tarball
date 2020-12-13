package com.github.lotqwerty.lottweaks.client.keys;

import java.util.List;

import com.github.lotqwerty.lottweaks.RotationHelper;
import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent.RenderHotbarListener;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent.ScrollListener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class RotateKey extends ItemSelectKeyBase implements ScrollListener, RenderHotbarListener {

	public RotateKey(int keyCode, String category) {
		super("Rotate", keyCode, category);
	}
	
	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		candidates.clear();
		MinecraftClient mc = MinecraftClient.getInstance();
		if (!mc.player.isCreative()) {
			return;
		}
		ItemStack itemStack = mc.player.inventory.getMainHandStack();
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
	public void onScroll(ScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		MinecraftClient mc = MinecraftClient.getInstance();
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

	@Override
	public void onRenderHotbar(RenderHotbarEvent event) {
//		if (event.getType() != ElementType.HOTBAR) {
//			return;
//		}
		if (this.pressTime == 0) {
			candidates.clear();
			return;
		}
		if (!MinecraftClient.getInstance().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		int x = event.getWindow().getScaledWidth() / 2 - 90 + MinecraftClient.getInstance().player.inventory.selectedSlot * 20 + 2;
		int y = event.getWindow().getScaledHeight() - 16 - 3;
		y -= 50 + (20 + candidates.size());
		LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
	}

}