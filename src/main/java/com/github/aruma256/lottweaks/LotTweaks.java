package com.github.aruma256.lottweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.LotTweaksClient;
import com.github.aruma256.lottweaks.network.LTPacketHandler;
import com.github.aruma256.lottweaks.network.ServerConnectionListener;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LotTweaks.MODID)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "3.0.5";
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
		public static ForgeConfigSpec.BooleanValue SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT;// = true;
		public static ForgeConfigSpec.DoubleValue REACH_RANGE_AT_DEFAULT;// = 6
		public static ForgeConfigSpec.DoubleValue REACH_RANGE_AT_EXTENSION;// = 100

		static {
			MAX_RANGE = COMMON_BUILDER
					.defineInRange("common.MAX_RANGE", 128, 0, 256);
			REPLACE_INTERVAL = COMMON_BUILDER
					.defineInRange("client.REPLACE_INTERVAL", 1, 1, 256);
			REQUIRE_OP_TO_USE_REPLACE = COMMON_BUILDER
					.comment("Default: false")
					.define("server.REQUIRE_OP_TO_USE_REPLACE", false);
			DISABLE_ANIMATIONS = COMMON_BUILDER
					.comment("Default: false")
					.define("client.DISABLE_ANIMATIONS", false);
			SNEAK_TO_SWITCH_GROUP = COMMON_BUILDER
					.comment("Default: false -> Double-tap to switch to the secondary group")
					.define("client.SNEAK_TO_SWITCH_GROUP", false);
			INVERT_REPLACE_LOCK = COMMON_BUILDER
					.comment("Default: false")
					.define("client.INVERT_REPLACE_LOCK", false);
			SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT = COMMON_BUILDER
					.comment("Default: true")
					.comment("'true' is highly recommended")
					.define("client.SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT", true);
			REACH_RANGE_AT_DEFAULT = COMMON_BUILDER
					.defineInRange("client.REACH_RANGE_AT_DEFAULT", 6., 0., 256.);
			REACH_RANGE_AT_EXTENSION = COMMON_BUILDER
					.defineInRange("client.REACH_RANGE_AT_EXTENSION", 100., -256., 256.);
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
		ItemGroupManager.init();
		LotTweaksClient.init();
	}

	private void commonInit(FMLCommonSetupEvent event) {
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new ReachRangeManager());
		MinecraftForge.EVENT_BUS.register(new ServerConnectionListener());
	}

	/*
	@NetworkCheckHandler
	public boolean networkCheckHandler(Map<String, String> mods, Side remoteSide) {
		if (!mods.containsKey(MODID)) {
			return true;
		}
		//
		String clientVersion;
		String serverVersion;
		if (remoteSide == Side.SERVER) {
			// CLIENT
			clientVersion = VERSION;
			serverVersion = mods.get(MODID);
		} else {
			// SERVER
			clientVersion = mods.get(MODID);
			serverVersion = VERSION;
		}
		return VersionComparator.isVersionNewerOrTheSame(clientVersion, serverVersion);
	}
	*/

}
