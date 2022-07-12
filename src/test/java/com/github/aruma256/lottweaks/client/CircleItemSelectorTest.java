package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;
import com.github.aruma256.lottweaks.client.selector.CircleItemSelector.Angle;
import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

class CircleItemSelectorTest extends MinecraftTestBase {

	private static final double DELTA = 1e-8;

	private static CircleItemSelector getSharedExample() {
		return new CircleItemSelector(
			Arrays.asList(
				new ItemStack(Blocks.IRON_BLOCK),
				new ItemStack(Blocks.GOLD_BLOCK),
				new ItemStack(Blocks.DIAMOND_BLOCK)
			),
			0
		) {
			@Override
			protected void replaceInventory() {
			}
		};
	}
	
	@Test
	final void test_getSelectedId() throws Exception {
		CircleItemSelector instance = getSharedExample();
		Method method = CircleItemSelector.class.getDeclaredMethod("getSelectedId");
		method.setAccessible(true);
		Field angleField = CircleItemSelector.class.getDeclaredField("angle");
		angleField.setAccessible(true);
		// default is 0
		assertEquals(0, method.invoke(instance));
		// bottom left
		double x, y;
		x = -1.73;
		y = -1.00;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(0, method.invoke(instance)); // (-1.73, -1.00) -> 0
		x = -1.74;
		y = -1.00;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(1, method.invoke(instance)); // (-1.74, -1.00) -> 1
		// top
		x = -0.01;
		y = 1;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(1, method.invoke(instance)); // (-0.01, 1) -> 1
		x = 0.01;
		y = 1;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(2, method.invoke(instance)); // (0.01, 1) -> 2
		// bottom right
		x = 1.74;
		y = -1.00;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(2, method.invoke(instance)); // (1.74, -1.00) -> 2
		x = 1.73;
		y = -1.00;
		angleField.set(instance, new Angle(Math.atan2(y, x)));
		assertEquals(0, method.invoke(instance)); // (1.73, -1.00) -> 0
	}

	@Test
	final void test_convertIndexToAngle() throws Exception {
		CircleItemSelector instance = getSharedExample();
		Method method = CircleItemSelector.class.getDeclaredMethod("convertIndexToAngle", int.class);
		method.setAccessible(true);
		double theta;
		theta = (double) method.invoke(instance, 0);
		assertEquals(0, Math.cos(theta), DELTA);
		assertEquals(-1, Math.sin(theta), DELTA);
		theta = (double) method.invoke(instance, 1);
		assertEquals(Math.cos(Math.PI/6*5), Math.cos(theta), DELTA);
		assertEquals(Math.sin(Math.PI/6*5), Math.sin(theta), DELTA);
	}

	@Test
	void test_Angle() throws Exception {
		Angle instance = new Angle(0d);
		// 0 -> 0
		assertEquals(0d, instance.value(), DELTA);
		// 1.999PI -> 1.999PI
		instance.add(1.999*Math.PI);
		assertEquals(1.999*Math.PI, instance.value(), DELTA);
		// 2.001PI -> 0.001PI
		instance.add(0.002*Math.PI);
		assertEquals(0.001*Math.PI, instance.value(), DELTA);
		// 6.001PI -> 0.001PI
		instance.add(6*Math.PI);
		assertEquals(0.001*Math.PI, instance.value(), DELTA);
		// -0.001PI -> 1.999PI
		instance.add(-0.002*Math.PI);
		assertEquals(1.999*Math.PI, instance.value(), DELTA);
	}

}
