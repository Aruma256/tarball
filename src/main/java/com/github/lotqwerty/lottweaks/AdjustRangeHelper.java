package com.github.lotqwerty.lottweaks;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AdjustRangeHelper {

	@SubscribeEvent
	public void some(PlayerTickEvent event) {
		if (event.side.isServer() && !event.player.isCreative()) {
			clearRangeModifiers(event.player);
		}
	}

	public static void clearRangeModifiers(PlayerEntity player) {
		IAttributeInstance instance = player.getAttribute(PlayerEntity.REACH_DISTANCE);
		for (AttributeModifier modifier: instance.func_225505_c_()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}
	
	public static void changeRangeModifier(PlayerEntity player, double dist) {
		clearRangeModifiers(player);
		IAttributeInstance instance = player.getAttribute(PlayerEntity.REACH_DISTANCE);
		instance.applyModifier(new AttributeModifier(LotTweaks.MODID, dist - instance.getBaseValue() + 0.5, AttributeModifier.Operation.ADDITION));
	}
	
}
