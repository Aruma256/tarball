package com.github.lotqwerty.lottweaks.client;

import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.RotationHelper;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent.ClientChatEventListener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.LiteralText;

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
			String[] part = msg.split(" ");
			if (part.length != 2) {
				throw new CommandException(new LiteralText(Formatting.RED + COMMAND_USAGE));
			}
			if (part[1].equals("add")){
				executeAdd();
			} else if (part[1].equals("reload")){
				executeReload();
			} else {
				throw new CommandException(new LiteralText(Formatting.RED + COMMAND_USAGE));
			}
		} catch (CommandException e) {
			displayMessage(e.getTextMessage());
		}
	}
	
	
	private void executeAdd() throws CommandException {
		MinecraftClient mc = MinecraftClient.getInstance();
		StringJoiner stringJoiner = new StringJoiner(",");
		int count = 0;
		for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
			ItemStack itemStack = mc.player.inventory.getStack(i);
			if (RotationHelper.canRotate(itemStack)) {
				throw new CommandException(new LiteralText(String.format("Already exists (%d)", i + 1)));
			}
			if (itemStack.isEmpty()) {
				break;
			}
			Block block = Block.getBlockFromItem(itemStack.getItem());
			if (block == Blocks.AIR) {
				throw new CommandException(new LiteralText(String.format("Failed to get block instance. (%d)", i + 1)));
			}
			String name = Registry.BLOCK.getId(block).toString();
			System.out.println(name);
			stringJoiner.add(name);
			count++;
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandException(new LiteralText(String.format("Hotbar is empty.")));
		}
		LotTweaks.LOGGER.debug("adding a new block-group from /lottweaks command");
		LotTweaks.LOGGER.debug(line);
		String[] oldBlockGroups = RotationHelper.BLOCK_GROUPS;
		String[] newBlockGroups = new String[oldBlockGroups.length + 1];
		System.arraycopy(oldBlockGroups, 0, newBlockGroups, 0, oldBlockGroups.length);
		newBlockGroups[newBlockGroups.length - 1] = line;
		boolean succeeded = RotationHelper.tryToUpdateBlockGroupsFromCommand(newBlockGroups);
		if (succeeded) {
			RotationHelper.writeToFile();
			displayMessage(new LiteralText(String.format("LotTweaks: added %d blocks", count)));
		} else {
			displayMessage(new LiteralText(Formatting.RED + "LotTweaks: failed to add blocks"));
		}
	}

	private void executeReload() throws CommandException {
		RotationHelper.loadFromFile();
		RotationHelper.loadBlockGroups();
		MinecraftClient mc = MinecraftClient.getInstance();
		int groupCount = RotationHelper.BLOCK_CHAIN.size();
		if (groupCount > 0) {
			displayMessage(new LiteralText("LotTweaks: reload succeeded!"));
		} else {
			displayMessage(new LiteralText(Formatting.RED + "LotTweaks: failed to reload config file (0 blocks loaded)"));
		}
	}

}
