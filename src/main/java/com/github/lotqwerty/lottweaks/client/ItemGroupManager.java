package com.github.lotqwerty.lottweaks.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ItemGroupManager {

	private static final String JSON_INDENT = "  ";
	public static final File CONFIG_FILE = new File(new File("config"), "LotTweaks-ItemGroup.json");
	public static final String[] LOG_GROUP_CONFIG = {"Not implemented"};

	private static ItemGroupManager instance;

	public static enum Group {
		PRIMARY,
		SECONDARY
	}

	public static ItemGroupManager getInstance() {
		return instance;
	}

	public static void init() {
		if (!CONFIG_FILE.exists()) {
			if (oldFileExists()) {
				convertOldFile();
			}
		}
		instance = loadFromFile();
	}

	private static ItemGroupManager loadFromFile() {
		JsonObject json;
		List<List<ItemState>> primaryGroupList;
		List<List<ItemState>> secondaryGroupList;
		try {
			json = new JsonParser().parse(new JsonReader(new BufferedReader(new FileReader(CONFIG_FILE)))).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			primaryGroupList = readGroupFromJsonFile(json.get("primary").getAsJsonArray());
			secondaryGroupList = readGroupFromJsonFile(json.get("secondary").getAsJsonArray());
		} catch (NBTException e) {
			throw new RuntimeException(e);
		}
		return new ItemGroupManager(primaryGroupList, secondaryGroupList);
	}

	private static List<List<ItemState>> readGroupFromJsonFile(JsonArray groupJsonArray) throws NBTException {
		List<List<ItemState>> groupList = new ArrayList<>();
		for(JsonElement groupJson : groupJsonArray) {
			List<ItemState> group = new ArrayList<>();
			for (JsonElement element : groupJson.getAsJsonArray()) {
				JsonObject dict = element.getAsJsonObject();
				Item item = Item.getByNameOrId(dict.get("id").getAsString());
				int meta = dict.has("meta") ? dict.get("meta").getAsInt() : 0;
				NBTTagCompound nbt = dict.has("nbt") ? JsonToNBT.getTagFromJson(dict.get("nbt").getAsString()) : null;
				group.add(new ItemState(new ItemStack(item, 1, meta, nbt)));
			}
			groupList.add(group);
		}
		return groupList;
	}

	private static boolean oldFileExists() {
		return new File(new File("config"), RotationHelper.ITEMGROUP_CONFFILE_PRIMARY).exists();
	}

	private static void convertOldFile() {
		RotationHelper.loadAllFromFile();
		new ItemGroupManager(RotationHelper.loadPrimaryGroup(), RotationHelper.loadSecondaryGroup()).save();
	}

	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////////////////////////////////////////////////

	private List<List<ItemState>> primaryGroupList;
	private List<List<ItemState>> secondaryGroupList;
	private final Map<ItemState, ItemState> primaryChain = new HashMap<ItemState, ItemState>();
	private final Map<ItemState, ItemState> secondaryChain = new HashMap<ItemState, ItemState>();

	private ItemGroupManager(List<List<ItemState>> primaryGroupList, List<List<ItemState>> secondaryGroupList) {
		this.primaryGroupList = primaryGroupList;
		this.secondaryGroupList = secondaryGroupList;
		createChain(primaryGroupList, Group.PRIMARY);
		createChain(secondaryGroupList, Group.SECONDARY);
	}

	private Map<ItemState, ItemState> getChain(Group groupType) {
		switch(groupType) {
		case PRIMARY:
			return this.primaryChain;
		case SECONDARY:
			return this.secondaryChain;
		default:
			throw new RuntimeException();
		}
	}

	private void createChain(List<List<ItemState>> groupList, Group groupType) {
		for (List<ItemState> group : groupList) {
			addGroup(group, groupType);
		}
	}

	public boolean addGroup(List<ItemState> group, Group groupType) {
		Map<ItemState, ItemState> chain = getChain(groupType);
		// check
		for (ItemState itemState : group) {
			if (chain.containsKey(itemState)) {
				return false;
			}
		}
		// add
		for (int i=0; i<group.size(); i++) {
			chain.put(group.get(i), group.get((i+1)%group.size()));
		}
		return true;
	}

	public boolean canRotate(ItemStack itemStack, Group group) {
		return getChain(group).containsKey(new ItemState(itemStack));
	}

	public List<ItemStack> getVariantsList(ItemStack itemStack, Group group) {
		final Map<ItemState, ItemState> chain = getChain(group);
		List<ItemStack> results = new ArrayList<>();
		results.add(itemStack);
		//
		int loopCount = 0;
		ItemState baseState = new ItemState(itemStack);
		ItemState nextState = chain.get(baseState);
		while(!baseState.equals(nextState)) {
			results.add(nextState.toItemStack());
			nextState = chain.get(nextState);
			loopCount++;
			if (loopCount >= 50000) throw new RuntimeException("Infinite loop!!");
		}
		return results;
	}

	public void save() {
		try {
			JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(CONFIG_FILE)));
			jsonWriter.setIndent(JSON_INDENT);
			jsonWriter.beginObject();
				jsonWriter.name("primary");
				writeGroup(jsonWriter, this.primaryGroupList);
				jsonWriter.name("secondary");
				writeGroup(jsonWriter, this.secondaryGroupList);
			jsonWriter.endObject();
			jsonWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeGroup(JsonWriter jsonWriter, List<List<ItemState>> groupList) throws IOException {
		jsonWriter.beginArray();
		for (List<ItemState> group : groupList) {
			jsonWriter.beginArray();
			for (ItemState itemState : group) {
				jsonWriter.beginObject();
				//
				Item item = itemState.cachedStack.getItem();
				int meta = itemState.cachedStack.getItemDamage();
				String nbt = itemState.cachedStack.hasTagCompound() ? itemState.cachedStack.getTagCompound().toString() : null;
				//
				jsonWriter.setIndent("");
				jsonWriter.name("id").value(Item.REGISTRY.getNameForObject(item).toString());
				if (meta > 0) {
					jsonWriter.name("meta").value(meta);
				}
				if (nbt != null) {
					jsonWriter.name("nbt").value(nbt);
				}
				jsonWriter.endObject();
				jsonWriter.setIndent(JSON_INDENT);
			}
			jsonWriter.endArray();
		}
		jsonWriter.endArray();
	}

}
