package com.github.aruma256.lottweaks.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;

public class DefaultGroup {

	public static List<List<ItemState>> getDefaultGroupList0() {
		List<List<ItemState>> l = new ArrayList<>();
		// overworld blocks
		l.add(toList(
			Blocks.GRASS_BLOCK,
			Blocks.DIRT,
			Blocks.COARSE_DIRT,
			Blocks.PODZOL,
			Blocks.GRAVEL,
			Blocks.CLAY,
			Blocks.STONE,
			Blocks.GRANITE,
			Blocks.DIORITE,
			Blocks.ANDESITE
		));
		l.add(toList(Blocks.COAL_ORE, Blocks.LAPIS_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE));
		// building blocks
		l.add(toList(
			Blocks.POLISHED_GRANITE,
			Blocks.POLISHED_GRANITE_STAIRS,
			Blocks.POLISHED_GRANITE_SLAB,
			Blocks.POLISHED_DIORITE,
			Blocks.POLISHED_DIORITE_STAIRS,
			Blocks.POLISHED_DIORITE_SLAB,
			Blocks.POLISHED_ANDESITE,
			Blocks.POLISHED_ANDESITE_STAIRS,
			Blocks.POLISHED_ANDESITE_SLAB,
			// brick blocks
			Blocks.BRICKS,
			Blocks.BRICK_STAIRS,
			Blocks.BRICK_SLAB,
			Blocks.BRICK_WALL,
			// stonebrick blocks
			Blocks.STONE_BRICKS,
			Blocks.STONE_BRICK_STAIRS,
			Blocks.STONE_BRICK_SLAB,
			Blocks.STONE_BRICK_WALL,
			Blocks.MOSSY_STONE_BRICKS,
			Blocks.MOSSY_STONE_BRICK_STAIRS,
			Blocks.MOSSY_STONE_BRICK_SLAB,
			Blocks.MOSSY_STONE_BRICK_WALL,
			Blocks.CRACKED_STONE_BRICKS,
			Blocks.CHISELED_STONE_BRICKS,
			// nether brick blocks
			Blocks.NETHER_BRICKS,
			Blocks.NETHER_BRICK_STAIRS,
			Blocks.NETHER_BRICK_SLAB,
			Blocks.NETHER_BRICK_FENCE,
			Blocks.NETHER_BRICK_WALL,
			// red nether brick blocks
			Blocks.RED_NETHER_BRICKS,
			Blocks.RED_NETHER_BRICK_STAIRS,
			Blocks.RED_NETHER_BRICK_SLAB,
			Blocks.RED_NETHER_BRICK_WALL,
			// quartz blocks
			Blocks.QUARTZ_BLOCK,
			Blocks.QUARTZ_STAIRS,
			Blocks.QUARTZ_SLAB,
			Blocks.SMOOTH_QUARTZ,
			Blocks.SMOOTH_QUARTZ_STAIRS,
			Blocks.SMOOTH_QUARTZ_SLAB,
			Blocks.CHISELED_QUARTZ_BLOCK,
			Blocks.QUARTZ_PILLAR,
			// purpur blocks
			Blocks.PURPUR_BLOCK,
			Blocks.PURPUR_PILLAR,
			Blocks.PURPUR_STAIRS,
			Blocks.PURPUR_SLAB
		));
		l.add(toList(
			// sand and sandstone blocks
			Blocks.SAND,
			Blocks.SANDSTONE,
			Blocks.SANDSTONE_STAIRS,
			Blocks.SANDSTONE_SLAB,
			Blocks.SANDSTONE_WALL,
			Blocks.SMOOTH_SANDSTONE,
			Blocks.SMOOTH_SANDSTONE_STAIRS,
			Blocks.SMOOTH_SANDSTONE_SLAB,
			Blocks.CUT_SANDSTONE,
			Blocks.CUT_SANDSTONE_SLAB,
			Blocks.CHISELED_SANDSTONE,
			// red sand and sandstone blocks
			Blocks.RED_SAND,
			Blocks.RED_SANDSTONE,
			Blocks.RED_SANDSTONE_STAIRS,
			Blocks.RED_SANDSTONE_SLAB,
			Blocks.RED_SANDSTONE_WALL,
			Blocks.SMOOTH_RED_SANDSTONE,
			Blocks.SMOOTH_RED_SANDSTONE_STAIRS,
			Blocks.SMOOTH_RED_SANDSTONE_SLAB,
			Blocks.CUT_RED_SANDSTONE,
			Blocks.CUT_RED_SANDSTONE_SLAB,
			Blocks.CHISELED_RED_SANDSTONE
		));
		l.add(toList(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.EMERALD_BLOCK));
		l.add(toList(
			Blocks.OAK_PLANKS,      Blocks.OAK_SLAB,      Blocks.OAK_STAIRS,      Blocks.OAK_FENCE,      Blocks.OAK_FENCE_GATE,      Items.OAK_DOOR,
			Blocks.SPRUCE_PLANKS,   Blocks.SPRUCE_SLAB,   Blocks.SPRUCE_STAIRS,   Blocks.SPRUCE_FENCE,   Blocks.SPRUCE_FENCE_GATE,   Items.SPRUCE_DOOR,
			Blocks.BIRCH_PLANKS,    Blocks.BIRCH_SLAB,    Blocks.BIRCH_STAIRS,    Blocks.BIRCH_FENCE,    Blocks.BIRCH_FENCE_GATE,    Items.BIRCH_DOOR,
			Blocks.JUNGLE_PLANKS,   Blocks.JUNGLE_SLAB,   Blocks.JUNGLE_STAIRS,   Blocks.JUNGLE_FENCE,   Blocks.JUNGLE_FENCE_GATE,   Items.JUNGLE_DOOR,
			Blocks.ACACIA_PLANKS,   Blocks.ACACIA_SLAB,   Blocks.ACACIA_STAIRS,   Blocks.ACACIA_FENCE,   Blocks.ACACIA_FENCE_GATE,   Items.ACACIA_DOOR,
			Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Items.DARK_OAK_DOOR
		));
		// plants
		l.add(toList(
			Blocks.OAK_LOG,      Blocks.OAK_LEAVES,
			Blocks.SPRUCE_LOG,   Blocks.SPRUCE_LEAVES,
			Blocks.BIRCH_LOG,    Blocks.BIRCH_LEAVES,
			Blocks.JUNGLE_LOG,   Blocks.JUNGLE_LEAVES,
			Blocks.ACACIA_LOG,   Blocks.ACACIA_LEAVES,
			Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES
		));
		l.add(toList(
			Blocks.OAK_SAPLING,
			Blocks.SPRUCE_SAPLING,
			Blocks.BIRCH_SAPLING,
			Blocks.JUNGLE_SAPLING,
			Blocks.ACACIA_SAPLING,
			Blocks.DARK_OAK_SAPLING,
			Blocks.TALL_GRASS,
			Blocks.LARGE_FERN,
			Blocks.VINE,
			Blocks.LILY_PAD,
			Blocks.DANDELION,
			Blocks.POPPY,
			Blocks.BLUE_ORCHID,
			Blocks.ALLIUM,
			Blocks.AZURE_BLUET,
			Blocks.RED_TULIP,
			Blocks.ORANGE_TULIP,
			Blocks.WHITE_TULIP,
			Blocks.PINK_TULIP,
			Blocks.OXEYE_DAISY,
			Blocks.CORNFLOWER,
			Blocks.LILY_OF_THE_VALLEY,
			Blocks.WITHER_ROSE,
			// double
			Blocks.SUNFLOWER,
			Blocks.LILAC,
			Blocks.ROSE_BUSH,
			Blocks.PEONY,
			// others
			Blocks.CACTUS,
			Blocks.DEAD_BUSH,
			Blocks.BROWN_MUSHROOM,
			Blocks.RED_MUSHROOM
		));
		// redstone and transportation
		l.add(toList(
			Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL,
			Items.MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART
		));
		// enchanted item example
		l.add(toList(
			Items.BOW,
			getEnchantedStack(Items.BOW, "{Enchantments:[{lvl:5s,id:\"minecraft:power\"}]}"),
			getEnchantedStack(Items.BOW, "{Enchantments:[{lvl:2s,id:\"minecraft:punch\"}]}"),
			getEnchantedStack(Items.BOW, "{Enchantments:[{lvl:1s,id:\"minecraft:flame\"}]}"),
			getEnchantedStack(Items.BOW, "{Enchantments:[{lvl:1s,id:\"minecraft:infinity\"}]}")
		));
		return l;
	}

	public static List<List<ItemState>> getDefaultGroupList1() {
		List<List<ItemState>> l = new ArrayList<>();
		l.add(toList(
			Blocks.RED_WOOL,
			Blocks.ORANGE_WOOL,
			Blocks.YELLOW_WOOL,
			Blocks.LIME_WOOL,
			Blocks.GREEN_WOOL,
			Blocks.CYAN_WOOL,
			Blocks.LIGHT_BLUE_WOOL,
			Blocks.BLUE_WOOL,
			Blocks.PURPLE_WOOL,
			Blocks.MAGENTA_WOOL,
			Blocks.PINK_WOOL,
			Blocks.BROWN_WOOL,
			Blocks.BLACK_WOOL,
			Blocks.GRAY_WOOL,
			Blocks.WHITE_WOOL
		));
		l.add(toList(
			Blocks.GLASS,
			Blocks.RED_STAINED_GLASS,
			Blocks.ORANGE_STAINED_GLASS,
			Blocks.YELLOW_STAINED_GLASS,
			Blocks.LIME_STAINED_GLASS,
			Blocks.GREEN_STAINED_GLASS,
			Blocks.CYAN_STAINED_GLASS,
			Blocks.LIGHT_BLUE_STAINED_GLASS,
			Blocks.BLUE_STAINED_GLASS,
			Blocks.PURPLE_STAINED_GLASS,
			Blocks.MAGENTA_STAINED_GLASS,
			Blocks.PINK_STAINED_GLASS,
			Blocks.BROWN_STAINED_GLASS,
			Blocks.BLACK_STAINED_GLASS,
			Blocks.GRAY_STAINED_GLASS,
			Blocks.WHITE_STAINED_GLASS
		));
		l.add(toList(
			Blocks.TERRACOTTA,
			Blocks.RED_TERRACOTTA,
			Blocks.ORANGE_TERRACOTTA,
			Blocks.YELLOW_TERRACOTTA,
			Blocks.LIME_TERRACOTTA,
			Blocks.GREEN_TERRACOTTA,
			Blocks.CYAN_TERRACOTTA,
			Blocks.LIGHT_BLUE_TERRACOTTA,
			Blocks.BLUE_TERRACOTTA,
			Blocks.PURPLE_TERRACOTTA,
			Blocks.MAGENTA_TERRACOTTA,
			Blocks.PINK_TERRACOTTA,
			Blocks.BROWN_TERRACOTTA,
			Blocks.BLACK_TERRACOTTA,
			Blocks.GRAY_TERRACOTTA,
			Blocks.WHITE_TERRACOTTA
		));
		l.add(toList(
			Blocks.RED_CONCRETE,
			Blocks.ORANGE_CONCRETE,
			Blocks.YELLOW_CONCRETE,
			Blocks.LIME_CONCRETE,
			Blocks.GREEN_CONCRETE,
			Blocks.CYAN_CONCRETE,
			Blocks.LIGHT_BLUE_CONCRETE,
			Blocks.BLUE_CONCRETE,
			Blocks.PURPLE_CONCRETE,
			Blocks.MAGENTA_CONCRETE,
			Blocks.PINK_CONCRETE,
			Blocks.BROWN_CONCRETE,
			Blocks.BLACK_CONCRETE,
			Blocks.GRAY_CONCRETE,
			Blocks.WHITE_CONCRETE
		));
		l.add(toList(
			Blocks.RED_CONCRETE_POWDER,
			Blocks.ORANGE_CONCRETE_POWDER,
			Blocks.YELLOW_CONCRETE_POWDER,
			Blocks.LIME_CONCRETE_POWDER,
			Blocks.GREEN_CONCRETE_POWDER,
			Blocks.CYAN_CONCRETE_POWDER,
			Blocks.LIGHT_BLUE_CONCRETE_POWDER,
			Blocks.BLUE_CONCRETE_POWDER,
			Blocks.PURPLE_CONCRETE_POWDER,
			Blocks.MAGENTA_CONCRETE_POWDER,
			Blocks.PINK_CONCRETE_POWDER,
			Blocks.BROWN_CONCRETE_POWDER,
			Blocks.BLACK_CONCRETE_POWDER,
			Blocks.GRAY_CONCRETE_POWDER,
			Blocks.WHITE_CONCRETE_POWDER
		));
		l.add(toList(
			Blocks.GLASS_PANE,
			Blocks.RED_STAINED_GLASS_PANE,
			Blocks.ORANGE_STAINED_GLASS_PANE,
			Blocks.YELLOW_STAINED_GLASS_PANE,
			Blocks.LIME_STAINED_GLASS_PANE,
			Blocks.GREEN_STAINED_GLASS_PANE,
			Blocks.CYAN_STAINED_GLASS_PANE,
			Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
			Blocks.BLUE_STAINED_GLASS_PANE,
			Blocks.PURPLE_STAINED_GLASS_PANE,
			Blocks.MAGENTA_STAINED_GLASS_PANE,
			Blocks.PINK_STAINED_GLASS_PANE,
			Blocks.BROWN_STAINED_GLASS_PANE,
			Blocks.BLACK_STAINED_GLASS_PANE,
			Blocks.GRAY_STAINED_GLASS_PANE,
			Blocks.WHITE_STAINED_GLASS_PANE
		));
		l.add(toList(
			Blocks.RED_CARPET,
			Blocks.ORANGE_CARPET,
			Blocks.YELLOW_CARPET,
			Blocks.LIME_CARPET,
			Blocks.GREEN_CARPET,
			Blocks.CYAN_CARPET,
			Blocks.LIGHT_BLUE_CARPET,
			Blocks.BLUE_CARPET,
			Blocks.PURPLE_CARPET,
			Blocks.MAGENTA_CARPET,
			Blocks.PINK_CARPET,
			Blocks.BROWN_CARPET,
			Blocks.BLACK_CARPET,
			Blocks.GRAY_CARPET,
			Blocks.WHITE_CARPET
		));
		l.add(toList(
			Blocks.RED_SHULKER_BOX,
			Blocks.ORANGE_SHULKER_BOX,
			Blocks.YELLOW_SHULKER_BOX,
			Blocks.LIME_SHULKER_BOX,
			Blocks.GREEN_SHULKER_BOX,
			Blocks.CYAN_SHULKER_BOX,
			Blocks.LIGHT_BLUE_SHULKER_BOX,
			Blocks.BLUE_SHULKER_BOX,
			Blocks.PURPLE_SHULKER_BOX,
			Blocks.MAGENTA_SHULKER_BOX,
			Blocks.PINK_SHULKER_BOX,
			Blocks.BROWN_SHULKER_BOX,
			Blocks.BLACK_SHULKER_BOX,
			Blocks.GRAY_SHULKER_BOX,
			Blocks.WHITE_SHULKER_BOX
		));
		l.add(toList(
			Blocks.RED_GLAZED_TERRACOTTA,
			Blocks.ORANGE_GLAZED_TERRACOTTA,
			Blocks.YELLOW_GLAZED_TERRACOTTA,
			Blocks.LIME_GLAZED_TERRACOTTA,
			Blocks.GREEN_GLAZED_TERRACOTTA,
			Blocks.CYAN_GLAZED_TERRACOTTA,
			Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
			Blocks.BLUE_GLAZED_TERRACOTTA,
			Blocks.PURPLE_GLAZED_TERRACOTTA,
			Blocks.MAGENTA_GLAZED_TERRACOTTA,
			Blocks.PINK_GLAZED_TERRACOTTA,
			Blocks.BROWN_GLAZED_TERRACOTTA,
			Blocks.BLACK_GLAZED_TERRACOTTA,
			Blocks.GRAY_GLAZED_TERRACOTTA,
			Blocks.WHITE_GLAZED_TERRACOTTA
		));
		l.add(toList(
			Blocks.RED_BED,
			Blocks.ORANGE_BED,
			Blocks.YELLOW_BED,
			Blocks.LIME_BED,
			Blocks.GREEN_BED,
			Blocks.CYAN_BED,
			Blocks.LIGHT_BLUE_BED,
			Blocks.BLUE_BED,
			Blocks.PURPLE_BED,
			Blocks.MAGENTA_BED,
			Blocks.PINK_BED,
			Blocks.BROWN_BED,
			Blocks.BLACK_BED,
			Blocks.GRAY_BED,
			Blocks.WHITE_BED
		));
		l.add(toList(
			Blocks.RED_BANNER,
			Blocks.ORANGE_BANNER,
			Blocks.YELLOW_BANNER,
			Blocks.LIME_BANNER,
			Blocks.GREEN_BANNER,
			Blocks.CYAN_BANNER,
			Blocks.LIGHT_BLUE_BANNER,
			Blocks.BLUE_BANNER,
			Blocks.PURPLE_BANNER,
			Blocks.MAGENTA_BANNER,
			Blocks.PINK_BANNER,
			Blocks.BROWN_BANNER,
			Blocks.BLACK_BANNER,
			Blocks.GRAY_BANNER,
			Blocks.WHITE_BANNER
		));
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

	private static ItemStack getEnchantedStack(Item item, String nbtStr) {
		ItemStack itemStack = new ItemStack(item);
		try {
			itemStack.setTag(JsonToNBT.parseTag(nbtStr));
		} catch (CommandSyntaxException e) {
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
