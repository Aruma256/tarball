package com.github.lotqwerty.lottweaks.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class RotationHelper {

	public static final String BLOCKGROUP_CONFFILE_MAIN = "LotTweaks-BlockGroups.txt";
	private static final String BLOCKGROUP_CONFFILE_SUB = "LotTweaks-BlockGroups2.txt";

	private static final HashMap<IBlockState, IBlockState> BLOCK_CHAIN_MAIN = new HashMap<>();
	private static final HashMap<IBlockState, IBlockState> BLOCK_CHAIN_SUB = new HashMap<>();

	private static final String[] DEFAULT_BLOCK_GROUP_STRLIST_MAIN = {
		"//VANILLA BLOCKS",
		"//STONE",
		toRotateStr("minecraft:stone", 7),
		"//DIRT",
		toRotateStr("minecraft:dirt", 3),
		"//PLANKS",
		toRotateStr("minecraft:planks", 6),
		"//SAPLING",
		toRotateStr("minecraft:sapling", 6),
		"//ORE series",
		"minecraft:gold_ore,minecraft:iron_ore,minecraft:coal_ore,minecraft:lapis_ore,minecraft:diamond_ore,minecraft:redstone_ore,minecraft:emerald_ore",
		"//LOG + LOG2",
		toRotateStr("minecraft:log", 4) + ',' + toRotateStr("minecraft:log2", 2),
		"//LEAVES + LEAVES2",
		toRotateStr("minecraft:leaves", 4) + ',' + toRotateStr("minecraft:leaves2", 2),
		"//SPONGE",
		toRotateStr("minecraft:sponge", 2),
		"//SANDSTONE",
		toRotateStr("minecraft:sandstone", 3),
		"//WOOL",
		toRotateStr("minecraft:wool", 16),
		"//RED_FLOWER",
		toRotateStr("minecraft:red_flower", 9),
		"//Mineral Blocks",
		"minecraft:iron_block,minecraft:gold_block,minecraft:diamond_block,minecraft:emerald_block",
		"//DOUBLE_STONE_SLAB",
		toRotateStr("minecraft:double_stone_slab", 10),
		"//STONE_SLAB except meta-2",
		"minecraft:stone_slab/0,minecraft:stone_slab/1,minecraft:stone_slab/3,minecraft:stone_slab/4,minecraft:stone_slab/5,minecraft:stone_slab/6,minecraft:stone_slab/7",
		"//OAK_STAIRS series (Wood Stairs)",
		"minecraft:oak_stairs,minecraft:spruce_stairs,minecraft:birch_stairs,minecraft:jungle_stairs,minecraft:acacia_stairs,minecraft:dark_oak_stairs",
		"//DOOR series",
		"minecraft:wooden_door,minecraft:iron_door,minecraft:spruce_door,minecraft:birch_door,minecraft:jungle_door,minecraft:acacia_door,minecraft:dark_oak_door",
		"//FENCE series",
		"minecraft:fence,minecraft:spruce_fence,minecraft:birch_fence,minecraft:jungle_fence,minecraft:dark_oak_fence,minecraft:acacia_fence,minecraft:nether_brick_fence",
		"//STAINED_GLASS",
		toRotateStr("minecraft:stained_glass", 16),
		"//MONSTER_EGG",
		toRotateStr("minecraft:monster_egg", 6),
		"//STONEBRICK",
		toRotateStr("minecraft:stonebrick", 4),
		"//FENCE_GATE series",
		"minecraft:fence_gate,minecraft:spruce_fence_gate,minecraft:birch_fence_gate,minecraft:jungle_fence_gate,minecraft:dark_oak_fence_gate,minecraft:acacia_fence_gate",
		"//DOUBLE_WOODEN_SLAB",
		toRotateStr("minecraft:double_wooden_slab", 6),
		"//WOODEN_SLAB",
		toRotateStr("minecraft:wooden_slab", 6),
		"//COBBLESTONE_WALL",
		toRotateStr("minecraft:cobblestone_wall", 2),
		"//QUARTZ_BLOCK",
		toRotateStr("minecraft:quartz_block", 3),
		"//STAINED_HARDENED_CLAY",
		toRotateStr("minecraft:stained_hardened_clay", 16),
		"//STAINED_GLASS_PANE",
		toRotateStr("minecraft:stained_glass_pane", 16),
		"//PRISMARINE",
		toRotateStr("minecraft:prismarine", 3),
		"//CARPET",
		toRotateStr("minecraft:carpet", 16),
		"//DOUBLE_PLANT",
		toRotateStr("minecraft:double_plant", 6),
		"//RED_SANDSTONE",
		toRotateStr("minecraft:red_sandstone", 3),
		"//SHULKER_BOX series",
		"minecraft:white_shulker_box,minecraft:orange_shulker_box,minecraft:magenta_shulker_box,minecraft:light_blue_shulker_box,minecraft:yellow_shulker_box,minecraft:lime_shulker_box,minecraft:pink_shulker_box,minecraft:gray_shulker_box,minecraft:silver_shulker_box,minecraft:cyan_shulker_box,minecraft:purple_shulker_box,minecraft:blue_shulker_box,minecraft:brown_shulker_box,minecraft:green_shulker_box,minecraft:red_shulker_box,minecraft:black_shulker_box",
		"//GLAZED_TERRACOTTA series",
		"minecraft:white_glazed_terracotta,minecraft:orange_glazed_terracotta,minecraft:magenta_glazed_terracotta,minecraft:light_blue_glazed_terracotta,minecraft:yellow_glazed_terracotta,minecraft:lime_glazed_terracotta,minecraft:pink_glazed_terracotta,minecraft:gray_glazed_terracotta,minecraft:silver_glazed_terracotta,minecraft:cyan_glazed_terracotta,minecraft:purple_glazed_terracotta,minecraft:blue_glazed_terracotta,minecraft:brown_glazed_terracotta,minecraft:green_glazed_terracotta,minecraft:red_glazed_terracotta,minecraft:black_glazed_terracotta",
		"//CONCRETE",
		toRotateStr("minecraft:concrete", 16),
		"//CONCRETE_POWDER",
		toRotateStr("minecraft:concrete_powder", 16),
	};

	private static final String[] DEFAULT_BLOCK_GROUP_STRLIST_SUB = {
		"//WHITE",
		toSameColors(0),
		"//ORANGE",
		toSameColors(1),
		"//MAGENTA",
		toSameColors(2),
		"//LIGHT BLUE",
		toSameColors(3),
		"//YELLOW",
		toSameColors(4),
		"//LIME",
		toSameColors(5),
		"//PINK",
		toSameColors(6),
		"//GRAY",
		toSameColors(7),
		"//LIGHT GRAY",
		toSameColors(8),
		"//CYAN",
		toSameColors(9),
		"//PURPLE",
		toSameColors(10),
		"//BLUE",
		toSameColors(11),
		"//BROWN",
		toSameColors(12),
		"//GREEN",
		toSameColors(13),
		"//RED",
		toSameColors(14),
		"//BLACK",
		toSameColors(15),
	};

	public static List<String> BLOCK_GROUPS_STRLIST_MAIN = new ArrayList<>(Arrays.asList(DEFAULT_BLOCK_GROUP_STRLIST_MAIN));
	private static final List<String> BLOCK_GROUPS_STRLIST_SUB = new ArrayList<>(Arrays.asList(DEFAULT_BLOCK_GROUP_STRLIST_SUB));

	public enum Group {
		MAIN,
		SUB,
	}

	private static String toRotateStr(String name, int max) {
		StringJoiner joiner = new StringJoiner(",");
		for (int i=0;i<max;i++) {
			joiner.add(String.format("%s/%d", name, i));
		}
		return joiner.toString();
	}

	private static String toSameColors(int meta) {
		String format = "minecraft:wool/N,minecraft:stained_glass/N,minecraft:stained_hardened_clay/N,minecraft:stained_glass_pane/N,minecraft:carpet/N,minecraft:concrete/N,minecraft:concrete_powder/N";
		return format.replace("N", String.valueOf(meta));
	}

	private static HashMap<IBlockState, IBlockState> getBlockChain(Group group) {
		return (group == Group.MAIN) ? BLOCK_CHAIN_MAIN : BLOCK_CHAIN_SUB;
	}

	private static List<String> getBlockGroupStrList(Group group) {
		return (group == Group.MAIN) ? BLOCK_GROUPS_STRLIST_MAIN : BLOCK_GROUPS_STRLIST_SUB;
	}

	private static String getFileName(Group group) {
		return (group == Group.MAIN) ? BLOCKGROUP_CONFFILE_MAIN : BLOCKGROUP_CONFFILE_SUB;
	}

	@SuppressWarnings("deprecation")
	public static boolean canRotate(ItemStack itemStack, Group group) {
		if (itemStack == null || itemStack.isEmpty()) {
			return false;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == Blocks.AIR) {
			return false;
		}
		int meta = itemStack.getItemDamage();
		return getBlockChain(group).containsKey(block.getStateFromMeta(meta));
	}
	
	private static ItemStack toItemStack(IBlockState state) {
		try {
			ItemStack stack = state.getBlock().getPickBlock(state, null, null, null, null);
			if (stack != null && !stack.isEmpty()) {
				return stack;
			}
		} catch (Exception e) {
		}
		return new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
	}

	@SuppressWarnings("deprecation")
	public static List<ItemStack> getAllRotateResult(ItemStack itemStack, Group group){
		List<ItemStack> stacks = new ArrayList<>();
		if (itemStack == null || itemStack.isEmpty()) {
			return null;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == Blocks.AIR) {
			return null;
		}
		int meta = itemStack.getItemDamage();
		IBlockState srcState = block.getStateFromMeta(meta);
		if (!getBlockChain(group).containsKey(srcState)) {
			return null;
		}
		stacks.add(itemStack);
		IBlockState state = getBlockChain(group).get(srcState);
		int counter = 0;
		while (state != srcState) {
			stacks.add(toItemStack(state));
			state = getBlockChain(group).get(state);
			counter++;
			if (counter >= 50000) {
				LotTweaks.LOGGER.error("infinite loop!");
				return null;
			}
		}
		return stacks;
	}

	public static boolean loadAllBlockGroupFromStrArray() {
		boolean flag = true;
		for(Group group : Group.values()) {
			flag &= loadBlockGroupFromStrArray(group);
		}
		return flag;
	}

	@SuppressWarnings("deprecation")
	private static boolean loadBlockGroupFromStrArray(Group group) {
		HashMap<IBlockState, IBlockState> newBlockChain = new HashMap<>();
		try {
			int lineCount = 0;
			for (String line: getBlockGroupStrList(group)) {
				lineCount++;
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				List<IBlockState> states = new ArrayList<>();
				for (String part: line.split(",")) {
					String blockName;
					int meta;
					if (part.contains("/")) {
						String[] name_meta = part.split("/");
						blockName = name_meta[0];
						meta = Integer.parseInt(name_meta[1]);
					} else {
						blockName = part;
						meta = 0;
					}
					Block block = Block.getBlockFromName(blockName);
					if (block == null || block == Blocks.AIR) {
						LotTweaks.LOGGER.error(String.format("Not found: '%s'", part));
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					IBlockState state = block.getStateFromMeta(meta);
					states.add(state);
				}
				if (states.size() <= 1) {
					LotTweaks.LOGGER.error("Failed to load group: '%s'", line);
					LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
					throw new BlockGroupRegistrationException();
				}
				for (int i=0;i<states.size();i++) {
					if (newBlockChain.containsKey(states.get(i))) {
						LotTweaks.LOGGER.error("BLOCK_GROUPS value is invalid.");
						LotTweaks.LOGGER.error(String.format("(BLOCK_GROUPS line %d)", lineCount));
						throw new BlockGroupRegistrationException();
					}
					newBlockChain.put(states.get(i), states.get((i+1)%states.size()));
				}
				LotTweaks.LOGGER.debug(String.format("BLOCK_GROUPS line %d: OK", lineCount));
			}
		} catch (BlockGroupRegistrationException e) {
			return false;
		} catch (Exception e) {
			LotTweaks.LOGGER.error(e);
			return false;
		}
		getBlockChain(group).clear();
		getBlockChain(group).putAll(newBlockChain);
		return true;
	}

	public static boolean tryToAddBlockGroupFromCommand(String newBlockGroup, Group group) {
		List<String> strList = getBlockGroupStrList(group);
		strList.add(newBlockGroup);
		boolean succeeded = loadBlockGroupFromStrArray(group);
		if (succeeded) {
			writeToFile(group);
			return true;
		} else {
			strList.remove(strList.size()-1);
			return false;
		}
	}

	public static boolean loadAllFromFile() {
		boolean flag = true;
		for(Group group : Group.values()) {
			flag &= loadFromFile(group);
		}
		return flag;
	}

	private static boolean loadFromFile(Group group) {
		File file = new File(new File("config"), getFileName(group));
		try {
			if (!file.exists()) {
				LotTweaks.LOGGER.debug("Config file does not exist.");
				writeToFile(group);
			} else {
				List<String> listFromFile = loadFile(file);
				List<String> listOnMemory = getBlockGroupStrList(group);
				listOnMemory.clear();
				listOnMemory.addAll(listFromFile);
			}
		} catch (IOException e) {
			LotTweaks.LOGGER.error(String.format("Failed to load config from file (Group: %s)", group.name()));
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static List<String> loadFile(File file) throws IOException{
		try {
			return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
		}
		try {
			return Files.readAllLines(file.toPath(), Charset.forName("Shift_JIS"));
		} catch (IOException e) {
		}
		return Files.readAllLines(file.toPath(), Charset.defaultCharset());
	}

	public static void writeAllToFile() {
		for(Group group : Group.values()) {
			writeToFile(group);
		}
	}

	private static void writeToFile(Group group) {
		LotTweaks.LOGGER.debug("Write config to file.");
		String filename = (group == Group.MAIN ? BLOCKGROUP_CONFFILE_MAIN : BLOCKGROUP_CONFFILE_SUB);
		File file = new File(new File("config"), filename);
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			for (String line: getBlockGroupStrList(group)) {
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
