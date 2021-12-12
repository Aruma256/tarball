package com.github.lotqwerty.lottweaks.fabric;

import net.minecraft.world.entity.player.Player;

public class ServerReachDistanceManager {
	
//	private static final HashMap<String, Double> map = new HashMap<>();
	private static final double SQUARED_REACH = 128D * 128D;
	
	public static double getSquaredReachDistance(Player player, double defaultSquaredValue) {
		if (player.isCreative()) {
			return SQUARED_REACH;
		}
		return defaultSquaredValue;
	}
	
}
