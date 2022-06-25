package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;

class ItemStateTest {

	private static ItemStack createNBTstack(ItemStack itemStack, String nbtString) throws Exception {
		itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
		return itemStack;
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		if (!Bootstrap.isRegistered()) Bootstrap.register();
	}

	@Test
	void test_toItemStack() throws Exception {
		ItemStack sampleStack = createNBTstack(new ItemStack(Items.IRON_HELMET), "{ench:[{lvl:4s,id:4s}],RepairCost:1}");
		assertTrue(ItemStack.areItemStacksEqual(sampleStack, new ItemState(sampleStack).toItemStack()));
		assertFalse(ItemStack.areItemStacksEqual(new ItemStack(Items.IRON_HELMET), new ItemState(sampleStack).toItemStack()));
	}

	@Test
	void test_equals() throws Exception {
		assertEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)),
			new ItemState(new ItemStack(Blocks.WOOL, 44, 5))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)),
			new ItemState(new ItemStack(Blocks.WOOL, 44, 4))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)),
			new ItemState(new ItemStack(Blocks.CARPET, 44, 5))
		);
		assertEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}"))
		);
		assertNotEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:3s}],RepairCost:1}"))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Items.IRON_HELMET, 1, 5)),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}"))
		);
	}

	@Test
	void test_hashCode() throws Exception {
		assertEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)).hashCode(),
			new ItemState(new ItemStack(Blocks.WOOL, 44, 5)).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)).hashCode(),
			new ItemState(new ItemStack(Blocks.WOOL, 44, 4)).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.WOOL, 1, 5)).hashCode(),
			new ItemState(new ItemStack(Blocks.CARPET, 44, 5)).hashCode()
		);
		assertEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode()
		);
		assertNotEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:3s}],RepairCost:1}")).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Items.IRON_HELMET, 1, 5)).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1, 5), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode()
		);
	}

}
