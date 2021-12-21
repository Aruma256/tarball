package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.AdjustRangeHelper;
import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.client.renderer.LTTextRenderer;
import com.github.lotqwerty.lottweaks.client.renderer.SelectionBoxRenderer;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ExtendRangeKey extends LTKeyBase {

	public ExtendRangeKey(int keyCode, String category) {
		super("ExtendRange", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		LTPacketHandler.sendExtendRangeMessage(128);
	}

	@Override
	protected void onKeyReleased() {
		LTPacketHandler.sendExtendRangeMessage(0);
	}

	@SubscribeEvent
	public void onRenderOverlay(final RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.HOTBAR) {
			return;
		}
		if (!Minecraft.getMinecraft().player.isCreative()) {
			return;
		}
		if (!LotTweaksClient.requireServerVersion("2.2.4")) {
			if (this.pressTime > 0) {
				LTTextRenderer.showServerSideRequiredMessage(event.getResolution(), "2.2.4");
			}
			return;
		}
		//
		boolean pressed = (this.pressTime > 0);
		boolean activated = AdjustRangeHelper.isExtendRangeActivated(Minecraft.getMinecraft().player);
		if (pressed && !activated) {
			LTTextRenderer.showMessage(event.getResolution(), "switching...");
		} else if (!pressed && activated) {
			LTTextRenderer.showMessage(event.getResolution(), "switching...");
		}
	}

	@SubscribeEvent
	public void onDrawBlockHighlightEvent(final DrawBlockHighlightEvent event) {
		if (!LotTweaksClient.requireServerVersion("2.2.4")) {
			return;
		}
		if (!AdjustRangeHelper.isExtendRangeActivated(Minecraft.getMinecraft().player)) {
			return;
		}
		RayTraceResult target = Minecraft.getMinecraft().objectMouseOver;
		if (target != null && target.typeOfHit == RayTraceResult.Type.BLOCK){
			if (SelectionBoxRenderer.render(target.getBlockPos(), event.getPartialTicks(), 1f, 0f, 0f)) {
				event.setCanceled(true);
			}
		}
	}

}
