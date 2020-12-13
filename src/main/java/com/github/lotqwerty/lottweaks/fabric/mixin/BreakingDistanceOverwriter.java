package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.lotqwerty.lottweaks.fabric.ServerReachDistanceManager;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class BreakingDistanceOverwriter {
	
	@ModifyConstant(method = "processBlockBreakingAction(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;Lnet/minecraft/util/math/Direction;I)V", constant = @Constant(doubleValue = 36.0D))
	private double lottweaks_getReachDistance(double defaultValue) {
		ServerPlayerEntity player = ((ServerPlayerInteractionManager)((Object)this)).player;
		return ServerReachDistanceManager.getSquaredReachDistance(player, defaultValue);
	}

}
