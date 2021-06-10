package com.github.lotqwerty.lottweaks.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent.ClientChatEventListener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import com.github.lotqwerty.lottweaks.client.RotationHelper.Group;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.LiteralText;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class LotTweaksCommand implements ClientChatEventListener {

	private static final String COMMAND_NAME = '/' + LotTweaks.MODID;
	private static final String COMMAND_USAGE = String.format("/%s <arg : 'add' or 'reload'>", COMMAND_NAME);

	private static void displayMessage(Text textComponent) {
		MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.SYSTEM, textComponent, null);
	}

	@Override
	public void onClientSendChat(final ClientChatEvent event) {
		String msg = event.getMessage();
		if (!msg.startsWith(COMMAND_NAME)) {
			return;
		}
		event.setCanceled(true);
		try {
			List<String> argsList = new ArrayList<>(Arrays.asList(msg.split(" ")));
			if (!argsList.get(0).equals(COMMAND_NAME)) {
				throw new CommandException(new LiteralText(Formatting.RED + COMMAND_USAGE));
			}
			argsList.remove(0);
			String[] args = argsList.toArray(new String[0]);
			//
			if (args.length < 1) {
				throw new CommandException(new LiteralText(Formatting.RED + COMMAND_USAGE));
			}
			if (args[0].equals("add")) {
				if (args.length == 2) {
					if (args[1].equals("1") || args[1].equals("main")) {
						executeAdd(Group.MAIN);
					} else if (args[1].equals("2") || args[1].equals("sub")) {
						executeAdd(Group.SUB);
					} else {
						throw new CommandException(new LiteralText(Formatting.RED + COMMAND_USAGE));
					}
				} else {
					executeAdd(Group.MAIN);
				}
			} else if (args[0].equals("reload")) {
				executeReload();
			}
		} catch (CommandException e) {
			displayMessage(e.getTextMessage());
		}
	}
	
	private void executeAdd(Group group) throws CommandException {
	MinecraftClient mc = MinecraftClient.getInstance();
		StringJoiner stringJoiner = new StringJoiner(",");
		int count = 0;
		for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
			ItemStack itemStack = mc.player.getInventory().getStack(i);
			if (RotationHelper.canRotate(itemStack, group)) {
				throw new CommandException(new LiteralText(String.format("Already exists (%d)", i + 1)));
			}
			if (itemStack.isEmpty()) {
				break;
			}
			Item item = itemStack.getItem();
			if (item == Items.AIR) {
				throw new CommandException(new LiteralText(String.format("Failed to get item instance. (%d)", i + 1)));
			}
			String name = Registry.ITEM.getId(item).toString();
			stringJoiner.add(name);
			count++;
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandException(new LiteralText(String.format("Hotbar is empty.")));
		}
		LotTweaks.LOGGER.debug("adding a new block/item-group from /lottweaks command");
		LotTweaks.LOGGER.debug(line);
		boolean succeeded = RotationHelper.tryToAddItemGroupFromCommand(line, group);
		if (succeeded) {
			displayMessage(new LiteralText(String.format("LotTweaks: added %d blocks/items", count)));
		} else {
			displayMessage(new LiteralText(Formatting.RED + "LotTweaks: failed to add blocks/items"));
		}
	}

	private void executeReload() throws CommandException {
		try {
			boolean f;
			f = RotationHelper.loadAllFromFile();
			if (!f) throw new CommandException(new LiteralText("LotTweaks: failed to reload config file"));
			f = RotationHelper.loadAllItemGroupFromStrArray();
			if (!f) throw new CommandException(new LiteralText("LotTweaks: failed to reload blocks"));
			displayMessage(new LiteralText("LotTweaks: reload succeeded!"));
		} catch (CommandException e) {
			displayMessage(new LiteralText(Formatting.RED + e.getMessage()));
		}
	}

}
