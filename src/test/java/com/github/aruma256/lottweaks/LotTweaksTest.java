package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraftforge.fml.relauncher.Side;

class LotTweaksTest extends MinecraftTestBase {

	@Test
	final void test_networkCheckHandler() {
		LotTweaks instance = new LotTweaks();
		// client must accept servers without mod
		assertTrue(instance.networkCheckHandler(new HashMap<String, String>(), Side.SERVER));
		// server must accept clients without mod
		assertTrue(instance.networkCheckHandler(new HashMap<String, String>(), Side.CLIENT));
		// client must accept servers with the same version of the mod installed.
		assertTrue(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, LotTweaks.VERSION), Side.SERVER));
		// server must accept clients with the same version of the mod installed.
		assertTrue(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, LotTweaks.VERSION), Side.CLIENT));
		// client must NOT accept servers with newer versions of the mod installed.
		assertFalse(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, LotTweaks.VERSION + "1"), Side.SERVER));
		// server must accept clients with newer versions of the mod installed.
		assertTrue(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, LotTweaks.VERSION + "1"), Side.CLIENT));
		// client must accept servers with older versions of the mod installed.
		assertTrue(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, "0" + LotTweaks.VERSION), Side.SERVER));
		// server must NOT accept clients with older versions of the mod installed.
		assertFalse(instance.networkCheckHandler(Collections.singletonMap(LotTweaks.MODID, "0" + LotTweaks.VERSION), Side.CLIENT));
	}

}
