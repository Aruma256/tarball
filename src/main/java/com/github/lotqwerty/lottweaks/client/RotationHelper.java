package com.github.lotqwerty.lottweaks.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RotationHelper {

	public static final String ITEMGROUP_CONFFILE_PRIMARY = "LotTweaks-BlockGroups.txt";
	private static final String ITEMGROUP_CONFFILE_SECONDARY = "LotTweaks-BlockGroups2.txt";

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

	private static List<String> getItemGroupStrList(Group group) {
		return (group == Group.PRIMARY) ? ITEM_GROUPS_STRLIST_PRIMARY : ITEM_GROUPS_STRLIST_SECONDARY;
	}

	private static String getFileName(Group group) {
		return (group == Group.PRIMARY) ? ITEMGROUP_CONFFILE_PRIMARY : ITEMGROUP_CONFFILE_SECONDARY;
	}

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

}
