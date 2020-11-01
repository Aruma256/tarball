package com.github.lotqwerty.lottweaks;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AdjustRangeHelper {

	@SubscribeEvent
	public void some(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			clearRangeModifiers(event.player);
		}
	}

	public static void clearRangeModifiers(EntityPlayer player) {
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		for (AttributeModifier modifier: instance.getModifiers()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}
	
	public static void changeRangeModifier(EntityPlayer player, double dist) {
		clearRangeModifiers(player);
		IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(LotTweaks.MODID, dist, 0));
	}
	
}
