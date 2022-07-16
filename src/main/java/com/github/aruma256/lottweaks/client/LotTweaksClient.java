package com.github.aruma256.lottweaks.client;

import org.lwjgl.input.Keyboard;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.keys.ExtendReachRangeKey;
import com.github.aruma256.lottweaks.client.keys.ExPickKey;
import com.github.aruma256.lottweaks.client.keys.ReplaceKey;
import com.github.aruma256.lottweaks.client.keys.OpenPaletteKey;
import com.github.aruma256.lottweaks.client.keys.RotateRowKey;
import com.github.aruma256.lottweaks.network.LTPacketHandler;
import com.github.aruma256.lottweaks.network.LTPacketHandler.HelloMessageHandler.HelloCallback;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LotTweaksClient implements HelloCallback
{

	public static void init() {
    	KeyBinding key;
		key = new ExPickKey(Keyboard.KEY_V, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new OpenPaletteKey(Keyboard.KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateRowKey(Keyboard.KEY_K, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(Keyboard.KEY_G, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ExtendReachRangeKey(Keyboard.KEY_U, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		//
		LotTweaksClient instance = new LotTweaksClient();
		MinecraftForge.EVENT_BUS.register(instance);
		LTPacketHandler.HelloMessageHandler.callback = instance;
		//
		ClientCommandHandler.instance.registerCommand(new LotTweaksCommand());
		MinecraftForge.EVENT_BUS.register(IngameLog.instance);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(final ClientConnectedToServerEvent event) {
		IngameLog.instance.show();
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(final ClientDisconnectionFromServerEvent event) {
		CompatibilityChecker.instance.clearServerModVersion();
	}

	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(LotTweaks.MODID)) {
			LotTweaks.onConfigUpdate();
		}
	}

	@Override
	public void onHello(String version) {
		CompatibilityChecker.instance.setServerModVersion(version);
		LTPacketHandler.sendReachRangeMessage(LotTweaks.CONFIG.REACH_RANGE_AT_DEFAULT);
	}

}
