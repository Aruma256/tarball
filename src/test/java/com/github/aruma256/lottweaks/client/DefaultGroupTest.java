package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class DefaultGroupTest extends MinecraftTestBase {

	@Test
	final void test_getDefaultGroupList0() {
		assertNotNull(DefaultGroup.getDefaultGroupList0());
	}

	@Test
	final void test_getDefaultGroupList1() {
		assertNotNull(DefaultGroup.getDefaultGroupList1());
	}

	@Test
	final void test_toItemState() throws Exception {
		Method method = DefaultGroup.class.getDeclaredMethod("toItemState", Object.class);
		method.setAccessible(true);
		assertNotNull(method.invoke(null, Blocks.IRON_BLOCK));
		assertNotNull(method.invoke(null, Items.IRON_HELMET));
		assertNotNull(method.invoke(null, new ItemStack(Blocks.WOOL, 4, 5)));
		assertNotNull(method.invoke(null, new ItemState(new ItemStack(Blocks.WOOL, 4, 5))));
		assertThrows(Exception.class, (() -> method.invoke(null, "dummy")));
	}

	@Test
	final void test_toMetaVariants() throws Exception {
		Method method = DefaultGroup.class.getDeclaredMethod("toMetaVariants", Block.class, int.class);
		method.setAccessible(true);
		assertEquals(
			Arrays.asList(
				new ItemState(new ItemStack(Blocks.WOOL, 1, 0)),
				new ItemState(new ItemStack(Blocks.WOOL, 1, 1)),
				new ItemState(new ItemStack(Blocks.WOOL, 1, 2))
			),
			method.invoke(null, Blocks.WOOL, 3)
		);
	}

	@Test
	final void test_getEnchantedStack() throws Exception {
		Method method = DefaultGroup.class.getDeclaredMethod("getEnchantedStack", Item.class, String.class);
		method.setAccessible(true);
		assertEquals(1, EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, (ItemStack)method.invoke(null, Items.BOW, "{ench:[{lvl:1s,id:51s}]}")));
	}

	@Test
	final void test_toList() throws Exception {
		Method method = DefaultGroup.class.getDeclaredMethod("toList", Object[].class);
		method.setAccessible(true);
		assertEquals(
			Arrays.asList(
				new ItemState(new ItemStack(Blocks.COAL_BLOCK)),
				new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
				new ItemState(new ItemStack(Blocks.GOLD_BLOCK)),
				new ItemState(new ItemStack(Blocks.DIAMOND_BLOCK))
			),
			method.invoke(null, (Object)new Object[] {
				new ItemState(new ItemStack(Blocks.COAL_BLOCK)),
				Arrays.asList(
					new ItemState(new ItemStack(Blocks.IRON_BLOCK)),
					Arrays.asList(
						new ItemState(new ItemStack(Blocks.GOLD_BLOCK)),
						new ItemState(new ItemStack(Blocks.DIAMOND_BLOCK))
					)
				)
			})
		);
	}

}
