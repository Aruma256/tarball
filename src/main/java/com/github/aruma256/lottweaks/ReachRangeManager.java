package com.github.aruma256.lottweaks;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ReachRangeManager {

	private static final UUID _UUID = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final String NAME = LotTweaks.MODID + "v2";

	public static void onLogin(EntityPlayer player) {
		remove_OUTDATED_RangeModifiers(player);
		removeV2Modifier(player);
	}

	public static void onLogout(EntityPlayer player) {
		removeV2Modifier(player);
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			removeV2Modifier(event.player);
		}
	}

	public static void setV2Modifier(EntityPlayer player, int dist) {
		removeV2Modifier(player);
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(_UUID, NAME, dist - instance.getBaseValue(), 0));
	}

	private static void removeV2Modifier(EntityPlayer player) {
		_removeReachRangeModifier(player, _UUID);
	}

	private static void remove_OUTDATED_RangeModifiers(EntityPlayer player) {
		// clean up v1 attribute
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}

	private static void _removeReachRangeModifier(EntityPlayer player, UUID uuid) {
		player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(uuid);
	}

}
