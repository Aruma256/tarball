package com.github.aruma256.lottweaks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

public class ClientUtil {

	public static Minecraft getClient() {
		return Minecraft.getInstance();
	}

	public static ClientPlayerEntity getClientPlayer() {
		return getClient().player;
	}

	public static ClientWorld getClientWorld() {
		return getClient().level;
	}

}
