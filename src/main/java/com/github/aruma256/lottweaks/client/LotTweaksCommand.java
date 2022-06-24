package com.github.aruma256.lottweaks.client;

import java.util.ArrayList;
import java.util.List;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.ItemGroupManager.Group;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.IClientCommand;

public class LotTweaksCommand extends CommandBase implements IClientCommand {

	private static final String COMMAND_NAME = LotTweaks.MODID;
	private static final String COMMAND_USAGE = String.format("/%s add", COMMAND_NAME);

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return COMMAND_USAGE;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException(getUsage(sender), new Object[0]);
		}
		if (args[0].equals("add")) {
			if (args.length == 2) {
				if (args[1].equals("1") || args[1].equals("main") || args[1].equals("primary")) {
					executeAdd(Group.PRIMARY);
				} else if (args[1].equals("2") || args[1].equals("sub") || args[1].equals("secondary")) {
					executeAdd(Group.SECONDARY);
				} else {
					throw new WrongUsageException(getUsage(sender), new Object[0]);
				}
			} else {
				executeAdd(Group.PRIMARY);
			}
		} else if (args[0].equals("reload")) {
			executeReload();
		}
	}

	private void executeAdd(Group groupType) throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();
		List<ItemState> group = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
			if (itemStack.isEmpty()) {
				break;
			}
			if (ItemGroupManager.getInstance(groupType).isRegistered(itemStack)) {
				throw new CommandException(String.format("LotTweaks: The item in the slot %d is already registered.", i+1), new Object[0]);
			}
			group.add(new ItemState(itemStack));
			count++;
		}
		if (ItemGroupManager.getInstance(groupType).addGroupFromCommand(group)) {
			ItemGroupManager.save();
			mc.ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(String.format("LotTweaks: added %d items", count)));
		} else {
			mc.ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(TextFormatting.RED + "LotTweaks: failed to create a new group"));
			LotTweaksClient.showErrorLogToChat();
		}
	}

	private void executeReload() {
		Minecraft mc = Minecraft.getMinecraft();
		if (ItemGroupManager.init()) {
			mc.ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString("LotTweaks: reload succeeded!"));
		} else {
			mc.ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString("LotTweaks: reload failed"));
		}
		LotTweaksClient.showErrorLogToChat();
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
