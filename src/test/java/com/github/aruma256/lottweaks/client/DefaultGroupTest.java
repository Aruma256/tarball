package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
		//TODO rewrite tests
		Method method = DefaultGroup.class.getDeclaredMethod("toItemState", Object.class);
		method.setAccessible(true);
		assertNotNull(method.invoke(null, Blocks.IRON_BLOCK));
		assertNotNull(method.invoke(null, Items.IRON_HELMET));
		assertNotNull(method.invoke(null, new ItemStack(Blocks.RED_WOOL)));
		assertNotNull(method.invoke(null, new ItemState(new ItemStack(Blocks.RED_WOOL, 4))));
		assertThrows(Exception.class, (() -> method.invoke(null, "dummy")));
	}

	@Test
	final void test_getEnchantedStack() throws Exception {
		Method method = DefaultGroup.class.getDeclaredMethod("getEnchantedStack", Item.class, String.class);
		method.setAccessible(true);
		assertEquals(1, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, (ItemStack)method.invoke(null, Items.BOW, "{Enchantments:[{lvl:1s,id:\"minecraft:infinity\"}]}")));
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
