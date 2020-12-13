package com.github.lotqwerty.lottweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

@Mod(modid = LotTweaks.MODID, name = LotTweaks.NAME, version = LotTweaks.VERSION)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "1.2.8";
	public static Logger LOGGER;

	private static final String HAS_BEEN_MOVED = String.format("'BLOCK_GROUPS' config has been moved to '%s'", RotationHelper.BLOCKGROUP_CONFFILE);

	@Config(modid = MODID, type = Type.INSTANCE, name = NAME)
	public static class CONFIG {
		public static String[] BLOCK_GROUPS = {HAS_BEEN_MOVED};
		@RangeInt(min = 0, max = 256)
		public static int MAX_RANGE = 128;
		@RangeInt(min = 1, max = 120)
		public static int REPLACE_INTERVAL = 1;
		public static boolean REQUIRE_OP_TO_USE_REPLACE = false;
		public static boolean DISABLE_ANIMATIONS = false;
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
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			if (CONFIG.BLOCK_GROUPS.length > 0 && !CONFIG.BLOCK_GROUPS[0].equals(HAS_BEEN_MOVED)) {
				RotationHelper.BLOCK_GROUPS = CONFIG.BLOCK_GROUPS;
				RotationHelper.writeToFile();
			}
			RotationHelper.loadFromFile();
			RotationHelper.loadBlockGroups();
		}
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
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
}
