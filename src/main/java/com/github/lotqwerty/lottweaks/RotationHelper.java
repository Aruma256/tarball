package com.github.lotqwerty.lottweaks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RotationHelper {

	protected static final String BLOCKGROUP_CONFFILE = "LotTweaks-BlockGroups.txt";

	public static final HashMap<Block, Block> BLOCK_CHAIN = new HashMap<>();

	public static final String[] DEFAULT_BLOCK_GROUPS = {
			"//VANILLA BLOCKS",
			"//STONE",
			"minecraft:stone,minecraft:granite,minecraft:polished_granite,minecraft:diorite,minecraft:polished_diorite,minecraft:andesite,minecraft:polished_andesite",
			"//DIRT",
			"minecraft:dirt,minecraft:coarse_dirt,minecraft:podzol",
			"//WOOL",
			"minecraft:white_wool,minecraft:orange_wool,minecraft:magenta_wool,minecraft:light_blue_wool,minecraft:yellow_wool,minecraft:lime_wool,minecraft:pink_wool,minecraft:gray_wool,minecraft:light_gray_wool,minecraft:cyan_wool,minecraft:purple_wool,minecraft:blue_wool,minecraft:brown_wool,minecraft:green_wool,minecraft:red_wool,minecraft:black_wool",
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
					ResourceLocation resourceLocation = new ResourceLocation(blockName);
					if (!ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {
						LotTweaks.LOGGER.error(String.format("Not found: '%s'", part));
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
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
