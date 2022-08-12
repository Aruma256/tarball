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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.aruma256.lottweaks.LotTweaks;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class ItemGroupManager {

	private static final String JSON_INDENT = "  ";
	public static final File CONFIG_FILE = new File(new File("config"), String.format("LotTweaks-ItemGroup-%s.json", LotTweaks.MC_VERSION));

	private static List<ItemGroupManager> managerList = new ArrayList<>();

	public static int getSize() {
		return managerList.size();
	}

	public static ItemGroupManager getInstance(int groupListId) {
		return managerList.get(groupListId);
	}

	public static boolean init() {
		if (!CONFIG_FILE.exists()) {
			IngameLog.instance.addInfoLog(CONFIG_FILE.getName() + " is not found");
			if (V2ConfigLoader.V2configFileExists()) {
				IngameLog.instance.addInfoLog("Converting old config files...");
				convertOldFile();
				IngameLog.instance.addInfoLog("Conversion complete!");
			} else {
				generateDefaultConfig();
				IngameLog.instance.addInfoLog("Default config file has been created.");
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
			IngameLog.instance.addErrorLog("JsonIOError");
			return false;
		} catch (JsonSyntaxException | IllegalStateException e1) {
			IngameLog.instance.addErrorLog("JsonSyntaxError");
			return false;
		} catch (FileNotFoundException e1) {
			IngameLog.instance.addErrorLog("ERROR FileNotFound");
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
				if (!dict.has("id")) {
					IngameLog.instance.addErrorLog("'id' is missing");
					continue;
				}
				String itemStr = dict.get("id").getAsString();
				Item item = Item.getByNameOrId(itemStr);
				if (item == null) {
					IngameLog.instance.addErrorLog(String.format("'%s' was not found", itemStr));
					continue;
				}
				if (item == Items.AIR) {
					IngameLog.instance.addErrorLog(String.format("'%s' is not supported", itemStr));
					continue;
				}
				int meta = dict.has("meta") ? dict.get("meta").getAsInt() : 0;
				ItemStack itemStack = new ItemStack(item, 1, meta);
				if (dict.has("nbt")) {
					String nbtString = dict.get("nbt").getAsString();
					try {
						itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
					} catch (NBTException e) {
						IngameLog.instance.addErrorLog("NBTException -> " + nbtString);
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
			jsonWriter.name("mc_version").value("1.12.x");
			jsonWriter.name("config_version").value(3);
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

	private static void convertOldFile() {
		save(
			new ItemGroupManager(V2ConfigLoader.loadPrimaryGroup()),
			new ItemGroupManager(V2ConfigLoader.loadSecondaryGroup())
		);
	}

	////////////////////////////////////////////////
	////////////////////////////////////////////////
	////////////////////////////////////////////////

	private List<List<ItemState>> groupList = new ArrayList<>();
	private final Map<ItemState, List<ItemState>> cache = new HashMap<>();

	private ItemGroupManager(Iterable<List<ItemState>> groupList) {
		for (List<ItemState> group : groupList) {
			addGroup(group);
		}
	}

	public boolean addGroup(List<ItemState> group) {
		if (canBeAdded(group)) {
			groupList.add(group);
			for (ItemState state : group) {
				cache.put(state, group);
			}
			return true;
		}
		return false;
	}

	private boolean canBeAdded(List<ItemState> group) {
		if (group.size() <= 1) {
			IngameLog.instance.addErrorLog("A group must have 2 or more elements");
			return false;
		}
		Set<ItemState> dupCheck = new HashSet<>();
		for (ItemState itemState : group) {
			if (isRegistered(itemState) || dupCheck.contains(itemState)) {
				String itemName = Item.REGISTRY.getNameForObject(itemState.cachedStack.getItem()).toString();
				int meta = itemState.cachedStack.getItemDamage();
				if (itemState.cachedStack.hasTagCompound()) {
					IngameLog.instance.addErrorLog(String.format("Item config '%s/%d/%s' is duplicated.", itemName, meta, itemState.cachedStack.getTagCompound().toString()));
				} else {
					IngameLog.instance.addErrorLog(String.format("Item config '%s/%d' is duplicated.", itemName, meta));
				}
				return false;
			}
			dupCheck.add(itemState);
		}
		return true;
	}

	public boolean isRegistered(ItemStack itemStack) {
		return isRegistered(new ItemState(itemStack));
	}

	private boolean isRegistered(ItemState itemState) {
		return cache.containsKey(itemState);
	}

	public List<ItemStack> getVariantsList(ItemStack itemStack) {
		List<ItemState> resultsState = null;
		ItemState itemState = new ItemState(itemStack);
		resultsState = cache.get(itemState);
		if (resultsState == null) {
			itemStack = itemStack.copy();
			itemStack.setTagCompound(null);
			itemState = new ItemState(itemStack);
			resultsState = cache.get(itemState);
		}
		//
		if (resultsState == null) return null;
		return resultsState.stream().map(e -> e.toItemStack()).collect(Collectors.toList());
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
