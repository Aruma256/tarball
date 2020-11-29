package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class AdjustRangeKey extends LTKeyBase {

	public AdjustRangeKey(int keyCode, String category) {
		super("AdjustRange", keyCode, category);
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
		// Update dist
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult rayTraceResult = mc.getRenderViewEntity().pick(255.0, mc.getRenderPartialTicks(), false);
		double dist;
		if (rayTraceResult == null || rayTraceResult.getType() == RayTraceResult.Type.MISS) {
			dist = LotTweaks.CONFIG.MAX_RANGE.get();
		} else {
			dist = Math.min(LotTweaks.CONFIG.MAX_RANGE.get(), mc.player.getEyePosition(event.getPartialTicks()).distanceTo(rayTraceResult.getHitVec()));
		}
		LTPacketHandler.sendReachRangeMessage(dist);
		// Render
		int distInt = (int)dist;
		String distStr = String.valueOf(distInt);
		int x = (event.getWindow().getScaledWidth() - mc.fontRenderer.getStringWidth(distStr)) / 2;
		int y = event.getWindow().getScaledHeight() - 70;
		mc.fontRenderer.drawStringWithShadow(distStr, x, y, 0xFFFFFF);
	}
}
