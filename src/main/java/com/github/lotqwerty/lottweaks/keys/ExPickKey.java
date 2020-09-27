package com.github.lotqwerty.lottweaks.keys;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.ForgeHooks;

public class ExPickKey extends LTKeyBase {

	public ExPickKey(int keyCode, String category) {
		super("Ex Pick", keyCode, category);
	}
	
	@Override
	public void onKeyPress() {
		if (this.pressTime != 1) {
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