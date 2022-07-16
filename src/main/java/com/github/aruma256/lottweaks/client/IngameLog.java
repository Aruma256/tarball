package com.github.aruma256.lottweaks.client;

import java.util.ArrayDeque;
import java.util.Queue;

import com.github.aruma256.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class IngameLog {

	public static final IngameLog instance = new IngameLog();

	private final Queue<String> logQueue = new ArrayDeque<>();

	private IngameLog() {
	}

	public void addInfoLog(String message) {
		logQueue.add(message);
	}

	public void addErrorLog(String message) {
		logQueue.add(TextFormatting.RED + message);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(final ClientConnectedToServerEvent event) {
		show();
	}

	public void show() {
		if (LotTweaks.CONFIG.SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT) {
			while (!logQueue.isEmpty()) {
				Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString("LotTweaks: " + logQueue.poll()));
			}
		}
	}

	public String debug_pollLog() {
		return logQueue.poll();
	}

}
