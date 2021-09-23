package com.github.lotqwerty.lottweaks.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerConnectionListener {

	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!event.getPlayer().level.isClientSide) {
			LTPacketHandler.sendHelloMessage((ServerPlayer) event.getPlayer());
		}
	}

}
