package com.github.lotqwerty.lotblocks.keys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class LongPickKey extends KeyBinding {

	public LongPickKey(int keyCode, String category) {
		super("Long Pick", keyCode, category);
	}

	@SubscribeEvent
	public void tick(final KeyInputEvent event) {
		if (!this.isPressed()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rayTraceResult;
		if (mc.player.capabilities.isCreativeMode) {
			rayTraceResult = mc.getRenderViewEntity().rayTrace(255.0, mc.getRenderPartialTicks());
		} else {
			rayTraceResult = mc.objectMouseOver;
		}
		if (rayTraceResult != null) {
			ForgeHooks.onPickBlock(rayTraceResult, mc.player, mc.world);
		}
	}
}