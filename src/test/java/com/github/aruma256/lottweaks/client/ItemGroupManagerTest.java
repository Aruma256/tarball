package com.github.aruma256.lottweaks.client;

import static com.github.aruma256.lottweaks.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

class ItemGroupManagerTest {

	private static ItemGroupManager getItemGroupManagerInstance(List<List<ItemState>> groupList) throws Exception {
		Constructor<ItemGroupManager> constructor = ItemGroupManager.class.getDeclaredConstructor(Iterable.class);
		constructor.setAccessible(true);
		return constructor.newInstance(groupList);
	}

	private static ItemGroupManager getSharedExample() throws Exception {
		return getItemGroupManagerInstance(
			Arrays.asList(
				Arrays.asList(
					new ItemState(new ItemStack(Blocks.STONE)),
					new ItemState(new ItemStack(Items.IRON_HELMET)),
					new ItemState(createNBTstack(new ItemStack(Items.GOLDEN_PICKAXE), "{ench:[{lvl:5s,id:32s}],display:{Name:\"Golden Pickaxe222\"}}"))
				),
				Arrays.asList(
					new ItemState(new ItemStack(Blocks.WOOL, 1, 2)),
					new ItemState(new ItemStack(Blocks.WOOL, 1, 3)),
					new ItemState(new ItemStack(Blocks.WOOL, 1, 5)),
					new ItemState(new ItemStack(Blocks.WOOL, 1, 7))
				)
			)
		);
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		if (!Bootstrap.isRegistered()) Bootstrap.register();
	}

	private JsonArray toJsonArray(String str) {
		return new JsonParser().parse(str).getAsJsonArray();
	}

	@Test
	void test_readGroupFromJsonFile() throws Exception {
		Method method = ItemGroupManager.class.getDeclaredMethod("readGroupFromJsonFile", JsonArray.class);
		method.setAccessible(true);
		// accepts an empty array
		assertEquals(Collections.EMPTY_LIST, method.invoke(null, toJsonArray("[]")));
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// ignores dictionaries that do not have an id key
		assertEquals(Arrays.asList(Collections.EMPTY_LIST), method.invoke(null, toJsonArray("[[{'meta':1}]]".replace('\'', '"'))));
		assertEquals("'id' is missing", ItemGroupManager.LOG_GROUP_CONFIG.poll());
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// ignores dictionaries with invalid item names
		assertEquals(Arrays.asList(Collections.EMPTY_LIST), method.invoke(null, toJsonArray("[[{'id':'minecraft:item_that_doesnt_exist'}]]".replace('\'', '"'))));
		assertEquals("'minecraft:item_that_doesnt_exist' not found", ItemGroupManager.LOG_GROUP_CONFIG.poll());
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// meta value can be specified
		assertEquals(Arrays.asList(Arrays.asList(new ItemState(new ItemStack(Blocks.WOOL, 1, 1)))), method.invoke(null, toJsonArray("[[{'id':'minecraft:wool','meta':1}]]".replace('\'', '"'))));
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// if meta is omitted, it is set to 0
		assertEquals(Arrays.asList(Arrays.asList(new ItemState(new ItemStack(Blocks.WOOL, 1, 0)))), method.invoke(null, toJsonArray("[[{'id':'minecraft:wool'}]]".replace('\'', '"'))));
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// nbt can be specified
		assertEquals(
			Arrays.asList(Arrays.asList(new ItemState(createNBTstack(new ItemStack(Items.GOLDEN_PICKAXE), "{ench:[{lvl:5s,id:32s}],display:{Name:\"Golden Pickaxe222\"}}")))),
			method.invoke(null, toJsonArray("[[{'id':'minecraft:golden_pickaxe','nbt':'{ench:[{lvl:5s,id:32s}],display:{Name:\\'Golden Pickaxe222\\'}}'}]]".replace('\'', '"')))
		);
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
		// accepts nested arrays
		assertEquals(
			Arrays.asList(
				Arrays.asList(
					new ItemState(new ItemStack(Blocks.STONE)),
					new ItemState(new ItemStack(Items.IRON_HELMET))
				),
				Arrays.asList(
					new ItemState(new ItemStack(Blocks.WOOL, 1, 2)),
					new ItemState(new ItemStack(Blocks.WOOL, 1, 3))
				)
			),
			method.invoke(null, toJsonArray("[[{'id':'minecraft:stone'},{'id':'minecraft:iron_helmet'}],[{'id':'minecraft:wool','meta':2},{'id':'minecraft:wool','meta':3}]]".replace('\'', '"')))
		);
		assertTrue(ItemGroupManager.LOG_GROUP_CONFIG.isEmpty());
	}

	@Test
	void test_addGroup() throws Exception {
		ItemGroupManager instance = getSharedExample();
		assertFalse(instance.addGroup(Arrays.asList(
			new ItemState(new ItemStack(Blocks.IRON_BLOCK))
		)));
		assertFalse(instance.addGroup(Arrays.asList(
			new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
			new ItemState(new ItemStack(Blocks.IRON_BLOCK))
		)));
		assertFalse(instance.addGroup(Arrays.asList(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 3)),
			new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
			new ItemState(new ItemStack(Blocks.GOLD_BLOCK))
		)));
		assertTrue(instance.addGroup(Arrays.asList(
			new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
			new ItemState(new ItemStack(Blocks.GOLD_BLOCK))
		)));
		assertFalse(instance.addGroup(Arrays.asList(
			new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
			new ItemState(new ItemStack(Blocks.GOLD_BLOCK))
		)));
	}

	@Test
	void test_isRegistered() throws Exception {
		ItemGroupManager instance = getSharedExample();
		assertTrue(instance.isRegistered(new ItemStack(Blocks.STONE)));
		assertFalse(instance.isRegistered(new ItemStack(Blocks.COBBLESTONE)));
		assertTrue(instance.isRegistered(new ItemStack(Items.IRON_HELMET)));
		assertFalse(instance.isRegistered(createNBTstack(new ItemStack(Items.IRON_HELMET), "{ench:[{lvl:4s,id:4s}]}")));
		assertTrue(instance.isRegistered(createNBTstack(new ItemStack(Items.GOLDEN_PICKAXE), "{ench:[{lvl:5s,id:32s}],display:{Name:\"Golden Pickaxe222\"}}")));
		assertFalse(instance.isRegistered(createNBTstack(new ItemStack(Items.GOLDEN_PICKAXE), "{ench:[{lvl:5s,id:32s}]}")));
		assertFalse(instance.isRegistered(new ItemStack(Blocks.WOOL)));
		assertTrue(instance.isRegistered(new ItemStack(Blocks.WOOL, 10, 5)));
	}

	@Test
	void test_getVariantsList() throws Exception {
		ItemGroupManager instance = getSharedExample();
		List<ItemStack> variants = instance.getVariantsList(new ItemStack(Items.IRON_HELMET));
		assertTrue(ItemStack.areItemStacksEqual(new ItemStack(Blocks.STONE), variants.get(0)));
		assertTrue(ItemStack.areItemStacksEqual(new ItemStack(Items.IRON_HELMET), variants.get(1)));
		assertTrue(ItemStack.areItemStacksEqual(createNBTstack(new ItemStack(Items.GOLDEN_PICKAXE), "{ench:[{lvl:5s,id:32s}],display:{Name:\"Golden Pickaxe222\"}}"), variants.get(2)));
	}

}
