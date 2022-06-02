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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RotationHelper {

	public static final String ITEMGROUP_CONFFILE_PRIMARY = "LotTweaks-BlockGroups.txt";
	private static final String ITEMGROUP_CONFFILE_SECONDARY = "LotTweaks-BlockGroups2.txt";
/*
	private static final HashMap<ItemState, ItemState> ITEM_CHAIN_PRIMARY = new HashMap<>();
	private static final HashMap<ItemState, ItemState> ITEM_CHAIN_SECONDARY = new HashMap<>();

	private static final String[] DEFAULT_ITEM_GROUP_STRLIST_PRIMARY = {
		"//■ LotTweaks BlockGroups (PRIMARY)",
		"//VANILLA BLOCKS",
		"//STONE",
		toMetaVariationsStr("minecraft:stone", 7),
		"//DIRT",
		toMetaVariationsStr("minecraft:dirt", 3),
		"//PLANKS",
		toMetaVariationsStr("minecraft:planks", 6),
		"//SAPLING",
		toMetaVariationsStr("minecraft:sapling", 6),
		"//ORE series",
		"minecraft:gold_ore,minecraft:iron_ore,minecraft:coal_ore,minecraft:lapis_ore,minecraft:diamond_ore,minecraft:redstone_ore,minecraft:emerald_ore",
		"//LOG + LOG2",
		toMetaVariationsStr("minecraft:log", 4) + ',' + toMetaVariationsStr("minecraft:log2", 2),
		"//LEAVES + LEAVES2",
		toMetaVariationsStr("minecraft:leaves", 4) + ',' + toMetaVariationsStr("minecraft:leaves2", 2),
		"//SPONGE",
		toMetaVariationsStr("minecraft:sponge", 2),
		"//SANDSTONE",
		toMetaVariationsStr("minecraft:sandstone", 3),
		"//WOOL",
		toColorVariationsStr("minecraft:wool"),
		"//RED_FLOWER",
		toMetaVariationsStr("minecraft:red_flower", 9),
		"//Mineral Blocks",
		"minecraft:iron_block,minecraft:gold_block,minecraft:diamond_block,minecraft:emerald_block",
//		"//DOUBLE_STONE_SLAB",
//		toRotateStr("minecraft:double_stone_slab", 10),
		"//STONE_SLAB except meta-2",
		"minecraft:stone_slab/0,minecraft:stone_slab/1,minecraft:stone_slab/3,minecraft:stone_slab/4,minecraft:stone_slab/5,minecraft:stone_slab/6,minecraft:stone_slab/7",
		"//OAK_STAIRS series (Wood Stairs)",
		"minecraft:oak_stairs,minecraft:spruce_stairs,minecraft:birch_stairs,minecraft:jungle_stairs,minecraft:acacia_stairs,minecraft:dark_oak_stairs",
		"//DOOR series",
		"minecraft:wooden_door,minecraft:iron_door,minecraft:spruce_door,minecraft:birch_door,minecraft:jungle_door,minecraft:acacia_door,minecraft:dark_oak_door",
		"//FENCE series",
		"minecraft:fence,minecraft:spruce_fence,minecraft:birch_fence,minecraft:jungle_fence,minecraft:dark_oak_fence,minecraft:acacia_fence,minecraft:nether_brick_fence",
		"//STAINED_GLASS",
		toColorVariationsStr("minecraft:stained_glass"),
		"//MONSTER_EGG",
		toMetaVariationsStr("minecraft:monster_egg", 6),
		"//STONEBRICK",
		toMetaVariationsStr("minecraft:stonebrick", 4),
		"//FENCE_GATE series",
		"minecraft:fence_gate,minecraft:spruce_fence_gate,minecraft:birch_fence_gate,minecraft:jungle_fence_gate,minecraft:dark_oak_fence_gate,minecraft:acacia_fence_gate",
//		"//DOUBLE_WOODEN_SLAB",
//		toRotateStr("minecraft:double_wooden_slab", 6),
		"//WOODEN_SLAB",
		toMetaVariationsStr("minecraft:wooden_slab", 6),
		"//COBBLESTONE_WALL",
		toMetaVariationsStr("minecraft:cobblestone_wall", 2),
		"//QUARTZ_BLOCK",
		toMetaVariationsStr("minecraft:quartz_block", 3),
		"//STAINED_HARDENED_CLAY",
		toColorVariationsStr("minecraft:stained_hardened_clay"),
		"//STAINED_GLASS_PANE",
		toColorVariationsStr("minecraft:stained_glass_pane"),
		"//PRISMARINE",
		toMetaVariationsStr("minecraft:prismarine", 3),
		"//CARPET",
		toColorVariationsStr("minecraft:carpet"),
		"//DOUBLE_PLANT",
		toMetaVariationsStr("minecraft:double_plant", 6),
		"//RED_SANDSTONE",
		toMetaVariationsStr("minecraft:red_sandstone", 3),
		"//SHULKER_BOX series",
		"minecraft:white_shulker_box,minecraft:orange_shulker_box,minecraft:magenta_shulker_box,minecraft:light_blue_shulker_box,minecraft:yellow_shulker_box,minecraft:lime_shulker_box,minecraft:pink_shulker_box,minecraft:gray_shulker_box,minecraft:silver_shulker_box,minecraft:cyan_shulker_box,minecraft:purple_shulker_box,minecraft:blue_shulker_box,minecraft:brown_shulker_box,minecraft:green_shulker_box,minecraft:red_shulker_box,minecraft:black_shulker_box",
		"//GLAZED_TERRACOTTA series",
		"minecraft:white_glazed_terracotta,minecraft:orange_glazed_terracotta,minecraft:magenta_glazed_terracotta,minecraft:light_blue_glazed_terracotta,minecraft:yellow_glazed_terracotta,minecraft:lime_glazed_terracotta,minecraft:pink_glazed_terracotta,minecraft:gray_glazed_terracotta,minecraft:silver_glazed_terracotta,minecraft:cyan_glazed_terracotta,minecraft:purple_glazed_terracotta,minecraft:blue_glazed_terracotta,minecraft:brown_glazed_terracotta,minecraft:green_glazed_terracotta,minecraft:red_glazed_terracotta,minecraft:black_glazed_terracotta",
		"//CONCRETE",
		toColorVariationsStr("minecraft:concrete"),
		"//CONCRETE_POWDER",
		toColorVariationsStr("minecraft:concrete_powder"),
		"//VANILLA ITEMS",
		"//TOOLS",
		"minecraft:wooden_axe,minecraft:compass,minecraft:clock",
	};

	private static final String[] DEFAULT_ITEM_GROUP_STRLIST_SECONDARY = {
		"//■ LotTweaks BlockGroups (SECONDARY)",
		"//WHITE",
		toSameColorsStr(0),
		"//ORANGE",
		toSameColorsStr(1),
		"//MAGENTA",
		toSameColorsStr(2),
		"//LIGHT BLUE",
		toSameColorsStr(3),
		"//YELLOW",
		toSameColorsStr(4),
		"//LIME",
		toSameColorsStr(5),
		"//PINK",
		toSameColorsStr(6),
		"//GRAY",
		toSameColorsStr(7),
		"//LIGHT GRAY",
		toSameColorsStr(8),
		"//CYAN",
		toSameColorsStr(9),
		"//PURPLE",
		toSameColorsStr(10),
		"//BLUE",
		toSameColorsStr(11),
		"//BROWN",
		toSameColorsStr(12),
		"//GREEN",
		toSameColorsStr(13),
		"//RED",
		toSameColorsStr(14),
		"//BLACK",
		toSameColorsStr(15),
	};
*/
	public static List<String> ITEM_GROUPS_STRLIST_PRIMARY = new ArrayList<>();
	private static final List<String> ITEM_GROUPS_STRLIST_SECONDARY = new ArrayList<>();

	public static final List<String> LOG_GROUP_CONFIG = new ArrayList<>();

	private enum Group {
		PRIMARY,
		SECONDARY,
	}

	private static void warnGroupConfigErrors(String msg, int lineCount, Group group) {
//		String fullMsg =  String.format("%s (Line %d of %s group)", msg, lineCount, group.name());
//		LOG_GROUP_CONFIG.add(fullMsg);
//		LotTweaks.LOGGER.warn(fullMsg);
	}
/*
	private static String toMetaVariationsStr(String name, int max) {
		StringJoiner joiner = new StringJoiner(",");
		for (int i=0;i<max;i++) {
			joiner.add(String.format("%s/%d", name, i));
		}
		return joiner.toString();
	}

	private static String toColorVariationsStr(String name) {
		StringJoiner joiner = new StringJoiner(",");
		int[] metas = new int[] {14, 1, 4, 5, 13, 9, 3, 11, 10, 2, 6, 12, 15, 7, 8, 0};
		for (int meta : metas) {
			joiner.add(String.format("%s/%d", name, meta));
		}
		return joiner.toString();
	}

	private static String toSameColorsStr(int meta) {
		String format = "minecraft:wool/N,minecraft:stained_glass/N,minecraft:stained_hardened_clay/N,minecraft:stained_glass_pane/N,minecraft:carpet/N,minecraft:concrete/N,minecraft:concrete_powder/N";
		return format.replace("N", String.valueOf(meta));
	}

	private static HashMap<ItemState, ItemState> getItemChain(Group group) {
		return (group == Group.PRIMARY) ? ITEM_CHAIN_PRIMARY : ITEM_CHAIN_SECONDARY;
	}
*/
	private static List<String> getItemGroupStrList(Group group) {
		return (group == Group.PRIMARY) ? ITEM_GROUPS_STRLIST_PRIMARY : ITEM_GROUPS_STRLIST_SECONDARY;
	}

	private static String getFileName(Group group) {
		return (group == Group.PRIMARY) ? ITEMGROUP_CONFFILE_PRIMARY : ITEMGROUP_CONFFILE_SECONDARY;
	}
/*
	public static boolean canRotate(ItemStack itemStack, Group group) {
		if (itemStack == null || itemStack.isEmpty()) {
			return false;
		}
		return getItemChain(group).containsKey(new ItemState(itemStack));
	}

	public static List<ItemStack> getAllRotateResult(ItemStack itemStack, Group group){
		List<ItemStack> stacks = new ArrayList<>();
		if (itemStack == null || itemStack.isEmpty()) {
			return null;
		}
		ItemState srcState = new ItemState(itemStack);
		if (!getItemChain(group).containsKey(srcState)) {
			return null;
		}
		stacks.add(itemStack);
		ItemState state = getItemChain(group).get(srcState);
		int counter = 0;
		while (!state.equals(srcState)) {
			stacks.add(state.toItemStack());
			state = getItemChain(group).get(state);
			counter++;
			if (counter >= 50000) {
				LotTweaks.LOGGER.error("infinite loop!");
				return null;
			}
		}
		return stacks;
	}

	public static boolean loadAllItemGroupFromStrArray() {
		LOG_GROUP_CONFIG.clear();
		boolean flag = true;
		for(Group group : Group.values()) {
			flag &= loadItemGroupFromStrArray(group);
		}
		return flag;
	}
*/

	public static List<List<ItemState>> loadPrimaryGroup() {
		return loadItemGroupFromStrArray(Group.PRIMARY);
	}

	public static List<List<ItemState>> loadSecondaryGroup() {
		return loadItemGroupFromStrArray(Group.SECONDARY);
	}

	private static List<List<ItemState>> loadItemGroupFromStrArray(Group group) {
		List<List<ItemState>> res = new ArrayList<>();

		HashMap<ItemState, ItemState> newItemChain = new HashMap<>();
		try {
			int lineCount = 0;
			for (String line: getItemGroupStrList(group)) {
				lineCount++;
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				List<ItemState> states = new ArrayList<>();
				for (String part: line.split(",")) {
					String itemStr;
					int meta;
					if (part.contains("/")) {
						String[] name_meta = part.split("/");
						itemStr = name_meta[0];
						meta = Integer.parseInt(name_meta[1]);
					} else {
						itemStr = part;
						meta = 0;
					}
					if (itemStr.equals("minecraft:double_stone_slab") || itemStr.equals("minecraft:double_wooden_slab")) {
						warnGroupConfigErrors(String.format("'%s' is no longer supported.", part), lineCount, group);
						continue;
					}
					Item item = Item.getByNameOrId(itemStr);
					if (item == null || item == Items.AIR) {
						Block block = Block.getBlockFromName(itemStr);
						if (block == null || block == Blocks.AIR) {
							warnGroupConfigErrors(String.format("'%s' was not found.", itemStr), lineCount, group);
							continue;
						}
						item = Item.getItemFromBlock(block);
					}
					if (item == null || item == Items.AIR) {
						warnGroupConfigErrors(String.format("'%s' is not supported.", part), lineCount, group);
						continue;
					}
					ItemState state = new ItemState(new ItemStack(item, 1, meta));
					if (states.contains(state) || newItemChain.containsKey(state)) {
						warnGroupConfigErrors(String.format("'%s' is duplicated.", part), lineCount, group);
						continue;
					}
					states.add(state);
				}
				if (states.size() <= 1) {
					warnGroupConfigErrors(String.format("The group size is %d.", states.size()), lineCount, group);
					continue;
				}
				for (int i=0;i<states.size();i++) {
					newItemChain.put(states.get(i), states.get((i+1)%states.size()));
				}
				res.add(states);
			}
		} catch (Exception e) {
			LotTweaks.LOGGER.error(e);
//			return false;
		}
//		getItemChain(group).clear();
//		getItemChain(group).putAll(newItemChain);
//		return true;
		return res;
	}
/*
	public static boolean tryToAddItemGroupFromCommand(String newItemGroup, Group group) {
		List<String> strList = getItemGroupStrList(group);
		strList.add(newItemGroup);
		boolean succeeded = loadItemGroupFromStrArray(group);
		if (succeeded) {
			writeToFile(group);
			return true;
		} else {
			strList.remove(strList.size()-1);
			return false;
		}
	}
*/
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
//				writeToFile(group);
			} else {
				List<String> listFromFile = loadFile(file);
				List<String> listOnMemory = getItemGroupStrList(group);
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
/*
	public static void writeAllToFile() {
		for(Group group : Group.values()) {
			writeToFile(group);
		}
	}

	private static void writeToFile(Group group) {
		LotTweaks.LOGGER.debug("Write config to file.");
		String filename = (group == Group.PRIMARY ? ITEMGROUP_CONFFILE_PRIMARY : ITEMGROUP_CONFFILE_SECONDARY);
		File file = new File(new File("config"), filename);
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
			for (String line: getItemGroupStrList(group)) {
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
*/
}
