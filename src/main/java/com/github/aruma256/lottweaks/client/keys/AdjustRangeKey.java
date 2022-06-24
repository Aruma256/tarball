package com.github.aruma256.lottweaks.client.keys;

import com.github.aruma256.lottweaks.client.LotTweaksClient;
import com.github.aruma256.lottweaks.client.renderer.LTTextRenderer;
import com.github.aruma256.lottweaks.network.LTPacketHandler;

import net.minecraft.client.Minecraft;
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

	@Override
	protected void onKeyPressStart() {
		if (LotTweaksClient.requireServerVersion("3.0.0")) {
			LTPacketHandler.sendReachExtensionMessage(20);
		}
	}

	@Override
	protected void onKeyReleased() {
		if (LotTweaksClient.requireServerVersion("3.0.0")) {
			LTPacketHandler.sendReachExtensionMessage(0);
		}
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
		if (!LotTweaksClient.requireServerVersion("3.0.0")) {
			LTTextRenderer.showServerSideRequiredMessage(event.getResolution(), "3.0.0");
			return;
		}
	}
}
