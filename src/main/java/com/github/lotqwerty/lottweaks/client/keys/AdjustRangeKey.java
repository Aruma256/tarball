package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		// Update dist
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rayTraceResult = mc.getRenderViewEntity().rayTrace(255.0, event.getPartialTicks());
		double dist;
		if (rayTraceResult == null || rayTraceResult.typeOfHit == RayTraceResult.Type.MISS) {
			dist = LotTweaks.CONFIG.MAX_RANGE;
		} else {
			dist = Math.min(LotTweaks.CONFIG.MAX_RANGE, mc.player.getPositionEyes(event.getPartialTicks()).distanceTo(rayTraceResult.hitVec));
		}
		LTPacketHandler.sendReachRangeMessage(dist);
		// Render
		int distInt = (int)dist;
		String distStr = String.valueOf(distInt);
		int x = (event.getResolution().getScaledWidth() - mc.fontRenderer.getStringWidth(distStr)) / 2;
		int y = event.getResolution().getScaledHeight() - 70;
		mc.fontRenderer.drawStringWithShadow(distStr, x, y, 0xFFFFFF);
	}
}
