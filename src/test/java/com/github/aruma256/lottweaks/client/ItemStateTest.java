package com.github.aruma256.lottweaks.client;

import static com.github.aruma256.lottweaks.testhelper.TestHelper.createNBTstack;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

class ItemStateTest extends MinecraftTestBase {

	@Test
	void test_toItemStack() {
		ItemStack sampleStack = createNBTstack(new ItemStack(Items.IRON_HELMET), "{ench:[{lvl:4s,id:4s}],RepairCost:1}");
		assertTrue(ItemStack.matches(sampleStack, new ItemState(sampleStack).toItemStack()));
		assertFalse(ItemStack.matches(new ItemStack(Items.IRON_HELMET), new ItemState(sampleStack).toItemStack()));
	}

	@Test
	void test_equals() {
		assertEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL, 1)),
			new ItemState(new ItemStack(Blocks.RED_WOOL, 44))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL, 1)),
			new ItemState(new ItemStack(Blocks.BLUE_WOOL, 44))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL)),
			new ItemState(new ItemStack(Blocks.BLUE_WOOL))
		);
		assertEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}"))
		);
		assertNotEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:3s}],RepairCost:1}"))
		);
		assertNotEquals(
			new ItemState(new ItemStack(Items.IRON_HELMET, 1)),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}"))
		);
	}

	@Test
	void test_hashCode() {
		assertEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL, 1)).hashCode(),
			new ItemState(new ItemStack(Blocks.RED_WOOL, 44)).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL, 1)).hashCode(),
			new ItemState(new ItemStack(Blocks.BLUE_WOOL, 44)).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Blocks.RED_WOOL)).hashCode(),
			new ItemState(new ItemStack(Blocks.BLUE_WOOL)).hashCode()
		);
		assertEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode()
		);
		assertNotEquals(
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:3s}],RepairCost:1}")).hashCode()
		);
		assertNotEquals(
			new ItemState(new ItemStack(Items.IRON_HELMET, 1)).hashCode(),
			new ItemState(createNBTstack(new ItemStack(Items.IRON_HELMET, 1), "{ench:[{lvl:4s,id:4s}],RepairCost:1}")).hashCode()
		);
	}

}
