package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class CompatibilityCheckerTest {

	private static CompatibilityChecker getInstance() {
		try {
			Constructor<CompatibilityChecker> constructor = CompatibilityChecker.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testInitialState() {
		assertEquals("0", getInstance().getServerLTVersion());
	}

	@Test
	void testSetGetServerLTVersion() {
		CompatibilityChecker instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		assertEquals("1.2.3", instance.getServerLTVersion());
	}

	@Test
	void testClearServerLTVersion() {
		CompatibilityChecker instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		instance.clearServerLTVersion();
		assertEquals("0", instance.getServerLTVersion());
	}

	@Test
	void testRequireServerLTVersion() {
		CompatibilityChecker instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		assertTrue(instance.requireServerLTVersion("1.2.2"));
		assertTrue(instance.requireServerLTVersion("1.2.3"));
		assertFalse(instance.requireServerLTVersion("1.2.4"));
	}

}
