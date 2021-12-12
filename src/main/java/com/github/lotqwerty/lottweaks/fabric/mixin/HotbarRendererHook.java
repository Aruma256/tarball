package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public abstract class HotbarRendererHook {

	@Inject(at = @At("TAIL"), method = "renderHotbar(FLcom/mojang/blaze3d/vertex/PoseStack;)V")
	private void lottweaks_renderHotbar(float tickDelta, PoseStack matrices, CallbackInfo info) {
		RenderHotbarEvent.post(matrices, tickDelta);
	}

}
