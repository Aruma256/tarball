package com.github.lotqwerty.lottweaks.client.keys;

import java.util.List;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.client.RotationHelper.Group;
import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotateKey extends ItemSelectKeyBase {

	private int phase = 0;

	public RotateKey(int keyCode, String category) {
		super("Rotate", keyCode, category);
	}

	private void updatePhase() {
		if (this.doubleTapTick == 0) {
			phase = 0;
		} else {
			phase ^= 1;
		}
	}

	private Group getGroup() {
		if (LotTweaks.CONFIG.SNEAK_TO_SWITCH_GROUP) {
			return (!Minecraft.getMinecraft().player.isSneaking()) ? Group.MAIN : Group.SUB;
		} else {
			return this.phase==0 ? Group.MAIN : Group.SUB;
		}
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		this.updatePhase();
		candidates.clear();
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.isCreative()) {
			return;
		}
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty()) {
			return;
		}
		List<ItemStack> results = RotationHelper.getAllRotateResult(itemStack, getGroup());
		if (results == null || results.size() <= 1) {
			return;
		}
		candidates.addAll(results);
	}

	@Override
	protected void onKeyReleased() {
		super.onKeyReleased();
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
		int wheel = event.getDwheel();
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
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		ScaledResolution sr = event.getResolution();
		int x = sr.getScaledWidth() / 2 - 90 + Minecraft.getMinecraft().player.inventory.currentItem * 20 + 2;
		int y = sr.getScaledHeight() - 16 - 3;
		y -= 50 + (20 + candidates.size());
		LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
	}

}