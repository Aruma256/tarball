package com.github.aruma256.lottweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.github.aruma256.lottweaks.client.ItemGroupManager;
import com.github.aruma256.lottweaks.client.LotTweaksClient;
import com.github.aruma256.lottweaks.network.LTPacketHandler;
import com.github.aruma256.lottweaks.network.ServerConnectionListener;

@Mod(modid = LotTweaks.MODID, name = LotTweaks.NAME, version = LotTweaks.VERSION)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "3.0.2";
	public static Logger LOGGER;

	@Config(modid = MODID, type = Type.INSTANCE, name = NAME)
	public static class CONFIG {
		@RangeInt(min = 0, max = 256)
		public static int MAX_RANGE = 128;
		@RangeInt(min = 1, max = 120)
		public static int REPLACE_INTERVAL = 1;
		@Comment(value = { "Default: false" })
		public static boolean REQUIRE_OP_TO_USE_REPLACE = false;
		@Comment(value = { "Default: false" })
		public static boolean DISABLE_ANIMATIONS = false;
		@Comment(value = { "Default: false -> Double-tap to switch to the secondary group" })
		public static boolean SNEAK_TO_SWITCH_GROUP = false;
		@Comment(value = { "Default: false" })
		public static boolean INVERT_REPLACE_LOCK = false;
		@Comment(value = { "Default: true", "'true' is highly recommended" })
		public static boolean SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT = true;
		@RangeInt(min = 0, max = 256)
		public static int REACH_RANGE_AT_DEFAULT = 6;
		@RangeInt(min = -256, max = 256)
		public static int REACH_RANGE_AT_EXTENSION = 100;
	}

	public static void onConfigUpdate() {
		ConfigManager.sync(LotTweaks.MODID, Type.INSTANCE);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();
		if (event.getSide() == Side.CLIENT) {
			LotTweaksClient.init();
		}
	}

	@EventHandler
	public void init(FMLPostInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			ItemGroupManager.init();
		}
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
		MinecraftForge.EVENT_BUS.register(new ServerConnectionListener());
	}

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
		if (clientVersion.compareTo(serverVersion) >= 0) {
			return true;
		} else {
			return false;
		}
	}

}
