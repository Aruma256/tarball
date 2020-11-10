package com.github.lotqwerty.lottweaks;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;
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
		ModifiableAttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		for (AttributeModifier modifier: instance.getModifierListCopy()) {
			if (modifier.getName().equals(LotTweaks.MODID)) {
				instance.removeModifier(modifier);
			}
		}
	}
	
	public static void changeRangeModifier(PlayerEntity player, double dist) {
		clearRangeModifiers(player);
		ModifiableAttributeInstance instance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		instance.applyPersistentModifier(new AttributeModifier(LotTweaks.MODID, dist - instance.getBaseValue() + 0.5, AttributeModifier.Operation.ADDITION));
	}
	
}
