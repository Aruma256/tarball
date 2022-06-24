package com.github.lotqwerty.lottweaks;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AdjustRangeHelper {

	private static final UUID UUID_V2 = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final UUID UUID_V3_BASE = UUID.fromString("c69d28b2-0017-4638-aa3c-17a59bfb2ebb");
	private static final UUID UUID_V3_EXTENSION = UUID.fromString("3c234877-97ea-42cd-9f39-c8ad062f78a9");
	private static final String NAME_V3_BASE = LotTweaks.MODID + "v3" + "base";
	private static final String NAME_V3_EXTENSION = LotTweaks.MODID + "v3" + "extension";

	public static void onLogin(EntityPlayer player) {
		remove_OUTDATED_RangeModifiers(player);
		removeV3Modifiers(player);
	}

	public static void onLogout(EntityPlayer player) {
		remove_OUTDATED_RangeModifiers(player);
		removeV3Modifiers(player);
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			removeV3Modifiers(event.player);
		}
	}

	public static void setV3BaseModifier(EntityPlayer player, double dist) {
		removeV3Modifiers(player);
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(UUID_V3_BASE, NAME_V3_BASE, dist - instance.getBaseValue(), 0));
	}

	public static void setV3ExtentionModifier(EntityPlayer player, double dist) {
		clearV3ExtentionModifier(player);
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(UUID_V3_EXTENSION, NAME_V3_EXTENSION, dist, 0));
	}

	public static void clearV3ExtentionModifier(EntityPlayer player) {
		_removeReachRangeModifier(player, UUID_V3_EXTENSION);
	}

	private static void removeV3Modifiers(EntityPlayer player) {
		_removeReachRangeModifier(player, UUID_V3_BASE);
		_removeReachRangeModifier(player, UUID_V3_EXTENSION);
	}

	private static void remove_OUTDATED_RangeModifiers(EntityPlayer player) {
		// the oldest attribute
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
		// V2 attribute
		_removeReachRangeModifier(player, UUID_V2);
	}

	private static void _removeReachRangeModifier(EntityPlayer player, UUID uuid) {
		player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(uuid);
	}

}
