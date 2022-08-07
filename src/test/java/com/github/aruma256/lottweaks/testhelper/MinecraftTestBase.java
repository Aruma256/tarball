package com.github.aruma256.lottweaks.testhelper;

import org.junit.jupiter.api.BeforeAll;

import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.registry.Bootstrap;

public class MinecraftTestBase {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Bootstrap.bootStrap();
	}

}
