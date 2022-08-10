package com.github.aruma256.lottweaks.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class V2ConfigLoader {

	private static final File ITEMGROUP_CONFFILE_PRIMARY = new File(new File("config"), "LotTweaks-BlockGroups.txt");
	private static final File ITEMGROUP_CONFFILE_SECONDARY = new File(new File("config"), "LotTweaks-BlockGroups2.txt");

	public static boolean V2configFileExists() {
		return ITEMGROUP_CONFFILE_PRIMARY.exists();
	}

	public static List<List<ItemState>> loadPrimaryGroup() {
		return createGroupList(loadFile(ITEMGROUP_CONFFILE_PRIMARY));
	}

	public static List<List<ItemState>> loadSecondaryGroup() {
		return createGroupList(loadFile(ITEMGROUP_CONFFILE_SECONDARY));
	}

	@Nonnull
	private static List<String> loadFile(File file) {
		if (file.exists()) {
			try {
				return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			} catch (IOException e) {
			}
			try {
				return Files.readAllLines(file.toPath(), Charset.forName("Shift_JIS"));
			} catch (IOException e) {
			}
			try {
				return Files.readAllLines(file.toPath(), Charset.defaultCharset());
			} catch (IOException e) {
				IngameLog.instance.addErrorLog(String.format("Failed to convert config file '%s'", file.getName()));
			}
		}
		return new ArrayList<>();
	}

	@Nonnull
	private static List<List<ItemState>> createGroupList(List<String> lines) {
		List<List<ItemState>> groupList = new ArrayList<>();
		for (String line : lines) {
			List<ItemState> group = createGroup(line);
			if (group != null) groupList.add(group);
		}
		
		return groupList;
	}

	@Nullable
	private static List<ItemState> createGroup(String line) {
		if (line.isEmpty() || line.startsWith("//")) return null;
		List<ItemState> group = new ArrayList<>();
		for (String itemStateStr : line.split(",")) {
			ItemState itemState = createItemState(itemStateStr);
			if (itemState != null) group.add(itemState);
		}
		return group;
	}

	@Nullable
	private static ItemState createItemState(String itemStateStr) {
		String itemName;
		int meta;
		if (itemStateStr.contains("/")) {
			String[] tmp = itemStateStr.split("/");
			itemName = tmp[0];
			try {
				meta = Integer.parseInt(tmp[1]);
			} catch (Exception e) {
				IngameLog.instance.addErrorLog(String.format("Could not parse '%s'", itemStateStr));
				return null;
			}
		} else {
			itemName = itemStateStr;
			meta = 0;
		}
		ResourceLocation itemResource = new ResourceLocation(itemName);
		if (!ForgeRegistries.ITEMS.containsKey(itemResource)) {
			IngameLog.instance.addErrorLog(String.format("'%s' was not found", itemName));
			return null;
		}
		Item item = ForgeRegistries.ITEMS.getValue(itemResource);
		if (item == Items.AIR) {
			IngameLog.instance.addErrorLog(String.format("'%s' is not supported", itemName));
			return null;
		}
		ItemStack itemStack = new ItemStack(item);
		itemStack.setDamageValue(meta);
		return new ItemState(itemStack);
	}

}
