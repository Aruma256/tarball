package com.github.lotqwerty.lottweaks.keys;

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

public class RotateMetaKey extends LTKeyBase {

	public RotateMetaKey(int keyCode, String category) {
		super("Rotate Meta", keyCode, category);
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
		} else {
			changeCurrentItemMeta(wheel > 0 ? -1 : 1);
		}
		event.setCanceled(true);
	}

	private static void changeCurrentItemMeta(int diff) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty() || !LotTweaks.isAllowedBlock(itemStack)) {
			return;
		}
		itemStack = itemStack.copy();
		LotTweaks.rotateMeta(itemStack, diff);
		mc.player.inventory.setInventorySlotContents(mc.player.inventory.currentItem, itemStack);
        mc.playerController.sendSlotPacket(mc.player.getHeldItem(EnumHand.MAIN_HAND), 36 + mc.player.inventory.currentItem);
	}

	@SubscribeEvent
	public void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HOTBAR || this.pressTime == 0 || !Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		if (itemStack.isEmpty() || !LotTweaks.isAllowedBlock(itemStack)) {
			return;
		}
		itemStack = itemStack.copy();
		int meta_range = LotTweaks.getRange(itemStack);
		ScaledResolution sr = event.getResolution();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
		for (int i=0; i<meta_range; i++) {
			int x = sr.getScaledWidth() / 2 - 90 + mc.player.inventory.currentItem * 20 + 2;
			int y = sr.getScaledHeight() - 16 - 3;
			double max_r = 20 + meta_range;
			double r = max_r * Math.tanh((this.pressTime + event.getPartialTicks()) / 6);
			y -= 50 + max_r;
			double t = -((double)i) / meta_range * 2 * Math.PI + Math.PI / 2;
			double dx = r * Math.cos(t);
			double dy = r * Math.sin(t);
			mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)(x + dx), (int)(y + dy));
			LotTweaks.rotateMeta(itemStack, 1);
		}
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
	}

}