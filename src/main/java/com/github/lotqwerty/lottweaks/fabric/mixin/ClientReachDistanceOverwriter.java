package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;

@Mixin(MultiPlayerGameMode.class)
public abstract class ClientReachDistanceOverwriter {

	@ModifyConstant(method = "getPickRange()F", constant = @Constant(floatValue = 5.0F))
	private float lottweaks_getPickRange(float defaultValue) {
		return AdjustRangeKey.reachDistance;
	}

}
