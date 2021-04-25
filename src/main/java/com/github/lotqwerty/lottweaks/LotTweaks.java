package com.github.lotqwerty.lottweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.fabricmc.api.ModInitializer;

//@Mod(LotTweaks.MODID)
public class LotTweaks implements ModInitializer {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "2.0.4";
	public static Logger LOGGER = LogManager.getLogger();

	public static class CONFIG {
		public static int MAX_RANGE = 128;
		public static int REPLACE_INTERVAL = 1;
		public static boolean REQUIRE_OP_TO_USE_REPLACE = false;
		public static boolean DISABLE_ANIMATIONS = false;
		public static boolean SNEAK_TO_SWITCH_GROUP = false;
	}
	
	@Override
	public void onInitialize() {
		LTPacketHandler.init();
//		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
	}

}
