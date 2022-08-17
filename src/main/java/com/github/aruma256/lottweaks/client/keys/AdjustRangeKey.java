package com.github.aruma256.lottweaks.client.keys;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.LotTweaksClient;
import com.github.aruma256.lottweaks.client.renderer.LTTextRenderer;
import com.github.aruma256.lottweaks.network.LTPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class AdjustRangeKey extends LTKeyBase implements IGuiOverlay {

	public AdjustRangeKey(int keyCode, String category) {
		super("lottweaks-adjustrange", keyCode, category);
	}

	@Override
	public void render(ForgeGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		if (this.pressTime == 0) {
			return;
		}
		if (!Minecraft.getInstance().player.isCreative()) {
			return;
		}
		if (!LotTweaksClient.requireServerVersion("2.2.1")) {
			LTTextRenderer.showServerSideRequiredMessage(mStack, Minecraft.getInstance().getWindow(), "2.2.1");
			return;
		}
		// Update dist
		Minecraft mc = Minecraft.getInstance();
		HitResult rayTraceResult = mc.getCameraEntity().pick(255.0, mc.getFrameTime(), false);
		double dist;
		if (rayTraceResult == null || rayTraceResult.getType() == HitResult.Type.MISS) {
			dist = LotTweaks.CONFIG.MAX_RANGE.get();
		} else {
			dist = Math.min(LotTweaks.CONFIG.MAX_RANGE.get(), mc.player.getEyePosition(partialTicks).distanceTo(rayTraceResult.getLocation()));
		}
		LTPacketHandler.sendReachRangeMessage(dist);
		// Render
		int distInt = (int)dist;
		String distStr = String.valueOf(distInt);
		LTTextRenderer.showMessage(mStack, Minecraft.getInstance().getWindow(), distStr);
	}

}
