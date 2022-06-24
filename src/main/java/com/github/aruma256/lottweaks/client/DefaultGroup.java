package com.github.aruma256.lottweaks.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DefaultGroup {

	public static List<List<ItemState>> getDefaultPrimaryGroupList() {
		List<List<ItemState>> l = new ArrayList<>();
		l.add(toList(
			Blocks.GRASS,
			toMetaVariants(Blocks.DIRT, 3),
			Blocks.GRAVEL, Blocks.CLAY,
			Blocks.STONE, new ItemStack(Blocks.STONE, 1, 1), new ItemStack(Blocks.STONE, 1, 3), new ItemStack(Blocks.STONE, 1, 5)
		));
		l.add(toList(Blocks.COAL_ORE, Blocks.LAPIS_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE));
		// building blocks
		l.add(toList(
			Blocks.SAND, toMetaVariants(Blocks.SANDSTONE, 3), new ItemStack(Blocks.STONE_SLAB, 1, 1), Blocks.SANDSTONE_STAIRS,
			new ItemStack(Blocks.SAND, 1, 1), toMetaVariants(Blocks.RED_SANDSTONE, 3), new ItemStack(Blocks.STONE_SLAB2, 1, 0), Blocks.RED_SANDSTONE_STAIRS
		));
		l.add(toList(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.EMERALD_BLOCK));
		// plants
		l.add(toList(toMetaVariants(Blocks.LOG, 4), toMetaVariants(Blocks.LOG2, 2)));
		l.add(toList(toMetaVariants(Blocks.LEAVES, 4), toMetaVariants(Blocks.LEAVES2, 2)));
		// color variations
		l.add(toList(Blocks.GLASS, toMetaVariants(Blocks.STAINED_GLASS, 16)));
		l.add(toMetaVariants(Blocks.WOOL, 16));

		return l;
	}

	public static List<List<ItemState>> getDefaultSecondaryGroupList() {
		List<List<ItemState>> groupList = new ArrayList<>();
		return groupList;
	}

	private static ItemState toItemState(Object object) {
		if (object instanceof Block) {
			return new ItemState(new ItemStack((Block)object));
		} else if (object instanceof Item) {
			return new ItemState(new ItemStack((Item)object));
		} else if (object instanceof ItemStack) {
			return new ItemState((ItemStack)object);
		} else if (object instanceof ItemState) {
			return (ItemState)object;
		} else {
			throw new RuntimeException(object.toString());
		}
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
			if (object instanceof List<?>) {
				group.addAll(toList(((List<?>)object).toArray()));
			} else {
				group.add(toItemState(object));
			}
		}
		return group;
	}

}
