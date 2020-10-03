package com.github.lotqwerty.lottweaks.client;

import java.util.StringJoiner;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.RotationHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

public class LotTweaksCommand  extends CommandBase implements IClientCommand {

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
		if (args.length != 1 || !args[0].equals("add")) {
			throw new WrongUsageException(getUsage(sender), new Object[0]);
		}
		Minecraft mc = Minecraft.getMinecraft();
		StringJoiner stringJoiner = new StringJoiner(",");
		for(int i=0;i<InventoryPlayer.getHotbarSize();i++) {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
			if (RotationHelper.canRotate(itemStack)) {
				throw new CommandException(String.format("Already exists (%d)", i+1),  new Object[0]);
			}
			System.out.println("Hello");
			if (itemStack.isEmpty()) {
				break;
			}
			Block block = Block.getBlockFromItem(itemStack.getItem());
			int meta = itemStack.getItemDamage();
			if (block == Blocks.AIR) {
				throw new CommandException(String.format("Failed to get block instance. (%d)", i+1),  new Object[0]);
			}
			String name = Block.REGISTRY.getNameForObject(block).toString();
			if (meta == 0) {
				stringJoiner.add(name);
			} else {
				stringJoiner.add(String.format("%s/%d", name, meta));
			}
		}
		String line = stringJoiner.toString();
		if (line.isEmpty()) {
			throw new CommandException(String.format("Hotbar is empty."),  new Object[0]);
		}
		LotTweaks.logger.debug("adding a new block-group from /lottweaks command");
		LotTweaks.logger.debug(line);
		String[] oldBlockGroups = LotTweaks.CONFIG.BLOCK_GROUPS;
		String[] newBlockGroups = new String[oldBlockGroups.length + 1];
		System.arraycopy(oldBlockGroups, 0, newBlockGroups, 0, oldBlockGroups.length);
		newBlockGroups[newBlockGroups.length - 1] = line;
		LotTweaks.CONFIG.BLOCK_GROUPS = newBlockGroups;
		LotTweaks.onConfigUpdate();
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
		return false;
	}

}
