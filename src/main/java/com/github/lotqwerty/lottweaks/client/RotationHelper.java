package com.github.lotqwerty.lottweaks.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RotationHelper {

	protected static final String BLOCKGROUP_CONFFILE = "LotTweaks-BlockGroups.txt";

	public static final HashMap<Block, Block> BLOCK_CHAIN = new HashMap<>();

	public static final String[] DEFAULT_BLOCK_GROUPS = {
			"//VANILLA BLOCKS",
			"//STONE",
			"minecraft:stone,minecraft:granite,minecraft:polished_granite,minecraft:diorite,minecraft:polished_diorite,minecraft:andesite,minecraft:polished_andesite",
			"//DIRT",
			"minecraft:dirt,minecraft:coarse_dirt,minecraft:podzol,minecraft:crimson_nylium,minecraft:warped_nylium",
			"//PLANKS",
			"minecraft:oak_planks,minecraft:spruce_planks,minecraft:birch_planks,minecraft:jungle_planks,minecraft:acacia_planks,minecraft:dark_oak_planks,minecraft:crimson_planks,minecraft:warped_planks",
			"//SAPLINGS",
			"minecraft:oak_sapling,minecraft:spruce_sapling,minecraft:birch_sapling,minecraft:jungle_sapling,minecraft:acacia_sapling,minecraft:dark_oak_sapling",
			"//ORE series",
			"minecraft:gold_ore,minecraft:iron_ore,minecraft:coal_ore,minecraft:lapis_ore,minecraft:diamond_ore,minecraft:redstone_ore,minecraft:emerald_ore",
			"//LOG",
			"minecraft:oak_log,minecraft:spruce_log,minecraft:birch_log,minecraft:jungle_log,minecraft:acacia_log,minecraft:dark_oak_log,minecraft:crimson_stem,minecraft:warped_stem",
			"//STRIPPED LOG",
			"minecraft:stripped_oak_log,minecraft:stripped_spruce_log,minecraft:stripped_birch_log,minecraft:stripped_jungle_log,minecraft:stripped_acacia_log,minecraft:stripped_dark_oak_log,minecraft:stripped_crimson_stem,minecraft:stripped_warped_stem",
			"//STRIPPED WOOD",
			"minecraft:stripped_oak_wood,minecraft:stripped_spruce_wood,minecraft:stripped_birch_wood,minecraft:stripped_jungle_wood,minecraft:stripped_acacia_wood,minecraft:stripped_dark_oak_wood,minecraft:stripped_crimson_hyphae,minecraft:stripped_warped_hyphae",
			"//WOOL",
			"minecraft:white_wool,minecraft:orange_wool,minecraft:magenta_wool,minecraft:light_blue_wool,minecraft:yellow_wool,minecraft:lime_wool,minecraft:pink_wool,minecraft:gray_wool,minecraft:light_gray_wool,minecraft:cyan_wool,minecraft:purple_wool,minecraft:blue_wool,minecraft:brown_wool,minecraft:green_wool,minecraft:red_wool,minecraft:black_wool",
			"//WOOD",
			"minecraft:oak_wood,minecraft:spruce_wood,minecraft:birch_wood,minecraft:jungle_wood,minecraft:acacia_wood,minecraft:dark_oak_wood,minecraft:crimson_hyphae,minecraft:warped_hyphae",
			"//SPONGE",
			"minecraft:sponge,minecraft:wet_sponge",
			"//SANDSTONE",
			"minecraft:sandstone,minecraft:chiseled_sandstone,minecraft:cut_sandstone",
			"//FLOWER",
			"minecraft:dandelion,minecraft:poppy,minecraft:blue_orchid,minecraft:allium,minecraft:azure_bluet,minecraft:red_tulip,minecraft:orange_tulip,minecraft:white_tulip,minecraft:pink_tulip,minecraft:oxeye_daisy,minecraft:cornflower,minecraft:lily_of_the_valley,minecraft:wither_rose",
			"//Mineral Blocks",
			"minecraft:gold_block,minecraft:iron_block,minecraft:coal_block,minecraft:lapis_block,minecraft:diamond_block,minecraft:redstone_block,minecraft:emerald_block",
			"//WOODEN SLAB",
			"minecraft:oak_slab,minecraft:spruce_slab,minecraft:birch_slab,minecraft:jungle_slab,minecraft:acacia_slab,minecraft:dark_oak_slab,minecraft:crimson_slab,minecraft:warped_slab",
			"//STONE SLAB",
			"minecraft:stone_slab,minecraft:smooth_stone_slab,minecraft:granite_slab,minecraft:polished_granite_slab,minecraft:diorite_slab,minecraft:polished_diorite_slab,minecraft:andesite_slab,minecraft:polished_andesite_slab",
			"//SANDSTONE SLAB",
			"minecraft:sandstone_slab,minecraft:cut_sandstone_slab,minecraft:smooth_sandstone_slab",
			"//RED SANDSTONE SLAB",
			"minecraft:red_sandstone_slab,minecraft:cut_red_sandstone_slab,minecraft:smooth_red_sandstone_slab",
			"//PRISMARINE SLAB",
			"minecraft:prismarine_slab,minecraft:prismarine_brick_slab,minecraft:dark_prismarine_slab",
			"//BLACKSTONE SLAB",
			"minecraft:blackstone_slab,minecraft:polished_blackstone_slab,minecraft:polished_blackstone_brick_slab",
			"//OTHER SLAB",
			"minecraft:petrified_oak_slab,minecraft:cobblestone_slab,minecraft:brick_slab,minecraft:stone_brick_slab,minecraft:nether_brick_slab,minecraft:quartz_slab,minecraft:purpur_slab,minecraft:mossy_stone_brick_slab,minecraft:mossy_cobblestone_slab,minecraft:end_stone_brick_slab,minecraft:smooth_quartz_slab,minecraft:red_nether_brick_slab",
			"//WOODEN STAIRS",
			"minecraft:oak_stairs,minecraft:spruce_stairs,minecraft:birch_stairs,minecraft:jungle_stairs,minecraft:crimson_stairs,minecraft:warped_stairs",
			"//WOODEN DOOR",
			"minecraft:oak_door,minecraft:spruce_door,minecraft:birch_door,minecraft:jungle_door,minecraft:acacia_door,minecraft:dark_oak_door,minecraft:crimson_door,minecraft:warped_door",
			"//WOODEN FENCE",
			"minecraft:oak_fence,minecraft:spruce_fence,minecraft:birch_fence,minecraft:jungle_fence,minecraft:acacia_fence,minecraft:dark_oak_fence,minecraft:crimson_fence,minecraft:warped_fence",
			"//STAINED_GLASS",
			"minecraft:white_stained_glass,minecraft:orange_stained_glass,minecraft:magenta_stained_glass,minecraft:light_blue_stained_glass,minecraft:yellow_stained_glass,minecraft:lime_stained_glass,minecraft:pink_stained_glass,minecraft:gray_stained_glass,minecraft:light_gray_stained_glass,minecraft:cyan_stained_glass,minecraft:purple_stained_glass,minecraft:blue_stained_glass,minecraft:brown_stained_glass,minecraft:green_stained_glass,minecraft:red_stained_glass,minecraft:black_stained_glass",
			"//INFESTED STONE BRICKS",
			"minecraft:infested_stone_bricks,minecraft:infested_mossy_stone_bricks,minecraft:infested_cracked_stone_bricks,minecraft:infested_chiseled_stone_bricks",
			"//FENCE GATE",
			"minecraft:oak_fence_gate,minecraft:spruce_fence_gate,minecraft:birch_fence_gate,minecraft:jungle_fence_gate,minecraft:acacia_fence_gate,minecraft:dark_oak_fence_gate,minecraft:crimson_fence_gate,minecraft:warped_fence_gate",
			"//STONEBRICKS",
			"minecraft:stone_bricks,minecraft:mossy_stone_bricks,minecraft:cracked_stone_bricks,minecraft:chiseled_stone_bricks",
			"//WALL",
			"minecraft:cobblestone_wall,minecraft:mossy_cobblestone_wall,minecraft:brick_wall,minecraft:prismarine_wall,minecraft:red_sandstone_wall,minecraft:mossy_stone_brick_wall,minecraft:granite_wall,minecraft:stone_brick_wall,minecraft:nether_brick_wall,minecraft:andesite_wall,minecraft:red_nether_brick_wall,minecraft:sandstone_wall,minecraft:end_stone_brick_wall,minecraft:diorite_wall,minecraft:blackstone_wall,minecraft:polished_blackstone_wall,minecraft:polished_blackstone_brick_wall",
			"//QUARTZ BLOCK",
			"minecraft:chiseled_quartz_block,minecraft:quartz_block,minecraft:quartz_bricks,minecraft:quartz_pillar",
			"//TERRACOTTA",
			"minecraft:white_terracotta,minecraft:orange_terracotta,minecraft:magenta_terracotta,minecraft:light_blue_terracotta,minecraft:yellow_terracotta,minecraft:lime_terracotta,minecraft:pink_terracotta,minecraft:gray_terracotta,minecraft:light_gray_terracotta,minecraft:cyan_terracotta,minecraft:purple_terracotta,minecraft:blue_terracotta,minecraft:brown_terracotta,minecraft:green_terracotta,minecraft:red_terracotta,minecraft:black_terracotta",
			"//STAINED GLASS PANE",
			"minecraft:white_stained_glass_pane,minecraft:orange_stained_glass_pane,minecraft:magenta_stained_glass_pane,minecraft:light_blue_stained_glass_pane,minecraft:yellow_stained_glass_pane,minecraft:lime_stained_glass_pane,minecraft:pink_stained_glass_pane,minecraft:gray_stained_glass_pane,minecraft:light_gray_stained_glass_pane",
			"//PRISMARINE",
			"minecraft:prismarine,minecraft:prismarine_bricks,minecraft:dark_prismarine",
			"//SHULKER BOX",
			"minecraft:shulker_box,minecraft:white_shulker_box,minecraft:orange_shulker_box,minecraft:magenta_shulker_box,minecraft:light_blue_shulker_box,minecraft:yellow_shulker_box,minecraft:lime_shulker_box,minecraft:pink_shulker_box,minecraft:gray_shulker_box,minecraft:light_gray_shulker_box,minecraft:cyan_shulker_box,minecraft:purple_shulker_box,minecraft:blue_shulker_box,minecraft:brown_shulker_box,minecraft:green_shulker_box,minecraft:red_shulker_box,minecraft:black_shulker_box",
			"//GLAZED TERRACOTTA",
			"minecraft:white_glazed_terracotta,minecraft:orange_glazed_terracotta,minecraft:magenta_glazed_terracotta,minecraft:light_blue_glazed_terracotta,minecraft:yellow_glazed_terracotta,minecraft:lime_glazed_terracotta,minecraft:pink_glazed_terracotta,minecraft:gray_glazed_terracotta,minecraft:light_gray_glazed_terracotta,minecraft:cyan_glazed_terracotta,minecraft:purple_glazed_terracotta,minecraft:blue_glazed_terracotta,minecraft:brown_glazed_terracotta,minecraft:green_glazed_terracotta,minecraft:red_glazed_terracotta,minecraft:black_glazed_terracotta",
			"//CONCRETE",
			"minecraft:white_concrete,minecraft:orange_concrete,minecraft:magenta_concrete,minecraft:light_blue_concrete,minecraft:yellow_concrete,minecraft:lime_concrete,minecraft:pink_concrete,minecraft:gray_concrete,minecraft:light_gray_concrete,minecraft:cyan_concrete,minecraft:purple_concrete,minecraft:blue_concrete,minecraft:brown_concrete,minecraft:green_concrete,minecraft:red_concrete,minecraft:black_concrete",
			"//CONCRETE POWDER",
			"minecraft:white_concrete_powder,minecraft:orange_concrete_powder,minecraft:magenta_concrete_powder,minecraft:light_blue_concrete_powder,minecraft:yellow_concrete_powder,minecraft:lime_concrete_powder,minecraft:pink_concrete_powder,minecraft:gray_concrete_powder,minecraft:light_gray_concrete_powder,minecraft:cyan_concrete_powder,minecraft:purple_concrete_powder,minecraft:blue_concrete_powder,minecraft:brown_concrete_powder,minecraft:green_concrete_powder,minecraft:red_concrete_powder,minecraft:black_concrete_powder",
			"//CORAL BLOCK",
			"minecraft:dead_tube_coral_block,minecraft:dead_brain_coral_block,minecraft:dead_bubble_coral_block,minecraft:dead_fire_coral_block,minecraft:dead_horn_coral_block,minecraft:tube_coral_block,minecraft:brain_coral_block,minecraft:bubble_coral_block,minecraft:fire_coral_block,minecraft:horn_coral_block",
			"//BLACKSTONE",
			"minecraft:blackstone,minecraft:polished_blackstone,minecraft:chiseled_polished_blackstone,minecraft:polished_blackstone_bricks,minecraft:cracked_polished_blackstone_bricks",
			"//CORAL",
			"minecraft:tube_coral,minecraft:brain_coral,minecraft:bubble_coral,minecraft:fire_coral,minecraft:horn_coral,minecraft:dead_brain_coral,minecraft:dead_bubble_coral,minecraft:dead_fire_coral,minecraft:dead_horn_coral,minecraft:dead_tube_coral",
			"//CORAL FAN",
			"minecraft:tube_coral_fan,minecraft:brain_coral_fan,minecraft:bubble_coral_fan,minecraft:fire_coral_fan,minecraft:horn_coral_fan,minecraft:dead_tube_coral_fan,minecraft:dead_brain_coral_fan,minecraft:dead_bubble_coral_fan,minecraft:dead_fire_coral_fan,minecraft:dead_horn_coral_fan",
	};

	public static String[] BLOCK_GROUPS = DEFAULT_BLOCK_GROUPS;

	public static boolean canRotate(ItemStack itemStack) {
		if (itemStack == null || itemStack.isEmpty()) {
			return false;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == Blocks.AIR) {
			return false;
		}
		return BLOCK_CHAIN.containsKey(block);
	}
	
	private static ItemStack toItemStack(Block block) {
//		try {
//			ItemStack stack = block.getPickBlock(null, null, null, null, null);
//			if (stack != null && !stack.isEmpty()) {
//				return stack;
//			}
//		} catch (Exception e) {
//		}
		return new ItemStack(block);
	}

	public static List<ItemStack> getAllRotateResult(ItemStack itemStack){
		List<ItemStack> stacks = new ArrayList<>();
		if (itemStack == null || itemStack.isEmpty()) {
			return null;
		}
		Block srcBlock = Block.getBlockFromItem(itemStack.getItem());
		if (srcBlock == Blocks.AIR) {
			return null;
		}
		if (!BLOCK_CHAIN.containsKey(srcBlock)) {
			return null;
		}
		stacks.add(itemStack);
		Block block = BLOCK_CHAIN.get(srcBlock);
		int counter = 0;
		while (block != srcBlock) {
			stacks.add(toItemStack(block));
			block = BLOCK_CHAIN.get(block);
			counter++;
			if (counter >= 50000) {
				LotTweaks.LOGGER.error("infinite loop!");
				return null;
			}
		}
		return stacks;
	}

	public static boolean loadBlockGroups() {
		BLOCK_CHAIN.clear();
		try {
			int lineCount = 0;
			for (String line: BLOCK_GROUPS) {
				lineCount++;
				if (line.startsWith("//")) {
					continue;
				}
				List<Block> blocks = new ArrayList<>();
				for (String part: line.split(",")) {
					String blockName = part;
					Identifier resourceLocation = new Identifier(blockName);
					if (!Registry.BLOCK.containsId(resourceLocation)) {
						LotTweaks.LOGGER.error(String.format("Not found: '%s'", part));
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					Block block = Registry.BLOCK.get(resourceLocation);
					if (block == null || block == Blocks.AIR) {
						LotTweaks.LOGGER.error(String.format("Not found: '%s'", part));
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					blocks.add(block);
				}
				if (blocks.size() <= 1) {
					LotTweaks.LOGGER.error("Failed to load group: '%s'", line);
					LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
					throw new BlockGroupRegistrationException();
				}
				for (int i=0;i<blocks.size();i++) {
					if (BLOCK_CHAIN.containsKey(blocks.get(i))) {
						LotTweaks.LOGGER.error("BLOCK_GROUPS value is invalid.");
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					BLOCK_CHAIN.put(blocks.get(i), blocks.get((i+1)%blocks.size()));
				}
				LotTweaks.LOGGER.debug(String.format("BLOCK_GROUPS line %d: OK", lineCount));
			}
		} catch (BlockGroupRegistrationException e) {
			BLOCK_CHAIN.clear();
			return false;
		} catch (Exception e) {
			LotTweaks.LOGGER.error(e);
			BLOCK_CHAIN.clear();
			return false;
		}
		return true;
	}

	public static boolean tryToUpdateBlockGroupsFromCommand(String[] newBlockGroups) {
		String[] oldBlockGroups = BLOCK_GROUPS;
		BLOCK_GROUPS = newBlockGroups;
		boolean succeeded = loadBlockGroups();
		if (succeeded) {
			return true;
		} else {
			BLOCK_GROUPS = oldBlockGroups;
			return false;
		}
	}

	public static void loadFromFile() {
		File file = new File(new File("config"), BLOCKGROUP_CONFFILE);
		try {
			if (!file.exists()) {
				LotTweaks.LOGGER.debug("Config file does not exist.");
				writeToFile();
			} else {
				ArrayList<String> list = new ArrayList<String>();
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					list.add(scanner.nextLine());
				}
				scanner.close();
				BLOCK_GROUPS = list.toArray(new String[list.size()]);
			}
		} catch (IOException e) {
			LotTweaks.LOGGER.error("Failed to load config from file");
			e.printStackTrace();
		}
	}

	public static void writeToFile() {
		LotTweaks.LOGGER.debug("Write config to file.");
		File file = new File(new File("config"), BLOCKGROUP_CONFFILE);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String line: BLOCK_GROUPS) {
				writer.append(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			LotTweaks.LOGGER.error("Failed to write config to file");
			e.printStackTrace();
			return;
		}
		LotTweaks.LOGGER.debug("Finished.");
	}

	@SuppressWarnings("serial")
	private static class BlockGroupRegistrationException extends Exception {
	}
	
}
