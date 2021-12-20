package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.github.lotqwerty.lottweaks.fabric.ServerReachDistanceManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;

@Mixin(ServerPlayerGameMode.class)
public abstract class BreakingDistanceOverwriter {

	@Accessor("player")
	abstract ServerPlayer lottweaks_getPlayer();

	@ModifyConstant(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;I)V", constant = @Constant(doubleValue = 36.0D))
	private double lottweaks_handleBlockBreakAction(double defaultValue) {
		ServerPlayer player = lottweaks_getPlayer();
		return ServerReachDistanceManager.getSquaredReachDistance(player, defaultValue);
	}

}
