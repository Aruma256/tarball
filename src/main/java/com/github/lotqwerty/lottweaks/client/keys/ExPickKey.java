package com.github.lotqwerty.lottweaks.client.keys;

import java.util.Deque;
import java.util.LinkedList;

import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ExPickKey extends ItemSelectKeyBase implements IGuiOverlay {

	private static final int HISTORY_SIZE = 10;

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

	private static final Deque<ItemStack> breakHistory = new LinkedList<>();

	private boolean isHistoryMode = false;

	public ExPickKey(int keyCode, String category) {
		super("lottweaks-expick", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		candidates.clear();
		Minecraft mc = Minecraft.getInstance();
		HitResult rayTraceResult;

		if (!mc.player.isCreative()) {
			rayTraceResult = mc.hitResult;
			if (rayTraceResult != null) {
				ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.level);
			}
			return;
		}
		if (!mc.player.isShiftKeyDown()) {
			normalModePick();
		} else {
			historyModePick();
		}
	}

	private void normalModePick() {
		Minecraft mc = Minecraft.getInstance();
		HitResult rayTraceResult = mc.getCameraEntity().pick(255.0, mc.getFrameTime(), false);
		if (rayTraceResult == null) {
			return;
		}
		boolean succeeded = ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.level);
		if (!succeeded) {
			return;
		}
		ItemStack itemStack = mc.player.getInventory().getSelected();
		if (itemStack.isEmpty()) {
			return;
		}
		addToCandidatesWithDedup(itemStack);
		BlockPos pos = ((BlockHitResult)rayTraceResult).getBlockPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				BlockState state = mc.level.getBlockState(pos.offset(posDiff));
				itemStack = state.getBlock().getCloneItemStack(state, rayTraceResult, mc.level, pos, mc.player);
				if (!itemStack.isEmpty()) {
					addToCandidatesWithDedup(itemStack);
				}
			} catch (Exception e) {
			}
		}
	}

	private void historyModePick() {
		if (!breakHistory.isEmpty()) {
			candidates.addAll(breakHistory);
			candidates.addFirst(Minecraft.getInstance().player.getInventory().getSelected());
			isHistoryMode = true;
		}
	}

	@Override
	protected void onKeyReleased() {
		super.onKeyReleased();
		isHistoryMode = false;
	}

	@SubscribeEvent
	public void onMouseWheelEvent(final InputEvent.MouseScrollingEvent event) {
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

	@Override
	public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (this.pressTime == 0) {
			return;
		}
		if (!Minecraft.getInstance().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		if (!isHistoryMode) {
			int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 8;
			int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 8;
			LTRenderer.renderItemStacks(candidates, x, y, pressTime, partialTicks, lastRotateTime, rotateDirection);
		} else {
			int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 90 + Minecraft.getInstance().player.getInventory().selected * 20 + 2;
			int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 16 - 3;
			LTRenderer.renderItemStacks(candidates, x, y, pressTime, partialTicks, lastRotateTime, rotateDirection, LTRenderer.RenderMode.LINE);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(final PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getLevel().isClientSide) {
			return;
		}
		if (!event.getEntity().isCreative()) {
			return;
		}
		//
		Minecraft mc = Minecraft.getInstance();
		BlockState blockState = event.getLevel().getBlockState(event.getPos());
		ItemStack itemStack = blockState.getBlock().getCloneItemStack(blockState, mc.hitResult, event.getLevel(), event.getPos(), event.getEntity());
		addToHistory(itemStack);
	}

	protected static void addToHistory(ItemStack itemStack) {
		if (itemStack == null || itemStack.isEmpty()) {
			return;
		}
		Deque<ItemStack> tmpHistory = new LinkedList<>();
		tmpHistory.addAll(breakHistory);
		breakHistory.clear();
		while(!tmpHistory.isEmpty()) {
			if (!ItemStack.matches(tmpHistory.peekFirst(), itemStack)) {
				breakHistory.add(tmpHistory.pollFirst());
			} else {
				tmpHistory.removeFirst();
			}
		}
		while (breakHistory.size() >= HISTORY_SIZE) {
			breakHistory.pollLast();
		}
		breakHistory.addFirst(itemStack);
	}

}