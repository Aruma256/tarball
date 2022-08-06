package com.github.aruma256.lottweaks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ClientUtil {

	public static Minecraft getClient() {
		return Minecraft.getMinecraft();
	}

	public static EntityPlayerSP getClientPlayer() {
		return getClient().player;
	}

}
