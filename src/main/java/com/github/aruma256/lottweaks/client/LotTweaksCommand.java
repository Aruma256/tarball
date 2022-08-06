package com.github.aruma256.lottweaks.client;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.ArrayList;
import java.util.List;

import com.github.aruma256.lottweaks.LotTweaks;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
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
				if (args[1].equals("0") || args[1].equals("main") || args[1].equals("primary")) {
					executeAdd(0);
				} else if (args[1].equals("1") || args[1].equals("sub") || args[1].equals("secondary")) {
					executeAdd(1);
				} else {
					throw new WrongUsageException(getUsage(sender), new Object[0]);
				}
			} else {
				executeAdd(0);
			}
		} else if (args[0].equals("reload")) {
			executeReload();
		}
	}

	private void executeAdd(int listId) throws CommandException {
		List<ItemState> group = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
			ItemStack itemStack = getClientPlayer().inventory.getStackInSlot(i);
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

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
