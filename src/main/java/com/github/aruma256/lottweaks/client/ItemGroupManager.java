package com.github.aruma256.lottweaks.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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

public class ItemGroupManager {

	private static final String JSON_INDENT = "  ";
	public static final File CONFIG_FILE = new File(new File("config"), "LotTweaks-ItemGroup.json");
	public static final Queue<String> LOG_GROUP_CONFIG = new ArrayDeque<>();

	private static List<ItemGroupManager> managerList = new ArrayList<>();

	public static ItemGroupManager getInstance(int groupListId) {
		return managerList.get(groupListId);
	}

	public static boolean init() {
		if (!CONFIG_FILE.exists()) {
			if (oldFileExists()) {
				convertOldFile();
			} else {
				generateDefaultConfig();
			}
		}
		return loadFromFile();
	}

	private static void generateDefaultConfig() {
		save(
			new ItemGroupManager(DefaultGroup.getDefaultGroupList0()),
			new ItemGroupManager(DefaultGroup.getDefaultGroupList1())
		);
	}

	private static boolean loadFromFile() {
		JsonObject json;
		List<List<ItemState>> groupList0;
		List<List<ItemState>> groupList1;
		try {
			json = new JsonParser().parse(new JsonReader(new BufferedReader(new InputStreamReader(new FileInputStream(CONFIG_FILE), StandardCharsets.UTF_8)))).getAsJsonObject();
			groupList0 = readGroupFromJsonFile(json.get("grouplist-0").getAsJsonArray());
			groupList1 = readGroupFromJsonFile(json.get("grouplist-1").getAsJsonArray());
		} catch (JsonIOException e1) {
			LOG_GROUP_CONFIG.add("JsonIOError");
			return false;
		} catch (JsonSyntaxException | IllegalStateException e1) {
			LOG_GROUP_CONFIG.add("JsonSyntaxError");
			return false;
		} catch (FileNotFoundException e1) {
			LOG_GROUP_CONFIG.add("ERROR FileNotFound");
			return false;
		}
		managerList = Arrays.asList(
			new ItemGroupManager(groupList0),
			new ItemGroupManager(groupList1)
		);
		return true;
	}

	private static List<List<ItemState>> readGroupFromJsonFile(JsonArray groupJsonArray) {
		List<List<ItemState>> groupList = new ArrayList<>();
		for(JsonElement groupJson : groupJsonArray) {
			List<ItemState> group = new ArrayList<>();
			for (JsonElement element : groupJson.getAsJsonArray()) {
				JsonObject dict = element.getAsJsonObject();
				Item item = Item.getByNameOrId(dict.get("id").getAsString());
				int meta = dict.has("meta") ? dict.get("meta").getAsInt() : 0;
				ItemStack itemStack = new ItemStack(item, 1, meta);
				if (dict.has("nbt")) {
					String nbtString = dict.get("nbt").getAsString();
					try {
						itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
					} catch (NBTException e) {
						LOG_GROUP_CONFIG.add("NBTException -> " + nbtString);
						continue;
					}
				}
				group.add(new ItemState(itemStack));
			}
			groupList.add(group);
		}
		return groupList;
	}

	public static void save() {
		save(managerList);
	}

	public static void save(ItemGroupManager ... groupManagers) {
		save(Arrays.asList(groupManagers));
	}

	private static void save(List<ItemGroupManager> managers) {
		try {
			JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8)));
			jsonWriter.setIndent(JSON_INDENT);
			jsonWriter.beginObject();
			for(int i=0; i<managers.size(); i++) {
				jsonWriter.name(String.format("grouplist-%d", i));
				managers.get(i).writeToJson(jsonWriter);
			}
			jsonWriter.endObject();
			jsonWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean oldFileExists() {
		return new File(new File("config"), RotationHelper.ITEMGROUP_CONFFILE_PRIMARY).exists();
	}

	private static void convertOldFile() {
		RotationHelper.loadAllFromFile();
		save(
			new ItemGroupManager(RotationHelper.loadPrimaryGroup()),
			new ItemGroupManager(RotationHelper.loadSecondaryGroup())
		);
	}

	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////////////////////////////////////////////////

	private List<List<ItemState>> groupList;
	private final Map<ItemState, ItemState> chain = new HashMap<ItemState, ItemState>();

	private ItemGroupManager(List<List<ItemState>> groupList) {
		this.groupList = groupList;
		initializeChain(groupList);
	}

	private void initializeChain(List<List<ItemState>> groupList) {
		for (List<ItemState> rawGroup : groupList) {
			addGroupToChain(rawGroup, false);
		}
	}

	public boolean addGroupFromCommand(List<ItemState> rawGroup) {
		if (addGroupToChain(rawGroup, true)) {
			this.groupList.add(rawGroup);
			return true;
		}
		return false;
	}

	private boolean addGroupToChain(List<ItemState> rawGroup, boolean strict) {
		List<ItemState> validGroup = new ArrayList<>();
		Set<ItemState> _set = new HashSet<>();
		// check
		for (ItemState itemState : rawGroup) {
			if (chain.containsKey(itemState) || _set.contains(itemState)) {
				String itemName = Item.REGISTRY.getNameForObject(itemState.cachedStack.getItem()).toString();
				int meta = itemState.cachedStack.getItemDamage();
				if (itemState.cachedStack.hasTagCompound()) {
					LOG_GROUP_CONFIG.add(String.format("Item config '%s/%d/%s' is duplicated.", itemName, meta, itemState.cachedStack.getTagCompound().toString()));
				} else {
					LOG_GROUP_CONFIG.add(String.format("Item config '%s/%d' is duplicated.", itemName, meta));
				}
				if (strict) return false;
				continue;
			}
			_set.add(itemState);
			validGroup.add(itemState);
		}
		if (validGroup.size() <= 1) {
			LOG_GROUP_CONFIG.add("A group must have 2 or more elements");
			return false;
		}
		// add
		for (int i=0; i<validGroup.size(); i++) {
			chain.put(validGroup.get(i), validGroup.get((i+1)%validGroup.size()));
		}
		return true;
	}

	public boolean isRegistered(ItemStack itemStack) {
		return chain.containsKey(new ItemState(itemStack));
	}

	public boolean canRotate(ItemStack itemStack) {
		return chain.containsKey(new ItemState(itemStack));
	}

	public List<ItemStack> getVariantsList(ItemStack itemStack) {
		List<ItemStack> results = new ArrayList<>();
		results.add(itemStack);
		//
		int loopCount = 0;
		ItemState baseState = new ItemState(itemStack);
		if (!chain.containsKey(baseState)) {
			itemStack = itemStack.copy();
			itemStack.setTagCompound(null);
			baseState = new ItemState(itemStack);
			if (!chain.containsKey(baseState)) {
				return null;
			}
		}
		ItemState nextState = chain.get(baseState);
		//
		while(!baseState.equals(nextState)) {
			results.add(nextState.toItemStack());
			nextState = chain.get(nextState);
			loopCount++;
			if (loopCount >= 50000) throw new RuntimeException("Infinite loop!!");
		}
		return results;
	}

	private void writeToJson(JsonWriter jsonWriter) throws IOException {
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
