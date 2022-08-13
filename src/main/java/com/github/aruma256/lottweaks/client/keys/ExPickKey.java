package com.github.aruma256.lottweaks.client.keys;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;
import com.github.aruma256.lottweaks.client.selector.HorizontalItemSelector;

import net.java.games.input.Mouse;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.MouseDragEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
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

	private CircleItemSelector nearbyBlockSelector;
	private HorizontalItemSelector historyBlockSelector;

	public ExPickKey(int keyCode, String category) {
		super("Ex Pick", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		super.onKeyPressStart();
		nearbyBlockSelector = null;
		historyBlockSelector = null;
		RayTraceResult rayTraceResult;

		if (!isPlayerCreative()) {
			rayTraceResult = getClient().hitResult;
			if (rayTraceResult != null) {
				ForgeHooks.onPickBlock(rayTraceResult, getClientPlayer(), getClientWorld());
			}
			return;
		}
		if (getClientPlayer().isShiftKeyDown()) {
			historyModePick();
		} else {
			normalModePick();
		}
	}

	private void normalModePick() {
		RayTraceResult rayTraceResult = getClient().getCameraEntity().pick(255.0, getClient().getDeltaFrameTime(), false);
		if (rayTraceResult == null) {
			return;
		}
		boolean succeeded = ForgeHooks.onPickBlock(rayTraceResult, getClientPlayer(), getClientWorld());
		if (!succeeded) {
			return;
		}
		List<ItemStack> results = new ArrayList<>();
		results.add(getClientPlayer().inventory.getSelected());
		BlockPos pos = ((BlockRayTraceResult)rayTraceResult).getBlockPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				BlockPos targetPos = pos.offset(posDiff);
				BlockState state = getClientWorld().getBlockState(targetPos);
				ItemStack pickedItemStack = state.getBlock().getPickBlock(state, new BlockRayTraceResult(new Vector3d(targetPos.getX(), targetPos.getY(), targetPos.getZ()), ((BlockRayTraceResult)rayTraceResult).getDirection(), targetPos, false), getClientWorld(), targetPos, getClientPlayer());
				if (!pickedItemStack.isEmpty()) {
					boolean isUnique = true;
					for (ItemStack result : results) {
						if (ItemStack.matches(result, pickedItemStack)) {
							isUnique = false;
							break;
						}
					}
					if (isUnique) {
						results.add(pickedItemStack);
					}
				}
			} catch (Exception e) {
			}
		}
		if (results.size() > 1) {
			nearbyBlockSelector = new CircleItemSelector(results, getClientPlayer().inventory.selected);
		}
	}

	private void historyModePick() {
		if (!breakHistory.isEmpty()) {
			List<ItemStack> results = new LinkedList<>();
			results.add(getClientPlayer().inventory.getSelected());
			results.addAll(breakHistory);
			historyBlockSelector = new HorizontalItemSelector(results, getClientPlayer().inventory.selected);
		}
	}

	@Override
	protected void onKeyReleased() {
		super.onKeyReleased();
		nearbyBlockSelector = null;
		historyBlockSelector = null;
	}

	/*
	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		if (this.pressTime == 0) {
			return;
		}
		if (nearbyBlockSelector == null) {
			return;
		}
		boolean flag = Display.isActive(); // EntityRenderer#updateCameraAndRender
<<<<<<< HEAD
		if (flag && Minecraft.getMinecraft().inGameHasFocus) {
			MouseDragEvent e;
=======
		if (flag && getClient().inGameHasFocus) {
>>>>>>> origin/1.12.2
			nearbyBlockSelector.notifyMouseMovement(Mouse.getDX(), Mouse.getDY());
		}
	}
	*/

	@SubscribeEvent
	public void onMouseWheelEvent(final InputEvent.MouseScrollEvent event) {
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
		if (historyBlockSelector == null) {
			return;
		}
		historyBlockSelector.rotate(wheel > 0 ? 1 : -1);
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
		MainWindow sr = event.getWindow();
		if (nearbyBlockSelector != null) {
			nearbyBlockSelector.render(sr);
		}
		if (historyBlockSelector != null) {
			historyBlockSelector.render(sr);
		}
	}

	@SubscribeEvent
	public void onBreakBlock(final PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getWorld().isClientSide()) {
			return;
		}
		if (!event.getPlayer().isCreative()) {
			return;
		}
		//
		BlockState blockState = event.getWorld().getBlockState(event.getPos());
		ItemStack itemStack = blockState.getBlock().getPickBlock(blockState, getClient().hitResult, event.getWorld(), event.getPos(), event.getPlayer());
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