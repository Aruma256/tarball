package com.github.lotqwerty.lottweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.lotqwerty.lottweaks.client.LotTweaksClient;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

@Mod(LotTweaks.MODID)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "1.2.6_beta021";
	public static Logger LOGGER = LogManager.getLogger();

	public static class CONFIG {
		public static double REPLACE_RANGE = 50.0;
		public static int REPLACE_INTERVAL = 1;
		public static boolean REQUIRE_OP_TO_USE_REPLACE = false;
	}

	public LotTweaks() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::clientInit);
		modEventBus.addListener(this::commonInit);
		
	}

	private void clientInit(FMLClientSetupEvent event) {
		LotTweaksClient.init();
	}

	private void commonInit(FMLCommonSetupEvent event) {
		RotationHelper.loadFromFile();
		RotationHelper.loadBlockGroups();
		LTPacketHandler.init();
		MinecraftForge.EVENT_BUS.register(new AdjustRangeHelper());
	}

}
