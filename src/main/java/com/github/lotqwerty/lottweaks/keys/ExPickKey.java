package com.github.lotqwerty.lottweaks.keys;

import java.util.Deque;
import java.util.LinkedList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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
public class ExPickKey extends LTKeyBase {

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
	private final Deque<ItemStack> candidates = new LinkedList<>();

	public ExPickKey(int keyCode, String category) {
		super("Ex Pick", keyCode, category);
	}

	private void addToCandidates(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return;
		}
		for (ItemStack c: candidates) {
			if (ItemStack.areItemStacksEqual(c, itemStack)) {
				return;
			}
		}
		candidates.add(itemStack);
	}
	
	@Override
	public void onKeyPress() {
		if (this.pressTime != 1) {
			return;
		}
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
		candidates.add(itemStack);
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
		int wheel = event.getDwheel();
		if (wheel == 0) {
			return;
		}
		if (candidates.isEmpty()) {
			return;
		}
		if (wheel > 0) {
			candidates.addFirst(candidates.pollLast());
		}else {
			candidates.addLast(candidates.pollFirst());
		}
		Minecraft mc = Minecraft.getMinecraft();
		mc.player.inventory.setInventorySlotContents(mc.player.inventory.currentItem, candidates.getFirst());
        mc.playerController.sendSlotPacket(mc.player.getHeldItem(EnumHand.MAIN_HAND), 36 + mc.player.inventory.currentItem);
		event.setCanceled(true);
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
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = event.getResolution();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
        int i = 0;
		for (ItemStack c: candidates) {
			int x = sr.getScaledWidth() / 2 - 8;
			int y = sr.getScaledHeight() / 2 - 8;
			double max_r = 20 + candidates.size();
			double r = max_r * Math.tanh((this.pressTime + event.getPartialTicks()) / 6);
			double t = -((double)i) / candidates.size() * 2 * Math.PI + Math.PI / 2;
			double dx = r * Math.cos(t);
			double dy = r * Math.sin(t);
			mc.getRenderItem().renderItemAndEffectIntoGUI(c, (int)(x + dx), (int)(y + dy));
			i++;
		}
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
	}

	
}