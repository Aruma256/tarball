package com.github.aruma256.lottweaks.testhelper;

import org.junit.jupiter.api.BeforeAll;

import net.minecraft.init.Bootstrap;

public class MinecraftTestBase {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		if (!Bootstrap.isRegistered()) Bootstrap.register();
	}

}
