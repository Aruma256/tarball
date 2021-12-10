package com.github.lotqwerty.lottweaks.client;

import org.lwjgl.glfw.GLFW;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;
import com.github.lotqwerty.lottweaks.client.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.client.keys.RotateKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class LotTweaksClient
{
	private static String serverVersion = "0";

	public static void init() {
		registerKey(new ExPickKey(GLFW.GLFW_KEY_V, LotTweaks.NAME));
		registerKey(new RotateKey(GLFW.GLFW_KEY_R, LotTweaks.NAME));
		registerKey(new ReplaceKey(GLFW.GLFW_KEY_G, LotTweaks.NAME));
		registerKey(new AdjustRangeKey(GLFW.GLFW_KEY_U, LotTweaks.NAME));
		//
		MinecraftForge.EVENT_BUS.register(new LotTweaksClient());
		//
		MinecraftForge.EVENT_BUS.register(new LotTweaksCommand());
	}

	private static void registerKey(KeyMapping key) {
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		if (key instanceof IIngameOverlay) {
			OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, key.getName(), (IIngameOverlay)key);
		}
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
		if (LotTweaks.CONFIG.SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT.get()) {
			Minecraft mc = Minecraft.getInstance();
			for (String line : RotationHelper.LOG_GROUP_CONFIG) {
				mc.gui.handleChat(ChatType.SYSTEM, new TextComponent(String.format("LotTweaks: %s%s", ChatFormatting.RED, line)), Util.NIL_UUID);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(final ClientPlayerNetworkEvent.LoggedInEvent event) {
		showErrorLogToChat();
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServer(final ClientPlayerNetworkEvent.LoggedOutEvent event) {
		clearServerVersion();
	}

}
