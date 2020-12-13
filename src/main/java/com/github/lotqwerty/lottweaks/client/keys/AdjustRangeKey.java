package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent.RenderHotbarListener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
public class AdjustRangeKey extends LTKeyBase implements RenderHotbarListener {

	private static final float MAX_RANGE = 128;
	public static float reachDistance = 6;
	
	public AdjustRangeKey(int keyCode, String category) {
		super("AdjustRange", keyCode, category);
	}

	@Override
	public void onRenderHotbar(RenderHotbarEvent event) {
//		if (event.getType() != ElementType.HOTBAR) {
//			return;
//		}
		if (this.pressTime == 0) {
			return;
		}
		if (!MinecraftClient.getInstance().player.isCreative()) {
			return;
		}
		// Update dist
		MinecraftClient mc = MinecraftClient.getInstance();
		HitResult rayTraceResult = mc.getCameraEntity().raycast(255.0, mc.getTickDelta(), false);
		double dist;
		if (rayTraceResult == null || rayTraceResult.getType() == HitResult.Type.MISS) {
//			dist = LotTweaks.CONFIG.MAX_RANGE.get();
			dist = MAX_RANGE;
		} else {
			dist = Math.min(MAX_RANGE, 0.5f + mc.player.getCameraPosVec(event.getPartialTicks()).distanceTo(rayTraceResult.getPos()));
		}
//		LTPacketHandler.sendReachRangeMessage(dist);
		reachDistance = (float) dist;
		// Render
		int distInt = (int)dist;
		String distStr = String.valueOf(distInt);
		int x = (event.getWindow().getScaledWidth() - mc.textRenderer.getWidth(distStr)) / 2;
		int y = event.getWindow().getScaledHeight() - 70;
		mc.textRenderer.drawWithShadow(event.getMatrixStack(), distStr, x, y, 0xFFFFFF);
	}
}
