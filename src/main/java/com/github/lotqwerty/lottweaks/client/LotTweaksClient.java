package com.github.lotqwerty.lottweaks.client;

import org.lwjgl.input.Keyboard;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;
import com.github.lotqwerty.lottweaks.client.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.client.keys.RotateKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
public class LotTweaksClient
{
	private static String serverVersion = "0";

	public static void init() {
    	KeyBinding key;
		key = new ExPickKey(Keyboard.KEY_V, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateKey(Keyboard.KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(Keyboard.KEY_G, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new AdjustRangeKey(Keyboard.KEY_U, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		//
		MinecraftForge.EVENT_BUS.register(new LotTweaksClient());
		//
		ClientCommandHandler.instance.registerCommand(new LotTweaksCommand());
	}

	public static boolean requireServerVersion(String requiredVersion) {
		return (serverVersion.compareTo(requiredVersion) >= 0);
	}

	public static void clearServerVersion() {
		setServerVersion("0");
	}

	public static void setServerVersion(String version) {
		serverVersion = version;
	}

	public static String getServerVersion() {
		return serverVersion;
	}

	public static void showErrorLogToChat() {
		if (LotTweaks.CONFIG.SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT) {
			Minecraft mc = Minecraft.getMinecraft();
			for (String line : RotationHelper.LOG_GROUP_CONFIG) {
				mc.ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(String.format("LotTweaks: %s%s", TextFormatting.RED, line)));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(final ClientConnectedToServerEvent event) {
		showErrorLogToChat();
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(final ClientDisconnectionFromServerEvent event) {
		clearServerVersion();
	}

	@SubscribeEvent
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(LotTweaks.MODID)) {
			LotTweaks.onConfigUpdate();
		}
	}

}
