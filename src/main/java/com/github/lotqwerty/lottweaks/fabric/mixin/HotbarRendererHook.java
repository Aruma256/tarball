package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public abstract class HotbarRendererHook {

	@Inject(at = @At("TAIL"), method = "renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V")
	private void lottweaks_renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		RenderHotbarEvent.post(matrices, tickDelta);
	}

}
