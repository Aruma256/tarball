package com.github.aruma256.lottweaks.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.aruma256.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LotTweaksCommand {

	private static final String COMMAND_NAME = LotTweaks.MODID;
	private static final String COMMAND_USAGE = String.format("/%s add", COMMAND_NAME);

	public String getName() {
		return COMMAND_NAME;
	}

	/*
	@Override
	public String getUsage(ICommandSender sender) {
		return COMMAND_USAGE;
	}
	*/

	@SubscribeEvent
	public void onClientSendChat(final ClientChatEvent event) {
		String rawMessage = event.getMessage();
		if (!rawMessage.startsWith("/" + getName())) return;
		event.setCanceled(true);
		try {
			List<String> tmpArgList = new ArrayList<>(Arrays.asList(rawMessage.split(" ")));
			tmpArgList.remove(0);
			execute((String[])tmpArgList.toArray(new String[0]));
		} catch (CommandException e) {
			IngameLog.instance.addErrorLog(e.message);
			IngameLog.instance.show();
		}
	}

	static class CommandException extends RuntimeException {
		String message;
		public CommandException(String message) {
			this.message = message;
		}
		public CommandException(String message, Object obj) {
			this(message);
		}
	}

	public void execute(String[] args) throws CommandException {
		if (args.length < 1) {
			throw new CommandException("'/lottweaks add' or '/lottweaks reload'");
		}
		if (args[0].equals("add")) {
			if (args.length == 2) {
				if (args[1].equals("0") || args[1].equals("main") || args[1].equals("primary")) {
					executeAdd(0);
				} else if (args[1].equals("1") || args[1].equals("sub") || args[1].equals("secondary")) {
					executeAdd(1);
				} else {
					throw new CommandException("/lottweaks add [0, 1]");
				}
			} else {
				executeAdd(0);
			}
		} else if (args[0].equals("reload")) {
			executeReload();
		}
	}

	private void executeAdd(int listId) throws CommandException {
		System.out.println("ADD!!!!!!!!!");
		List<ItemState> group = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < PlayerInventory.getSelectionSize(); i++) {
			ItemStack itemStack = Minecraft.getInstance().player.inventory.getItem(i);
			if (itemStack.isEmpty()) {
				break;
			}
			if (ItemGroupManager.getInstance(listId).isRegistered(itemStack)) {
				throw new CommandException(String.format("LotTweaks: The item in the slot %d is already registered.", i+1), new Object[0]);
			}
			group.add(new ItemState(itemStack));
			count++;
		}
		if (ItemGroupManager.getInstance(listId).addGroup(group)) {
			ItemGroupManager.save();
			IngameLog.instance.addInfoLog(String.format("added %d items", count));
		} else {
			IngameLog.instance.addErrorLog("failed to create a new group");
		}
		IngameLog.instance.show();
	}

	private void executeReload() {
		if (ItemGroupManager.init()) {
			IngameLog.instance.addInfoLog("reload succeeded!");
		} else {
			IngameLog.instance.addErrorLog("reload failed");
		}
		IngameLog.instance.show();
	}

}

