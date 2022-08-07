package com.github.aruma256.lottweaks.client;

import java.util.ArrayDeque;
import java.util.Queue;

import com.github.aruma256.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
	public void onPlayerLoggedIn(final LoggedInEvent event) {
		show();
	}

	public void show() {
		System.out.println("SHOW!!!!!!!");
		if (LotTweaks.CONFIG.SHOW_BLOCKCONFIG_ERROR_LOG_TO_CHAT.get()) {
			while (!logQueue.isEmpty()) {
				Minecraft.getInstance().gui.handleChat(ChatType.SYSTEM, new StringTextComponent("LotTweaks: " + logQueue.poll()), Util.NIL_UUID);
			}
		}
	}

	public String debug_pollLog() {
		return logQueue.poll();
	}

}
