package com.github.lotqwerty.lottweaks;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AdjustRangeHelper {

	private static final UUID _UUID = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final String NAME = LotTweaks.MODID + "v2";

	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		removeOldRangeModifiers(event.getPlayer());
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			clearRangeModifier(event.player);
		}
	}

	//Just remove all of them!
	public static void removeOldRangeModifiers(Player player) {
		AttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}
	
	private static void clearRangeModifier(Player player) {
		player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(_UUID);
	}

	public static void changeRangeModifier(Player player, double dist) {
		clearRangeModifier(player);
		AttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		instance.addPermanentModifier(new AttributeModifier(_UUID, NAME, dist - instance.getBaseValue() + 0.5, AttributeModifier.Operation.ADDITION));
	}
	
}
