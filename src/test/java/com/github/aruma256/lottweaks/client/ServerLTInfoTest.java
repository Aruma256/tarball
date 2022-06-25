package com.github.aruma256.lottweaks.client;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

class ServerLTInfoTest {

	private static ServerLTInfo getInstance() {
		try {
			Constructor<ServerLTInfo> constructor = ServerLTInfo.class.getDeclaredConstructor();
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
		ServerLTInfo instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		assertEquals("1.2.3", instance.getServerLTVersion());
	}

	@Test
	void testClearServerLTVersion() {
		ServerLTInfo instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		instance.clearServerLTVersion();
		assertEquals("0", instance.getServerLTVersion());
	}

	@Test
	void testRequireServerLTVersion() {
		ServerLTInfo instance = getInstance();
		instance.setServerLTVersion("1.2.3");
		assertTrue(instance.requireServerLTVersion("1.2.2"));
		assertTrue(instance.requireServerLTVersion("1.2.3"));
		assertFalse(instance.requireServerLTVersion("1.2.4"));
	}

}
