package com.github.aruma256.lottweaks.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class DefaultGroup {

	private static final int[] COLOR_VARIATION_META = {14, 1, 4, 5, 13, 9, 3, 11, 10, 2, 6, 12, 15, 7, 8, 0};

	public static List<List<ItemState>> getDefaultGroupList0() {
		List<List<ItemState>> l = new ArrayList<>();
		// overworld blocks
		l.add(toList(
			Blocks.GRASS,
			toMetaVariants(Blocks.DIRT, 3),
			Blocks.GRAVEL, Blocks.CLAY,
			Blocks.STONE, new ItemStack(Blocks.STONE, 1, 1), new ItemStack(Blocks.STONE, 1, 3), new ItemStack(Blocks.STONE, 1, 5)
		));
		l.add(toList(Blocks.COAL_ORE, Blocks.LAPIS_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE));
		// building blocks
		l.add(toList(
			new ItemStack(Blocks.STONE, 1, 2), new ItemStack(Blocks.STONE, 1, 4), new ItemStack(Blocks.STONE, 1, 6),
			Blocks.BRICK_BLOCK, Blocks.BRICK_STAIRS, new ItemStack(Blocks.STONE_SLAB, 1, 4),
			toMetaVariants(Blocks.STONEBRICK, 4), Blocks.STONE_BRICK_STAIRS, new ItemStack(Blocks.STONE_SLAB, 1, 5),
			Blocks.NETHER_BRICK, Blocks.NETHER_BRICK_STAIRS, new ItemStack(Blocks.STONE_SLAB, 1, 6), Blocks.NETHER_BRICK_FENCE,
			Blocks.RED_NETHER_BRICK,
			toMetaVariants(Blocks.QUARTZ_BLOCK, 3), Blocks.QUARTZ_STAIRS, new ItemStack(Blocks.STONE_SLAB, 1, 7),
			Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR, Blocks.PURPUR_STAIRS, Blocks.PURPUR_SLAB
		));
		l.add(toList(
			Blocks.SAND, toMetaVariants(Blocks.SANDSTONE, 3), new ItemStack(Blocks.STONE_SLAB, 1, 1), Blocks.SANDSTONE_STAIRS,
			new ItemStack(Blocks.SAND, 1, 1), toMetaVariants(Blocks.RED_SANDSTONE, 3), new ItemStack(Blocks.STONE_SLAB2, 1, 0), Blocks.RED_SANDSTONE_STAIRS
		));
		l.add(toList(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.EMERALD_BLOCK));
		l.add(toList(
			new ItemStack(Blocks.PLANKS, 1, 0), new ItemStack(Blocks.WOODEN_SLAB, 1, 0), Blocks.OAK_STAIRS, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Items.OAK_DOOR,
			new ItemStack(Blocks.PLANKS, 1, 1), new ItemStack(Blocks.WOODEN_SLAB, 1, 1), Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Items.SPRUCE_DOOR,
			new ItemStack(Blocks.PLANKS, 1, 2), new ItemStack(Blocks.WOODEN_SLAB, 1, 2), Blocks.BIRCH_STAIRS, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Items.BIRCH_DOOR,
			new ItemStack(Blocks.PLANKS, 1, 3), new ItemStack(Blocks.WOODEN_SLAB, 1, 3), Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Items.JUNGLE_DOOR,
			new ItemStack(Blocks.PLANKS, 1, 4), new ItemStack(Blocks.WOODEN_SLAB, 1, 4), Blocks.ACACIA_STAIRS, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Items.ACACIA_DOOR,
			new ItemStack(Blocks.PLANKS, 1, 5), new ItemStack(Blocks.WOODEN_SLAB, 1, 5), Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Items.DARK_OAK_DOOR
		));
		// plants
		l.add(toList(
			new ItemStack(Blocks.LOG, 1, 0), new ItemStack(Blocks.LEAVES, 1, 0),
			new ItemStack(Blocks.LOG, 1, 1), new ItemStack(Blocks.LEAVES, 1, 1),
			new ItemStack(Blocks.LOG, 1, 2), new ItemStack(Blocks.LEAVES, 1, 2),
			new ItemStack(Blocks.LOG, 1, 3), new ItemStack(Blocks.LEAVES, 1, 3),
			new ItemStack(Blocks.LOG2, 1, 0), new ItemStack(Blocks.LEAVES2, 1, 0),
			new ItemStack(Blocks.LOG2, 1, 1), new ItemStack(Blocks.LEAVES2, 1, 1)
		));
		l.add(toList(
			toMetaVariants(Blocks.SAPLING, 6),
			toMetaVariants(Blocks.TALLGRASS, 2),
			Blocks.VINE,
			Blocks.WATERLILY,
			Blocks.YELLOW_FLOWER,
			toMetaVariants(Blocks.RED_FLOWER, 9),
			toMetaVariants(Blocks.DOUBLE_PLANT, 6),
			Blocks.CACTUS,
			Blocks.DEADBUSH,
			Blocks.BROWN_MUSHROOM,
			Blocks.RED_MUSHROOM
		));
		// redstone and transportation
		l.add(toList(
			Blocks.RAIL, Blocks.GOLDEN_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL,
			Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART
		));
		// enchanted item example
		l.add(toList(
			Items.BOW,
			getEnchantedStack(Items.BOW, "{ench:[{lvl:5s,id:48s}]}"),
			getEnchantedStack(Items.BOW, "{ench:[{lvl:2s,id:49s}]}"),
			getEnchantedStack(Items.BOW, "{ench:[{lvl:1s,id:50s}]}"),
			getEnchantedStack(Items.BOW, "{ench:[{lvl:1s,id:51s}]}")
		));
		return l;
	}

	public static List<List<ItemState>> getDefaultGroupList1() {
		List<List<ItemState>> l = new ArrayList<>();
		l.add(toColorVariants(Blocks.WOOL));
		l.add(toList(Blocks.GLASS, toColorVariants(Blocks.STAINED_GLASS)));
		l.add(toList(Blocks.HARDENED_CLAY, toColorVariants(Blocks.STAINED_HARDENED_CLAY)));
		l.add(toColorVariants(Blocks.CONCRETE));
		l.add(toColorVariants(Blocks.CONCRETE_POWDER));
		l.add(toList(Blocks.GLASS_PANE, toColorVariants(Blocks.STAINED_GLASS_PANE)));
		l.add(toColorVariants(Blocks.CARPET));
		l.add(toList(Blocks.RED_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX));
		l.add(toList(Blocks.RED_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA, Blocks.SILVER_GLAZED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA));
		l.add(toColorVariants(Items.BED));
		l.add(toColorVariants(Items.BANNER));
		return l;
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

	private static List<ItemState> toColorVariants(Block block) {
		List<ItemState> group = new ArrayList<>();
		for (int meta : COLOR_VARIATION_META) {
			group.add(new ItemState(new ItemStack(block, 1, meta)));
		}
		return group;
	}

	private static List<ItemState> toColorVariants(Item item) {
		List<ItemState> group = new ArrayList<>();
		for (int meta : COLOR_VARIATION_META) {
			group.add(new ItemState(new ItemStack(item, 1, meta)));
		}
		return group;
	}

	private static ItemStack getEnchantedStack(Item item, String nbtStr) {
		ItemStack itemStack = new ItemStack(item);
		try {
			itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtStr));
		} catch (NBTException e) {
			throw new RuntimeException(e);
		}
		return itemStack;
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
