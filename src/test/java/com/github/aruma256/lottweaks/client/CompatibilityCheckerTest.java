package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class CompatibilityCheckerTest {

	private static CompatibilityChecker getInstance() throws Exception {
		Constructor<CompatibilityChecker> constructor = CompatibilityChecker.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	@Test
	void test_constructor() throws Exception {
		// The server version is regarded as "0.0.0" by default
		assertEquals("0.0.0", getInstance().getServerModVersion());
	}

	@Test
	void test_setServerModVersion() throws Exception {
		CompatibilityChecker instance = getInstance();
		assertNotEquals("1.2.3", instance.getServerModVersion());
		instance.setServerModVersion("1.2.3");
		assertEquals("1.2.3", instance.getServerModVersion());
	}

	@Test
	void test_getServerModVersion() throws Exception {
		CompatibilityChecker instance = getInstance();
		assertNotEquals("1.2.3", instance.getServerModVersion());
		instance.setServerModVersion("1.2.3");
		assertEquals("1.2.3", instance.getServerModVersion());
	}

	@Test
	void test_clearServerLTVersion() throws Exception {
		CompatibilityChecker instance = getInstance();
		instance.setServerModVersion("1.2.3");
		instance.clearServerModVersion();
		assertEquals("0.0.0", instance.getServerModVersion());
	}

	@Test
	void test_isServerCompatibleWith() throws Exception {
		CompatibilityChecker instance = getInstance();
		instance.setServerModVersion("1.2.3");
		// Compatible if server version and requested version are the same
		assertTrue( instance.isServerCompatibleWith("1.2.2"));
		// Compatible if server version is newer than requested version
		assertTrue( instance.isServerCompatibleWith("1.2.3"));
		// NOT Compatible if server version is newer than requested version
		assertFalse(instance.isServerCompatibleWith("1.2.4"));
	}

}
