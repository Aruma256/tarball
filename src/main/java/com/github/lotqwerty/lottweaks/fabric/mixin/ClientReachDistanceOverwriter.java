package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientReachDistanceOverwriter {

	@Inject(method = "getReachDistance()F", at = @At("HEAD"), cancellable = true)
	private void lottweaks_getReachDistance(CallbackInfoReturnable<Float> cir) {
		if (MinecraftClient.getInstance().player.isCreative()) {
			cir.setReturnValue(AdjustRangeKey.reachDistance);
		}
	}

}
