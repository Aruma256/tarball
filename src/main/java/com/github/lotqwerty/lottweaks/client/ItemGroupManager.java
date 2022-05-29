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

	public static ItemGroupManager instance = new ItemGroupManager();

	public static enum Group {
		PRIMARY,
		SECONDARY
	}

	private List<List<ItemState>> primaryGroupList;
	private List<List<ItemState>> secondaryGroupList;
	private Map<ItemState, ItemState> primaryChain;
	private Map<ItemState, ItemState> secondaryChain;

	private ItemGroupManager() {
	}

	public boolean canRotate(ItemStack itemStack, Group group) {
		return getChain(group).containsKey(new ItemState(itemStack));
	}

	private Map<ItemState, ItemState> getChain(Group group) {
		switch(group) {
		case PRIMARY:
			return primaryChain;
		case SECONDARY:
			return secondaryChain;
		default:
			throw new RuntimeException();
		}
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

	private List<List<ItemState>> readGroup(JsonArray groupJsonArray) throws NBTException {
		List<List<ItemState>> tmpGroupList = new ArrayList<>();
		for(JsonElement groupJson : groupJsonArray) {
			List<ItemState> group = new ArrayList<>();
			for (JsonElement element : groupJson.getAsJsonArray()) {
				JsonObject dict = element.getAsJsonObject();
				Item item = Item.getByNameOrId(dict.get("id").getAsString());
				int meta = dict.has("meta") ? dict.get("meta").getAsInt() : 0;
				NBTTagCompound nbt = dict.has("nbt") ? JsonToNBT.getTagFromJson(dict.get("nbt").getAsString()) : null;
				group.add(new ItemState(new ItemStack(item, 1, meta, nbt)));
			}
			tmpGroupList.add(group);
		}
		return tmpGroupList;
	}

	private Map<ItemState, ItemState> createChain(List<List<ItemState>> groupList) {
		Map<ItemState, ItemState> tmpChain = new HashMap<>();
		for (List<ItemState> group : groupList) {
			for (int i=0; i<group.size(); i++) {
				tmpChain.put(group.get(i), group.get((i+1)%group.size()));
			}
		}
		return tmpChain;
	}

	public void loadFromFile() {
		JsonObject json;
		try {
			json = new JsonParser().parse(new JsonReader(new BufferedReader(new FileReader(CONFIG_FILE)))).getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			this.primaryGroupList = readGroup(json.get("primary").getAsJsonArray());
			this.secondaryGroupList = readGroup(json.get("secondary").getAsJsonArray());
			this.primaryChain = createChain(primaryGroupList);
			this.secondaryChain = createChain(secondaryGroupList);
		} catch (NBTException e) {
			throw new RuntimeException(e);
		}
	}

}
