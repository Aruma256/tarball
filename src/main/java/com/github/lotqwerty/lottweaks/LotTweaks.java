package com.github.lotqwerty.lottweaks;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.client.LotTweaksClient;

@Mod(modid = LotTweaks.MODID, name = LotTweaks.NAME, version = LotTweaks.VERSION)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "1.1.1";
	public static Logger logger;

	@Config(modid = MODID, type = Type.INSTANCE, name = NAME)
	public static class CONFIG {
		@RangeDouble(min = 0.0, max = 250.0)
		public static double REPLACE_RANGE = 50.0;
		@RangeInt(min = 1, max = 120)
		public static int REPLACE_INTERVAL = 1;
		public static String[] BLOCK_GROUPS = RotationHelper.DEFAULT_BLOCK_GROUPS;
	}

	public static void onConfigUpdate() {
		ConfigManager.sync(LotTweaks.MODID, Type.INSTANCE);
		RotationHelper.loadBlockGroups();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		if (event.getSide() == Side.CLIENT) {
			LotTweaksClient.init();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		RotationHelper.loadBlockGroups();
		ReplacePacketHandler.init();
	}

}
