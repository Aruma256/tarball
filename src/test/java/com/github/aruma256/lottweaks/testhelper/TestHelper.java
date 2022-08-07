package com.github.aruma256.lottweaks.testhelper;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;

public class TestHelper {

	public static ItemStack createNBTstack(ItemStack itemStack, String nbtString) {
		try {
			itemStack.setTag(JsonToNBT.parseTag(nbtString));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		return itemStack;
	}

}
