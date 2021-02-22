package com.github.lotqwerty.lottweaks.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.client.RotationHelper.Group;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class LotTweaksCommand {

	private static final String COMMAND_NAME = '/' + LotTweaks.MODID;
	private static final String COMMAND_USAGE = String.format("/%s <arg : 'add' or 'reload'>", COMMAND_NAME);

	private static void displayMessage(ITextComponent textComponent) {
		Minecraft.getInstance().ingameGUI.func_238450_a_(ChatType.SYSTEM, textComponent, null);
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
				throw new CommandException(new StringTextComponent(TextFormatting.RED + COMMAND_USAGE));
			}
			argsList.remove(0);
			String[] args = argsList.toArray(new String[0]);
			//
			if (args.length < 1) {
				throw new CommandException(new StringTextComponent(TextFormatting.RED + COMMAND_USAGE));
			}
			if (args[0].equals("add")) {
				if (args.length == 2) {
					if (args[1].equals("1") || args[1].equals("main")) {
						executeAdd(Group.MAIN);
					} else if (args[1].equals("2") || args[1].equals("sub")) {
						executeAdd(Group.SUB);
					} else {
						throw new CommandException(new StringTextComponent(TextFormatting.RED + COMMAND_USAGE));
					}
				} else {
					executeAdd(Group.MAIN);
				}
			} else if (args[0].equals("reload")) {
				executeReload();
			}
		} catch (CommandException e) {
			displayMessage(e.getComponent());
		}
	}
	
	private void executeAdd(Group group) throws CommandException {
		Minecraft mc = Minecraft.getInstance();
		StringJoiner stringJoiner = new StringJoiner(",");
		int count = 0;
		for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
			if (RotationHelper.canRotate(itemStack, group)) {
				throw new CommandException(new StringTextComponent(String.format("Already exists (%d)", i + 1)));
			}
			if (itemStack.isEmpty()) {
				break;
			}
			Item item = itemStack.getItem();
			if (item == Items.AIR) {
				throw new CommandException(new StringTextComponent(String.format("Failed to get item instance. (%d)", i + 1)));
			}
			String name = ForgeRegistries.ITEMS.getKey(item).toString();
			stringJoiner.add(name);
			count++;
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandException(new StringTextComponent(String.format("Hotbar is empty.")));
		}
		LotTweaks.LOGGER.debug("adding a new block/item-group from /lottweaks command");
		LotTweaks.LOGGER.debug(line);
		boolean succeeded = RotationHelper.tryToAddItemGroupFromCommand(line, group);
		if (succeeded) {
			displayMessage(new StringTextComponent(String.format("LotTweaks: added %d blocks/items", count)));
		} else {
			displayMessage(new StringTextComponent(TextFormatting.RED + "LotTweaks: failed to add blocks/items"));
		}
	}

	private void executeReload() throws CommandException {
		try {
			boolean f;
			f = RotationHelper.loadAllFromFile();
			if (!f) throw new CommandException(new StringTextComponent("LotTweaks: failed to reload config file"));
			f = RotationHelper.loadAllItemGroupFromStrArray();
			if (!f) throw new CommandException(new StringTextComponent("LotTweaks: failed to reload blocks"));
			displayMessage(new StringTextComponent("LotTweaks: reload succeeded!"));
		} catch (CommandException e) {
			displayMessage(new StringTextComponent(TextFormatting.RED + e.getMessage()));
		}
	}

}
