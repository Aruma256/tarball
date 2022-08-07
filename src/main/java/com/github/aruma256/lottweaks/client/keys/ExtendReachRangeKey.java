package com.github.aruma256.lottweaks.client.keys;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.CompatibilityChecker;
import com.github.aruma256.lottweaks.client.renderer.LTTextRenderer;
import com.github.aruma256.lottweaks.network.LTPacketHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ExtendReachRangeKey extends LTKeyBase {

	public ExtendReachRangeKey(int keyCode, String category) {
		super("Extend Reach Range", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		if (CompatibilityChecker.instance.isServerCompatibleWith("2.2.1")) {
			LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_EXTENSION.get());
		}
	}

	@Override
	protected void onKeyReleased() {
		if (CompatibilityChecker.instance.isServerCompatibleWith("2.2.1")) {
			LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_DEFAULT.get());
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
		if (!isPlayerCreative()) {
			return;
		}
		if (!CompatibilityChecker.instance.isServerCompatibleWith("2.2.1")) {
			LTTextRenderer.showServerSideRequiredMessage(event.getMatrixStack(), "2.2.1");
			return;
		}
	}
}
