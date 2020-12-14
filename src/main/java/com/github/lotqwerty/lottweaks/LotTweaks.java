package com.github.lotqwerty.lottweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.fabricmc.api.ModInitializer;

//@Mod(LotTweaks.MODID)
public class LotTweaks implements ModInitializer {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "1.2.9";
	public static Logger LOGGER = LogManager.getLogger();

	public static class CONFIG {
		public static int MAX_RANGE = 128;
		public static int REPLACE_INTERVAL = 1;
		public static boolean REQUIRE_OP_TO_USE_REPLACE = false;
		public static boolean DISABLE_ANIMATIONS = false;

//		static {
//			MAX_RANGE = COMMON_BUILDER
//					.defineInRange("common.MAX_RANGE", 128, 0, 256);
//			REPLACE_INTERVAL = COMMON_BUILDER
//					.defineInRange("client.REPLACE_INTERVAL", 1, 1, 256);
//			REQUIRE_OP_TO_USE_REPLACE = COMMON_BUILDER
//					.define("server.REQUIRE_OP_TO_USE_REPLACE", false);
//			DISABLE_ANIMATIONS = COMMON_BUILDER
//					.define("client.DISABLE_ANIMATIONS", false);
//			//
//			COMMON_SPEC = COMMON_BUILDER.build();
//		}
	}
	
	@Override
	public void onInitialize() {
		LTPacketHandler.init();
//		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
	}

}
