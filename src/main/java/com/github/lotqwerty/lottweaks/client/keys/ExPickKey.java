package com.github.lotqwerty.lottweaks.client.keys;

import java.util.Deque;
import java.util.LinkedList;

import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExPickKey extends LTKeyBase {

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
		super("Ex Pick", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		candidates.clear();
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rayTraceResult;

		if (!mc.player.isCreative()) {
			rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult != null) {
				ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
			}
			return;
		}
		if (!mc.player.isSneaking()) {
			normalModePick();
		} else {
			historyModePick();
		}
	}

	private void normalModePick() {
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rayTraceResult = mc.getRenderViewEntity().rayTrace(255.0, mc.getRenderPartialTicks());
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
		addToCandidatesWithDedup(itemStack);
		BlockPos pos = rayTraceResult.getBlockPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				BlockPos targetPos = pos.add(posDiff);
				IBlockState state = mc.world.getBlockState(targetPos);
				itemStack = state.getBlock().getPickBlock(state, new RayTraceResult(new Vec3d(targetPos), rayTraceResult.sideHit, targetPos), mc.world, targetPos, mc.player);
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
			candidates.addFirst(Minecraft.getMinecraft().player.inventory.getCurrentItem());
			isHistoryMode = true;
		}
	}

	@Override
	protected void onKeyReleased() {
		super.onKeyReleased();
		isHistoryMode = false;
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (!Minecraft.getMinecraft().player.isCreative()) {
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
			return;
		}
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		ScaledResolution sr = event.getResolution();
		if (!isHistoryMode) {
			int x = sr.getScaledWidth() / 2 - 8;
			int y = sr.getScaledHeight() / 2 - 8;
			LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
		} else {
			int x = sr.getScaledWidth() / 2 - 90 + Minecraft.getMinecraft().player.inventory.currentItem * 20 + 2;
			int y = sr.getScaledHeight() - 16 - 3;
			LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(final PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getWorld().isRemote) {
			return;
		}
		if (!event.getEntityPlayer().isCreative()) {
			return;
		}
		//
		Minecraft mc = Minecraft.getMinecraft();
		IBlockState blockState = event.getWorld().getBlockState(event.getPos());
		ItemStack itemStack = blockState.getBlock().getPickBlock(blockState, mc.objectMouseOver, event.getWorld(), event.getPos(), event.getEntityPlayer());
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
			if (!ItemStack.areItemStacksEqual(tmpHistory.peekFirst(), itemStack)) {
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