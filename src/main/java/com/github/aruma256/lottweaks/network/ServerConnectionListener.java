package com.github.aruma256.lottweaks.network;

import com.github.aruma256.lottweaks.ReachRangeManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerConnectionListener {

	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide()) {
			ReachRangeManager.onLogin(event.getPlayer());
			LTPacketHandler.sendHelloMessage((ServerPlayerEntity) event.getPlayer());
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {
		if (!event.getPlayer().level.isClientSide()) {
			ReachRangeManager.onLogout(event.getPlayer());
		}
	}

}
