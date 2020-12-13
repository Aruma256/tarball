package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent.RenderHotbarListener;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent.ScrollListener;
import com.github.lotqwerty.lottweaks.fabric.mixin.VanillaPickInvoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class ExPickKey extends ItemSelectKeyBase implements ScrollListener, RenderHotbarListener {

	private static final BlockPos[] SEARCH_POS = {
			new BlockPos(1, 0, 0),
			new BlockPos(-1, 0, 0),
			new BlockPos(0, 1, 0),
			new BlockPos(0, -1, 0),
			new BlockPos(0, 0, 1),
			new BlockPos(0, 0, -1),
			//
			new BlockPos(1, 1, 0),
			new BlockPos(1, -1, 0),
			new BlockPos(-1, 1, 0),
			new BlockPos(-1, -1, 0),
			new BlockPos(1, 0, 1),
			new BlockPos(1, 0, -1),
			new BlockPos(-1, 0, 1),
			new BlockPos(-1, 0, -1),
			new BlockPos(0, 1, 1),
			new BlockPos(0, 1, -1),
			new BlockPos(0, -1, 1),
			new BlockPos(0, -1, -1),
			};

	public ExPickKey(int keyCode, String category) {
		super("Ex Pick", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		candidates.clear();
		MinecraftClient mc = MinecraftClient.getInstance();
		HitResult rayTraceResult;
		if (!mc.player.isCreative()) {
			rayTraceResult = mc.crosshairTarget;
			if (rayTraceResult != null) {
				((VanillaPickInvoker)MinecraftClient.getInstance()).lottweaks_invokeVanillaItemPick();
			}
			return;
		}
		rayTraceResult = mc.getCameraEntity().raycast(255.0, mc.getTickDelta(), false);
		if (rayTraceResult == null) {
			return;
		}
//		boolean succeeded = ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
//		if (!succeeded) {
//			return;
//		}
		mc.crosshairTarget = rayTraceResult;
		((VanillaPickInvoker)MinecraftClient.getInstance()).lottweaks_invokeVanillaItemPick();
		ItemStack itemStack = mc.player.inventory.getMainHandStack();
		if (itemStack.isEmpty()) {
			return;
		}
		addToCandidates(itemStack);
		BlockPos pos = ((BlockHitResult)rayTraceResult).getBlockPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				BlockState state = mc.world.getBlockState(pos.add(posDiff));
				itemStack = state.getBlock().getPickStack(mc.world, pos, state);
				if (!itemStack.isEmpty()) {
					addToCandidates(itemStack);
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onScroll(ScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!MinecraftClient.getInstance().player.isCreative()) {
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
			return;
		}
		if (!MinecraftClient.getInstance().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		int x = event.getWindow().getScaledWidth() / 2 - 8;
		int y = event.getWindow().getScaledHeight() / 2 - 8;
		LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
	}
	
}