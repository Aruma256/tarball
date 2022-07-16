package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import net.minecraft.util.text.TextFormatting;

class IngameLogTest {

	@Test
	final void test_addInfoLog() throws Exception {
		IngameLog instance = getInstance();
		instance.addInfoLog("abc");
		assertEquals("abc", instance.debug_pollLog());
		assertNull(instance.debug_pollLog());
	}

	@Test
	final void testAddErrorLog() throws Exception {
		IngameLog instance = getInstance();
		instance.addErrorLog("abc");
		assertEquals(TextFormatting.RED + "abc", instance.debug_pollLog());
		assertNull(instance.debug_pollLog());
	}

	@Test @Disabled
	final void test_onPlayerLoggedIn() {
		fail("Not yet implemented"); // TODO
	}

	// ######################
	//   SETUP METHODS
	// ######################

	private static IngameLog getInstance() throws Exception {
		Constructor<IngameLog> constructor = IngameLog.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

}
