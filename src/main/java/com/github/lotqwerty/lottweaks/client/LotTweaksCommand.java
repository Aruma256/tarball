package com.github.lotqwerty.lottweaks.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.RotationHelper.Group;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class LotTweaksCommand {

	private static final String COMMAND_NAME = '/' + LotTweaks.MODID;
	private static final String COMMAND_USAGE = String.format("/%s <arg : 'add' or 'reload'>", COMMAND_NAME);

	private static void displayMessage(Component textComponent) {
		Minecraft.getInstance().gui.handleSystemChat(BuiltinRegistries.CHAT_TYPE.get(ChatType.SYSTEM), textComponent);
	}

	@SubscribeEvent
	public void onClientSendChat(final ClientChatEvent event) {
		String msg = event.getMessage();
		if (!msg.startsWith(COMMAND_NAME)) {
			return;
		}
		event.setCanceled(true);
		try {
			List<String> argsList = new ArrayList<>(Arrays.asList(msg.split(" ")));
			if (!argsList.get(0).equals(COMMAND_NAME)) {
				throw new CommandRuntimeException(Component.literal(ChatFormatting.RED + COMMAND_USAGE));
			}
			argsList.remove(0);
			String[] args = argsList.toArray(new String[0]);
			//
			if (args.length < 1) {
				throw new CommandRuntimeException(Component.literal(ChatFormatting.RED + COMMAND_USAGE));
			}
			if (args[0].equals("add")) {
				if (args.length == 2) {
					if (args[1].equals("1") || args[1].equals("main") || args[1].equals("primary")) {
						executeAdd(Group.PRIMARY);
					} else if (args[1].equals("2") || args[1].equals("sub") || args[1].equals("secondary")) {
						executeAdd(Group.SECONDARY);
					} else {
						throw new CommandRuntimeException(Component.literal(ChatFormatting.RED + COMMAND_USAGE));
					}
				} else {
					executeAdd(Group.PRIMARY);
				}
			} else if (args[0].equals("reload")) {
				executeReload();
			}
		} catch (CommandRuntimeException e) {
			displayMessage(e.getComponent());
		}
	}
	
	private void executeAdd(Group group) throws CommandRuntimeException {
		Minecraft mc = Minecraft.getInstance();
		StringJoiner stringJoiner = new StringJoiner(",");
		int count = 0;
		for (int i = 0; i < Inventory.getSelectionSize(); i++) {
			ItemStack itemStack = mc.player.getInventory().getItem(i);
			if (RotationHelper.canRotate(itemStack, group)) {
				throw new CommandRuntimeException(Component.literal(String.format("Already exists (%d)", i + 1)));
			}
			if (itemStack.isEmpty()) {
				break;
			}
			Item item = itemStack.getItem();
			if (item == Items.AIR) {
				throw new CommandRuntimeException(Component.literal(String.format("Failed to get item instance. (%d)", i + 1)));
			}
			String name = ForgeRegistries.ITEMS.getKey(item).toString();
			stringJoiner.add(name);
			count++;
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandRuntimeException(Component.literal(String.format("Hotbar is empty.")));
		}
		LotTweaks.LOGGER.debug("adding a new block/item-group from /lottweaks command");
		LotTweaks.LOGGER.debug(line);
		boolean succeeded = RotationHelper.tryToAddItemGroupFromCommand(line, group);
		if (succeeded) {
			displayMessage(Component.literal(String.format("LotTweaks: added %d blocks/items", count)));
		} else {
			displayMessage(Component.literal(ChatFormatting.RED + "LotTweaks: failed to add blocks/items"));
		}
	}

	private void executeReload() throws CommandRuntimeException {
		try {
			boolean f;
			f = RotationHelper.loadAllFromFile();
			if (!f) throw new CommandRuntimeException(Component.literal("LotTweaks: failed to reload config file"));
			f = RotationHelper.loadAllItemGroupFromStrArray();
			if (!f) throw new CommandRuntimeException(Component.literal("LotTweaks: failed to reload blocks"));
			displayMessage(Component.literal("LotTweaks: reload succeeded!"));
		} catch (CommandRuntimeException e) {
			displayMessage(Component.literal(ChatFormatting.RED + e.getMessage()));
		}
		LotTweaksClient.showErrorLogToChat();
	}

}
