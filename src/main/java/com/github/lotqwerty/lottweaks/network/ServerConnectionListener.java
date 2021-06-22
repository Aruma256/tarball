package com.github.lotqwerty.lottweaks.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ServerConnectionListener {

	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			LTPacketHandler.sendHelloMessage((EntityPlayerMP) event.player);
		}
	}

}
