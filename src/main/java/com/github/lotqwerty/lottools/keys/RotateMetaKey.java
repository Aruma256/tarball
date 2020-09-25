package com.github.lotqwerty.lottools.keys;

import com.github.lotqwerty.lottools.LotTools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class RotateMetaKey extends KeyBinding {

	private long pressTime = 0;
	
	public RotateMetaKey(int keyCode, String category) {
		super("Rotate Meta", keyCode, category);
	}

	@SubscribeEvent
	public void onKeyInput(final KeyInputEvent event) {
		//キー押下をハンドル済みにする
	}

	@SubscribeEvent
	public void onClientTick(final ClientTickEvent event) {
		if (event.getPhase() == EventPriority.NORMAL) {
			if (this.isKeyDown()) {
				this.pressTime++;
			} else {
				this.pressTime = 0;
			}
		}
	}

	@SubscribeEvent
	public void onMouseEvent(final MouseEvent event) {
		if (this.pressTime <= 0) {
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
		if (itemStack.isEmpty() || !LotTools.isAllowedBlock(itemStack)) {
			return;
		}
		itemStack = itemStack.copy();
		LotTools.rotateMeta(itemStack, diff);
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
		if (itemStack.isEmpty() || !LotTools.isAllowedBlock(itemStack)) {
			return;
		}
		itemStack = itemStack.copy();
		int meta_range = LotTools.getRange(itemStack);
		ScaledResolution sr = event.getResolution();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
		for (int i=0; i<meta_range; i++) {
			
			
//			if (i >= this.pressTime) {
//				break;
//			}

			int x = sr.getScaledWidth() / 2 - 90 + mc.player.inventory.currentItem * 20 + 2;
			int y = sr.getScaledHeight() - 16 - 3;

//			double r = 20 + meta_range;
//			y -= 50 + r;

			double max_r = 20 + meta_range;
			double r = max_r * Math.tanh((this.pressTime + event.getPartialTicks()) / 6);
			y -= 50 + max_r;
			
			double t = -((double)i) / meta_range * 2 * Math.PI + Math.PI / 2;
			double dx = r * Math.cos(t);
			double dy = r * Math.sin(t);
			mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)(x + dx), (int)(y + dy));
			LotTools.rotateMeta(itemStack, 1);
		}

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();

		
	}
	


}