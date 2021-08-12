package com.github.lotqwerty.lottweaks.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerConnectionListener {

	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote) {
			LTPacketHandler.sendHelloMessage((ServerPlayerEntity) event.getPlayer());
		}
	}

}
