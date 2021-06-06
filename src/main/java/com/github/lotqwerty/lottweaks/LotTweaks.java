package com.github.lotqwerty.lottweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

@Mod(LotTweaks.MODID)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "2.0.4";
	public static Logger LOGGER = LogManager.getLogger();

	public static class CONFIG {
		private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec COMMON_SPEC;

		public static ForgeConfigSpec.IntValue MAX_RANGE;// = 128;
		public static ForgeConfigSpec.IntValue REPLACE_INTERVAL;// = 1;
		public static ForgeConfigSpec.BooleanValue REQUIRE_OP_TO_USE_REPLACE;// = false;
		public static ForgeConfigSpec.BooleanValue DISABLE_ANIMATIONS;// = false;
		public static ForgeConfigSpec.BooleanValue SNEAK_TO_SWITCH_GROUP;// = false;
		public static ForgeConfigSpec.BooleanValue INVERT_REPLACE_LOCK;// = false;

		static {
			MAX_RANGE = COMMON_BUILDER
					.defineInRange("common.MAX_RANGE", 128, 0, 256);
			REPLACE_INTERVAL = COMMON_BUILDER
					.defineInRange("client.REPLACE_INTERVAL", 1, 1, 256);
			REQUIRE_OP_TO_USE_REPLACE = COMMON_BUILDER
					.define("server.REQUIRE_OP_TO_USE_REPLACE", false);
			DISABLE_ANIMATIONS = COMMON_BUILDER
					.define("client.DISABLE_ANIMATIONS", false);
			SNEAK_TO_SWITCH_GROUP = COMMON_BUILDER
					.define("client.SNEAK_TO_SWITCH_GROUP", false);
			INVERT_REPLACE_LOCK = COMMON_BUILDER
					.define("client.INVERT_REPLACE_LOCK", false);
			//
			COMMON_SPEC = COMMON_BUILDER.build();
		}
	}

	public LotTweaks() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG.COMMON_SPEC, NAME + ".toml");
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::clientInit);
		modEventBus.addListener(this::commonInit);
		
	}

	private void clientInit(FMLClientSetupEvent event) {
		LotTweaksClient.init();
		RotationHelper.loadAllFromFile();
		RotationHelper.loadAllItemGroupFromStrArray();
	}

	private void commonInit(FMLCommonSetupEvent event) {
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
	}

}
