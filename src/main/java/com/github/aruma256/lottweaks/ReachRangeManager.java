package com.github.aruma256.lottweaks;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ReachRangeManager {

	private static final UUID _UUID = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final String NAME = LotTweaks.MODID + "v2";

	public static void onLogin(PlayerEntity player) {
		remove_OUTDATED_RangeModifiers(player);
		removeV2Modifier(player);
	}

	public static void onLogout(PlayerEntity player) {
		removeV2Modifier(player);
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			removeV2Modifier(event.player);
		}
	}

	public static void setV2Modifier(PlayerEntity player, double dist) {
		removeV2Modifier(player);
		if (!player.isCreative()) return;
		ModifiableAttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		instance.addPermanentModifier(new AttributeModifier(_UUID, NAME, dist - instance.getBaseValue(), AttributeModifier.Operation.ADDITION));
	}

	private static void removeV2Modifier(PlayerEntity player) {
		_removeReachRangeModifier(player, _UUID);
	}

	private static void remove_OUTDATED_RangeModifiers(PlayerEntity player) {
		// clean up v1 attribute
		ModifiableAttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}

	private static void _removeReachRangeModifier(PlayerEntity player, UUID uuid) {
		player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(uuid);
	}

}
