package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ExPickKey extends AbstractItemSelectKey {

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
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult rayTraceResult;

		if (!mc.player.isCreative()) {
			rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult != null) {
				ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
			}
			return;
		}
		rayTraceResult = mc.getRenderViewEntity().pick(255.0, mc.getRenderPartialTicks(), false);
		if (rayTraceResult == null) {
			return;
		}
		boolean succeeded = ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
		if (!succeeded) {
			return;
		}
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty()) {
			return;
		}
		addToCandidates(itemStack);
		BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				BlockState state = mc.world.getBlockState(pos.add(posDiff));
				itemStack = state.getBlock().getPickBlock(state, rayTraceResult, mc.world, pos, mc.player);
				if (!itemStack.isEmpty()) {
					addToCandidates(itemStack);
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onKeyReleased() {
		candidates.clear();
	}

	@SubscribeEvent
	public void onMouseWheelEvent(final InputEvent.MouseScrollEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!Minecraft.getInstance().player.isCreative()) {
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
			return;
		}
		if (!Minecraft.getInstance().player.isCreative()) {
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