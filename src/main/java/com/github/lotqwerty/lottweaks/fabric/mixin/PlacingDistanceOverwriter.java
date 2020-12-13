package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.lotqwerty.lottweaks.fabric.ServerReachDistanceManager;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class PlacingDistanceOverwriter {

	@ModifyConstant(method = "onPlayerInteractBlock(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V", constant = @Constant(doubleValue = 64.0D))
	private double lottweaks_getReachDistance(double defaultValue) {
		ServerPlayerEntity player = ((ServerPlayNetworkHandler)((Object)this)).player;
		return ServerReachDistanceManager.getSquaredReachDistance(player, defaultValue);
	}

}
