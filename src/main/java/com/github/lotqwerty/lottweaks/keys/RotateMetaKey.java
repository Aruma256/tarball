package com.github.lotqwerty.lottweaks.keys;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RotateMetaKey extends LTKeyBase {

	private final Deque<ItemStack> candidates = new LinkedList<>();

	public RotateMetaKey(int keyCode, String category) {
		super("Rotate", keyCode, category);
	}
	
	@Override
	protected void onKeyPress() {
		if (this.pressTime != 1) {
			return;
		}
		candidates.clear();
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.capabilities.isCreativeMode) {
			return;
		}
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty()) {
			return;
		}
		List<ItemStack> results = LotTweaks.getAllRotateResult(itemStack);
		if (results == null || results.size() <= 1) {
			return;
		}
		candidates.addAll(results);
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
        int size = candidates.size();
		for (ItemStack c: candidates) {
			int x = sr.getScaledWidth() / 2 - 90 + mc.player.inventory.currentItem * 20 + 2;
			int y = sr.getScaledHeight() - 16 - 3;
			double max_r = 20 + size;
			double r = max_r * Math.tanh((this.pressTime + event.getPartialTicks()) / 6);
			y -= 50 + max_r;
			double t = -((double)i) / size * 2 * Math.PI + Math.PI / 2;
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