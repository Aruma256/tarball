package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.client.renderer.LTRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExPickKey extends ItemSelectKeyBase {

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
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rayTraceResult;

		if (!mc.player.capabilities.isCreativeMode) {
			rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult != null) {
				ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
			}
			return;
		}
		rayTraceResult = mc.getRenderViewEntity().rayTrace(255.0, mc.getRenderPartialTicks());
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
		BlockPos pos = rayTraceResult.getBlockPos();
		for (BlockPos posDiff : SEARCH_POS) {
			try {
				IBlockState state = mc.world.getBlockState(pos.add(posDiff));
				itemStack = state.getBlock().getPickBlock(state, rayTraceResult, mc.world, pos, mc.player);
				if (!itemStack.isEmpty()) {
					addToCandidates(itemStack);
				}
			} catch (Exception e) {
			}
		}
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
		int x = sr.getScaledWidth() / 2 - 8;
		int y = sr.getScaledHeight() / 2 - 8;
		LTRenderer.renderItemStacks(candidates, x, y, pressTime, event.getPartialTicks(), lastRotateTime, rotateDirection);
	}
	
}