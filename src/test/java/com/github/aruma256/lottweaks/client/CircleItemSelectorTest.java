package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;

import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;

class CircleItemSelectorTest {

	private static CircleItemSelector getSharedExample() {
		return new CircleItemSelector(
			Arrays.asList(
				new ItemStack(Blocks.IRON_BLOCK),
				new ItemStack(Blocks.GOLD_BLOCK),
				new ItemStack(Blocks.DIAMOND_BLOCK)
			),
			0
		);
	}
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		if (!Bootstrap.isRegistered()) Bootstrap.register();
	}

	@Test
	final void test_getSelectedId() throws Exception {
		CircleItemSelector instance = getSharedExample();
		Method method = CircleItemSelector.class.getDeclaredMethod("getSelectedId");
		method.setAccessible(true);
		Field mouseDx = CircleItemSelector.class.getDeclaredField("mouseDx");
		mouseDx.setAccessible(true);
		Field mouseDy = CircleItemSelector.class.getDeclaredField("mouseDy");
		mouseDy.setAccessible(true);
		assertEquals(0, method.invoke(instance)); // defualt (0, 0) -> 0
		// bottom left
		mouseDx.setDouble(instance, -1.73);
		mouseDy.setDouble(instance, -1.00);
		assertEquals(0, method.invoke(instance)); // (-1.73, -1.00) -> 0
		mouseDx.setDouble(instance, -1.74);
		mouseDy.setDouble(instance, -1.00);
		assertEquals(1, method.invoke(instance)); // (-1.74, -1.00) -> 1
		// top
		mouseDx.setDouble(instance, -0.01);
		mouseDy.setDouble(instance, 1);
		assertEquals(1, method.invoke(instance)); // (-0.01, 1) -> 1
		mouseDx.setDouble(instance, 0.01);
		mouseDy.setDouble(instance, 1);
		assertEquals(2, method.invoke(instance)); // (0.01, 1) -> 2
		// bottom right
		mouseDx.setDouble(instance, 1.74);
		mouseDy.setDouble(instance, -1.00);
		assertEquals(2, method.invoke(instance)); // (1.74, -1.00) -> 2
		mouseDx.setDouble(instance, 1.73);
		mouseDy.setDouble(instance, -1.00);
		assertEquals(0, method.invoke(instance)); // (1.73, -1.00) -> 0
	}

}
