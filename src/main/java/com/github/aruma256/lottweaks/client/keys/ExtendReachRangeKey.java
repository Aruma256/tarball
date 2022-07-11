package com.github.aruma256.lottweaks.client.keys;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ServerLTInfo;
import com.github.aruma256.lottweaks.client.renderer.LTTextRenderer;
import com.github.aruma256.lottweaks.network.LTPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtendReachRangeKey extends LTKeyBase {

	public ExtendReachRangeKey(int keyCode, String category) {
		super("Extend Reach Range", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		if (ServerLTInfo.instance.requireServerLTVersion("2.2.1")) {
			LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_DEFAULT + LotTweaks.CONFIG.REACH_RANGE_AT_EXTENSION);
		}
	}

	@Override
	protected void onKeyReleased() {
		if (ServerLTInfo.instance.requireServerLTVersion("2.2.1")) {
			LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_DEFAULT);
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
		if (!ServerLTInfo.instance.requireServerLTVersion("3.0.0")) {
			LTTextRenderer.showServerSideRequiredMessage(event.getResolution(), "3.0.0");
			return;
		}
	}
}
