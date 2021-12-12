package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.lotqwerty.lottweaks.fabric.ServerReachDistanceManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class PlacingDistanceOverwriter {

	@ModifyConstant(method = "handleUseItemOn(Lnet/minecraft/network/protocol/game/ServerboundUseItemOnPacket;)V", constant = @Constant(doubleValue = 64.0D))
	private double lottweaks_handleUseItemOn(double defaultValue) {
		ServerPlayer player = ((ServerGamePacketListenerImpl)((Object)this)).player;
		return ServerReachDistanceManager.getSquaredReachDistance(player, defaultValue);
	}

}
