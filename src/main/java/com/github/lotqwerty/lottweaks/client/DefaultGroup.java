package com.github.lotqwerty.lottweaks.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DefaultGroup {

	public static List<List<ItemState>> getDefaultPrimaryGroupList() {
		List<List<ItemState>> groupList = new ArrayList<>();
		return groupList;
	}

	private static List<ItemState> toList(Block ... blocks) {
		List<ItemState> group = new ArrayList<>();
		for (Block block : blocks) {
			group.add(new ItemState(block));
		}
		return group;
	}

	private static List<ItemState> toList(Item ... items) {
		List<ItemState> group = new ArrayList<>();
		for (Item item : items) {
			group.add(new ItemState(item));
		}
		return group;
	}

	private static List<ItemState> toList(ItemStack ... itemStacks) {
		List<ItemState> group = new ArrayList<>();
		for (ItemStack itemStack : itemStacks) {
			group.add(new ItemState(itemStack));
		}
		return group;
	}

	private static List<ItemState> toMetaVariants(Block block, int end) {
		List<ItemState> group = new ArrayList<>();
		for (int meta=0; meta<end; meta++) {
			group.add(new ItemState(new ItemStack(block, 1, meta)));
		}
		return group;
	}

	private static List<ItemState> toList(Object ... objects) {
		List<ItemState> group = new ArrayList<>();
		for (Object object : objects) {
			if (object instanceof Block) {
				group.add(new ItemState((Block)object));
			} else if (object instanceof Item) {
				group.add(new ItemState((Item)object));
			} else if (object instanceof ItemStack) {
				group.add(new ItemState((ItemStack)object));
			} else if (object instanceof List<?>) {
				group.addAll(toList(((List<?>)object).toArray()));
			} else {
				throw new RuntimeException();
			}
		}
		return group;
	}

}
