package com.github.lotqwerty.lottweaks;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AdjustRangeHelper {

	private static final UUID _UUID = new UUID(2457550121339451521L, 1595282694073824061L);
	private static final String NAME = LotTweaks.MODID + "v2";
	private static final UUID EXTENDRANGE_UUID = UUID.fromString("4754375e-b417-400d-8d0c-b86f68192d34");
	private static final String EXTENDRANGE_NAME = LotTweaks.MODID + "v2_EXTENDRANGE";

	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent event) {
		removeOldRangeModifiers(event.player);
		deactivateExtendedRange(event.player);
	}

	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent event) {
		deactivateExtendedRange(event.player);
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			clearRangeModifier(event.player);
			deactivateExtendedRange(event.player);
		}
	}

	//Just remove all of them!
	private static void removeOldRangeModifiers(EntityPlayer player) {
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}

	private static void clearRangeModifier(EntityPlayer player) {
		player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(_UUID);
	}

	public static void changeRangeModifier(EntityPlayer player, double dist) {
		clearRangeModifier(player);
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(_UUID, NAME, dist - instance.getBaseValue() + 0.5, 0));
	}

	public static void deactivateExtendedRange(EntityPlayer player) {
		player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(EXTENDRANGE_UUID);
	}

	public static void activateExtendedRange(EntityPlayer player, int dist) {
		deactivateExtendedRange(player);
		player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).applyModifier(new AttributeModifier(EXTENDRANGE_UUID, EXTENDRANGE_NAME, dist, 0));
	}

	public static boolean isExtendRangeActivated(EntityPlayer player) {
		return player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getModifier(EXTENDRANGE_UUID) != null;
	}

}
