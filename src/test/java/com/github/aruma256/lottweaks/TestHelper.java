package com.github.aruma256.lottweaks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class TestHelper {

	public static ItemStack createNBTstack(ItemStack itemStack, String nbtString) {
		try {
			itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
		} catch (NBTException e) {
			throw new RuntimeException(e);
		}
		return itemStack;
	}

}
