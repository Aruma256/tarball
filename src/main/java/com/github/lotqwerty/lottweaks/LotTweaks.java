package com.github.lotqwerty.lottweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;
import com.github.lotqwerty.lottweaks.network.ServerConnectionListener;

@Mod(modid = LotTweaks.MODID, name = LotTweaks.NAME, version = LotTweaks.VERSION)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "2.2.1";
	public static Logger LOGGER;

	private static final String HAS_BEEN_MOVED = String.format("'BLOCK_GROUPS' config has been moved to '%s'", RotationHelper.ITEMGROUP_CONFFILE_MAIN);

	@Config(modid = MODID, type = Type.INSTANCE, name = NAME)
	public static class CONFIG {
		public static String[] BLOCK_GROUPS = {HAS_BEEN_MOVED};
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
			if (CONFIG.BLOCK_GROUPS.length > 0 && !CONFIG.BLOCK_GROUPS[0].equals(HAS_BEEN_MOVED)) {
				RotationHelper.ITEM_GROUPS_STRLIST_MAIN = Arrays.asList(CONFIG.BLOCK_GROUPS);
				RotationHelper.writeAllToFile();
			}
			RotationHelper.loadAllFromFile();
			RotationHelper.loadAllItemGroupFromStrArray();
		}
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
		MinecraftForge.EVENT_BUS.register(new ServerConnectionListener());
	}

	@EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		if (event.getSide() == Side.CLIENT) {
			if (CONFIG.BLOCK_GROUPS.length > 0 && !CONFIG.BLOCK_GROUPS[0].equals(HAS_BEEN_MOVED)) {
				CONFIG.BLOCK_GROUPS = new String[]{HAS_BEEN_MOVED};
				onConfigUpdate();
			}
		}
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
