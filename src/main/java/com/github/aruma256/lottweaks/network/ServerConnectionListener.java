package com.github.aruma256.lottweaks.network;

import com.github.aruma256.lottweaks.ReachRangeManager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class ServerConnectionListener {

	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			ReachRangeManager.onLogin(event.player);
			LTPacketHandler.sendHelloMessage((EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(final PlayerLoggedOutEvent event) {
		if (!event.player.world.isRemote) {
			ReachRangeManager.onLogout(event.player);
		}
	}

}
