package com.github.aruma256.lottweaks.client;

import org.lwjgl.glfw.GLFW;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.keys.ExtendReachRangeKey;
import com.github.aruma256.lottweaks.client.keys.ExPickKey;
import com.github.aruma256.lottweaks.client.keys.ReplaceKey;
import com.github.aruma256.lottweaks.client.keys.OpenPaletteKey;
import com.github.aruma256.lottweaks.client.keys.RotateRowKey;
import com.github.aruma256.lottweaks.network.LTPacketHandler;
import com.github.aruma256.lottweaks.network.LTPacketHandler.HelloMessage.HelloCallback;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class LotTweaksClient implements HelloCallback
{

	public static void init() {
    	KeyBinding key;
		key = new ExPickKey(GLFW.GLFW_KEY_V, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new OpenPaletteKey(GLFW.GLFW_KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateRowKey(GLFW.GLFW_KEY_K, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(GLFW.GLFW_KEY_G, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ExtendReachRangeKey(GLFW.GLFW_KEY_U, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		//
		LotTweaksClient instance = new LotTweaksClient();
		MinecraftForge.EVENT_BUS.register(instance);
		LTPacketHandler.HelloMessage.callback = instance;
		//
		MinecraftForge.EVENT_BUS.register(new LotTweaksCommand());
		MinecraftForge.EVENT_BUS.register(IngameLog.instance);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(final ClientPlayerNetworkEvent.LoggedInEvent event) {
		IngameLog.instance.show();
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(final ClientPlayerNetworkEvent.LoggedOutEvent event) {
		CompatibilityChecker.instance.clearServerModVersion();
	}

	/*
	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(LotTweaks.MODID)) {
			LotTweaks.onConfigUpdate();
		}
	}
	*/

	@Override
	public void onHello(String version) {
		CompatibilityChecker.instance.setServerModVersion(version);
		LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_DEFAULT.get());
	}

}
