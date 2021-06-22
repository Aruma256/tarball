package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.client.renderer.LTTextRenderer;
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
		if (!LotTweaksClient.requireServerVersion("2.2.1")) {
			LTTextRenderer.showServerSideRequiredMessage(event.getResolution(), "2.2.1");
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
		LTTextRenderer.showMessage(event.getResolution(), distStr);
	}
}
