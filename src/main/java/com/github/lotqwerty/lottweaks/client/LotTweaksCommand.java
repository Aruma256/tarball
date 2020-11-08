package com.github.lotqwerty.lottweaks.client;

import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.RotationHelper;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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
			String[] part = msg.split(" ");
			if (part.length != 2) {
				throw new CommandException(new StringTextComponent(TextFormatting.RED + COMMAND_USAGE));
			}
			if (part[1].equals("add")){
				executeAdd();
			} else if (part[1].equals("reload")){
				executeReload();
			} else {
				throw new CommandException(new StringTextComponent(TextFormatting.RED + COMMAND_USAGE));
			}
		} catch (CommandException e) {
			displayMessage(e.getComponent());
		}
	}
	
	
	private void executeAdd() throws CommandException {
		Minecraft mc = Minecraft.getInstance();
		StringJoiner stringJoiner = new StringJoiner(",");
		int count = 0;
		for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
			if (RotationHelper.canRotate(itemStack)) {
				throw new CommandException(new StringTextComponent(String.format("Already exists (%d)", i + 1)));
			}
			if (itemStack.isEmpty()) {
				break;
			}
			Block block = Block.getBlockFromItem(itemStack.getItem());
			if (block == Blocks.AIR) {
				throw new CommandException(new StringTextComponent(String.format("Failed to get block instance. (%d)", i + 1)));
			}
			String name = ForgeRegistries.BLOCKS.getKey(block).toString();
			stringJoiner.add(name);
			count++;
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandException(new StringTextComponent(String.format("Hotbar is empty.")));
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
			displayMessage(new StringTextComponent(String.format("LotTweaks: added %d blocks", count)));
		} else {
			displayMessage(new StringTextComponent(TextFormatting.RED + "LotTweaks: failed to add blocks"));
		}
	}

	private void executeReload() throws CommandException {
		RotationHelper.loadFromFile();
		RotationHelper.loadBlockGroups();
		Minecraft mc = Minecraft.getInstance();
		int groupCount = RotationHelper.BLOCK_CHAIN.size();
		if (groupCount > 0) {
			displayMessage(new StringTextComponent("LotTweaks: reload succeeded!"));
		} else {
			displayMessage(new StringTextComponent(TextFormatting.RED + "LotTweaks: failed to reload config file (0 blocks loaded)"));
		}
	}

}
