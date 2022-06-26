package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;

class ItemGroupManagerTest {

	private static ItemGroupManager getItemGroupManagerInstance(List<List<ItemState>> groupList) throws Exception {
		Constructor<ItemGroupManager> constructor = ItemGroupManager.class.getDeclaredConstructor(Iterable.class);
		constructor.setAccessible(true);
		return constructor.newInstance(groupList);
	}

	private static ItemStack createNBTstack(ItemStack itemStack, String nbtString) throws Exception {
		itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
		return itemStack;
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
