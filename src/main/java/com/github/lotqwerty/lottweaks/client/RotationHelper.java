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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RotationHelper {

	public static final String ITEMGROUP_CONFFILE_MAIN = "LotTweaks-BlockGroups.txt";
	private static final String ITEMGROUP_CONFFILE_SUB = "LotTweaks-BlockGroups2.txt";

	private static final HashMap<Item, Item> ITEM_CHAIN_MAIN = new HashMap<>();
	private static final HashMap<Item, Item> ITEM_CHAIN_SUB = new HashMap<>();
	public static final String[] DEFAULT_ITEM_GROUP_STRLIST_MAIN = {
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
		toColorVariationsStr("minecraft:COLOR_wool"),
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
		toColorVariationsStr("minecraft:COLOR_stained_glass"),
		"//INFESTED STONE BRICKS",
		"minecraft:infested_stone_bricks,minecraft:infested_mossy_stone_bricks,minecraft:infested_cracked_stone_bricks,minecraft:infested_chiseled_stone_bricks",
		"//FENCE GATE",
		"minecraft:oak_fence_gate,minecraft:spruce_fence_gate,minecraft:birch_fence_gate,minecraft:jungle_fence_gate,minecraft:acacia_fence_gate,minecraft:dark_oak_fence_gate,minecraft:crimson_fence_gate,minecraft:warped_fence_gate",
		"//STONEBRICKS",
		"minecraft:stone_bricks,minecraft:mossy_stone_bricks,minecraft:cracked_stone_bricks,minecraft:chiseled_stone_bricks",
		"//WALL",
		"minecraft:cobblestone_wall,minecraft:mossy_cobblestone_wall,minecraft:brick_wall,minecraft:prismarine_wall,minecraft:red_sandstone_wall,minecraft:mossy_stone_brick_wall,minecraft:granite_wall,minecraft:stone_brick_wall,minecraft:nether_brick_wall,minecraft:andesite_wall,minecraft:red_nether_brick_wall,minecraft:sandstone_wall,minecraft:end_stone_brick_wall,minecraft:diorite_wall,minecraft:blackstone_wall,minecraft:polished_blackstone_wall,minecraft:polished_blackstone_brick_wall",
		"//CARPET",
		toColorVariationsStr("minecraft:COLOR_carpet"),
		"//QUARTZ BLOCK",
		"minecraft:chiseled_quartz_block,minecraft:quartz_block,minecraft:quartz_bricks,minecraft:quartz_pillar",
		"//TERRACOTTA",
		"minecraft:terracotta," + toColorVariationsStr("minecraft:COLOR_terracotta"),
		"//STAINED GLASS PANE",
		toColorVariationsStr("minecraft:COLOR_stained_glass_pane"),
		"//PRISMARINE",
		"minecraft:prismarine,minecraft:prismarine_bricks,minecraft:dark_prismarine",
		"//SHULKER BOX",
		"minecraft:shulker_box," + toColorVariationsStr("minecraft:COLOR_shulker_box"),
		"//GLAZED TERRACOTTA",
		toColorVariationsStr("minecraft:COLOR_glazed_terracotta"),
		"//CONCRETE",
		toColorVariationsStr("minecraft:COLOR_concrete"),
		"//CONCRETE POWDER",
		toColorVariationsStr("minecraft:COLOR_concrete_powder"),
		"//CORAL BLOCK",
		"minecraft:dead_tube_coral_block,minecraft:dead_brain_coral_block,minecraft:dead_bubble_coral_block,minecraft:dead_fire_coral_block,minecraft:dead_horn_coral_block,minecraft:tube_coral_block,minecraft:brain_coral_block,minecraft:bubble_coral_block,minecraft:fire_coral_block,minecraft:horn_coral_block",
		"//BLACKSTONE",
		"minecraft:blackstone,minecraft:polished_blackstone,minecraft:chiseled_polished_blackstone,minecraft:polished_blackstone_bricks,minecraft:cracked_polished_blackstone_bricks",
		"//CORAL",
		"minecraft:tube_coral,minecraft:brain_coral,minecraft:bubble_coral,minecraft:fire_coral,minecraft:horn_coral,minecraft:dead_brain_coral,minecraft:dead_bubble_coral,minecraft:dead_fire_coral,minecraft:dead_horn_coral,minecraft:dead_tube_coral",
		"//CORAL FAN",
		"minecraft:tube_coral_fan,minecraft:brain_coral_fan,minecraft:bubble_coral_fan,minecraft:fire_coral_fan,minecraft:horn_coral_fan,minecraft:dead_tube_coral_fan,minecraft:dead_brain_coral_fan,minecraft:dead_bubble_coral_fan,minecraft:dead_fire_coral_fan,minecraft:dead_horn_coral_fan",
		"//BUTTON",
		"minecraft:stone_button,minecraft:oak_button,minecraft:spruce_button,minecraft:birch_button,minecraft:jungle_button,minecraft:acacia_button,minecraft:dark_oak_button,minecraft:crimson_button,minecraft:warped_button,minecraft:polished_blackstone_button",
		"//SIGN",
		"minecraft:oak_sign,minecraft:spruce_sign,minecraft:birch_sign,minecraft:jungle_sign,minecraft:acacia_sign,minecraft:dark_oak_sign,minecraft:crimson_sign,minecraft:warped_sign",
		"//LANTERN",
		"minecraft:lantern,minecraft:soul_lantern",
		"//CAMPFIRE",
		"minecraft:campfire,minecraft:soul_campfire",
		"//VANILLA ITEMS",
		"//TOOLS",
		"minecraft:wooden_axe,minecraft:compass,minecraft:clock",
	};

	private static final String[] DEFAULT_ITEM_GROUP_STRLIST_SUB = {
		"//WHITE",
		toSameColorsStr("white"),
		"//ORANGE",
		toSameColorsStr("orange"),
		"//MAGENTA",
		toSameColorsStr("magenta"),
		"//LIGHT BLUE",
		toSameColorsStr("light_blue"),
		"//YELLOW",
		toSameColorsStr("yellow"),
		"//LIME",
		toSameColorsStr("lime"),
		"//PINK",
		toSameColorsStr("pink"),
		"//GRAY",
		toSameColorsStr("gray"),
		"//LIGHT GRAY",
		toSameColorsStr("light_gray"),
		"//CYAN",
		toSameColorsStr("cyan"),
		"//PURPLE",
		toSameColorsStr("purple"),
		"//BLUE",
		toSameColorsStr("blue"),
		"//BROWN",
		toSameColorsStr("brown"),
		"//GREEN",
		toSameColorsStr("green"),
		"//RED",
		toSameColorsStr("red"),
		"//BLACK",
		toSameColorsStr("black"),
	};

	public static List<String> ITEM_GROUPS_STRLIST_MAIN = new ArrayList<>(Arrays.asList(DEFAULT_ITEM_GROUP_STRLIST_MAIN));
	private static final List<String> ITEM_GROUPS_STRLIST_SUB = new ArrayList<>(Arrays.asList(DEFAULT_ITEM_GROUP_STRLIST_SUB));

	public enum Group {
		MAIN,
		SUB,
	}

	private static String toColorVariationsStr(String name) {
		StringJoiner joiner = new StringJoiner(",");
		String[] colors = new String[] {"red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown", "black", "gray", "light_gray", "white"};
		for (String color : colors) {
			joiner.add(name.replace("COLOR", color));
		}
		return joiner.toString();
	}

	private static String toSameColorsStr(String color) {
		String format = "minecraft:COLOR_wool,minecraft:COLOR_stained_glass,minecraft:COLOR_terracotta,minecraft:COLOR_stained_glass_pane,minecraft:COLOR_carpet,minecraft:COLOR_concrete,minecraft:COLOR_concrete_powder,minecraft:COLOR_shulker_box";
		return format.replace("COLOR", color);
	}

	private static HashMap<Item, Item> getItemChain(Group group) {
		return (group == Group.MAIN) ? ITEM_CHAIN_MAIN : ITEM_CHAIN_SUB;
	}

	private static List<String> getItemGroupStrList(Group group) {
		return (group == Group.MAIN) ? ITEM_GROUPS_STRLIST_MAIN : ITEM_GROUPS_STRLIST_SUB;
	}

	private static String getFileName(Group group) {
		return (group == Group.MAIN) ? ITEMGROUP_CONFFILE_MAIN : ITEMGROUP_CONFFILE_SUB;
	}

	public static boolean canRotate(ItemStack itemStack, Group group) {
		if (itemStack == null || itemStack.isEmpty()) {
			return false;
		}
		Item item = itemStack.getItem();
		if (item == null || item == Items.AIR) {
			return false;
		}
		return getItemChain(group).containsKey(item);
	}
	
	private static ItemStack toItemStack(Item item) {
		return new ItemStack(item);
	}

	public static List<ItemStack> getAllRotateResult(ItemStack itemStack, Group group){
		List<ItemStack> stacks = new ArrayList<>();
		if (itemStack == null || itemStack.isEmpty()) {
			return null;
		}
		Item srcItem = itemStack.getItem();
		if (srcItem == null || srcItem == Items.AIR) {
			return null;
		}
		if (!getItemChain(group).containsKey(srcItem)) {
			return null;
		}
		stacks.add(itemStack);
		Item item = getItemChain(group).get(srcItem);
		int counter = 0;
		while (item != srcItem) {
			stacks.add(toItemStack(item));
			item = getItemChain(group).get(item);
			counter++;
			if (counter >= 50000) {
				LotTweaks.LOGGER.error("infinite loop!");
				return null;
			}
		}
		return stacks;
	}

	public static boolean loadAllItemGroupFromStrArray() {
		boolean flag = true;
		for(Group group : Group.values()) {
			flag &= loadItemGroupFromStrArray(group);
		}
		return flag;
	}

	private static boolean loadItemGroupFromStrArray(Group group) {
		HashMap<Item, Item> newItemChain = new HashMap<>();
		try {
			int lineCount = 0;
			for (String line: getItemGroupStrList(group)) {
				lineCount++;
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				List<Item> items = new ArrayList<>();
				for (String itemStr: line.split(",")) {
					Identifier resourceLocation = new Identifier(itemStr);
					Item item = Registry.ITEM.get(resourceLocation);
					if (item == null || item == Items.AIR) {
						LotTweaks.LOGGER.warn(String.format("'%s' is not supported", itemStr));
						throw new ItemGroupRegistrationException();
					}
					items.add(item);
				}
				if (items.size() <= 1) {
					LotTweaks.LOGGER.warn(String.format("group size is %d", items.size()));
					continue;
				}
				for (int i=0;i<items.size();i++) {
					if (newItemChain.containsKey(items.get(i))) {
						LotTweaks.LOGGER.error("GROUPS value is invalid.");
						LotTweaks.LOGGER.error(String.format("(GROUPS line %d)", lineCount));
						throw new ItemGroupRegistrationException();
					}
					newItemChain.put(items.get(i), items.get((i+1)%items.size()));
				}
				LotTweaks.LOGGER.debug(String.format("GROUPS line %d: OK", lineCount));
			}
		} catch (ItemGroupRegistrationException e) {
			return false;
		} catch (Exception e) {
			LotTweaks.LOGGER.error(e);
			return false;
		}
		getItemChain(group).clear();
		getItemChain(group).putAll(newItemChain);
		return true;
	}

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

	public static void writeAllToFile() {
		for(Group group : Group.values()) {
			writeToFile(group);
		}
	}

	private static void writeToFile(Group group) {
		LotTweaks.LOGGER.debug("Write config to file.");
		String filename = (group == Group.MAIN ? ITEMGROUP_CONFFILE_MAIN : ITEMGROUP_CONFFILE_SUB);
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

	@SuppressWarnings("serial")
	private static class ItemGroupRegistrationException extends Exception {
	}
	
}
