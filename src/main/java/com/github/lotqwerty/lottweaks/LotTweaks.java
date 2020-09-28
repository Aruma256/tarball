package com.github.lotqwerty.lottweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.apache.logging.log4j.Logger;

@Mod(modid = LotTweaks.MODID, name = LotTweaks.NAME, version = LotTweaks.VERSION)
public class LotTweaks {

	public static final String MODID = "lottweaks";
	public static final String NAME = "LotTweaks";
	public static final String VERSION = "1.0.9";
	public static Logger logger;
	
	public static final HashMap<IBlockState, IBlockState> BLOCK_CHAIN = new HashMap<>();

	private static String toRotateStr(String name, int max) {
		StringJoiner joiner = new StringJoiner(",");
		for (int i=0;i<max;i++) {
			joiner.add(String.format("%s/%d", name, i));
		}
		return joiner.toString();
	}
	
	@Config(modid = MODID, type = Type.INSTANCE, name = NAME)
	public static class CONFIG {
		@RangeDouble(min = 0.0, max = 250.0)
		public static double REPLACE_RANGE = 50.0;
		@RangeInt(min = 1, max = 120)
		public static int REPLACE_INTERVAL = 1;
		public static String[] BLOCK_GROUPS = {
			"//STONE",
			toRotateStr("minecraft:stone", 7),
			"//DIRT",
			toRotateStr("minecraft:dirt", 3),
			"//PLANKS",
			toRotateStr("minecraft:planks", 6),
			"//SAPLING",
			toRotateStr("minecraft:sapling", 6),
			"//LOG + LOG2",
			toRotateStr("minecraft:log", 4) + ',' + toRotateStr("minecraft:log2", 2),
			"//LEAVES + LEAVES2",
			toRotateStr("minecraft:leaves", 4) + ',' + toRotateStr("minecraft:leaves2", 2),
			"//SPONGE",
			toRotateStr("minecraft:sponge", 2),
			"//SANDSTONE",
			toRotateStr("minecraft:sandstone", 3),
			"//WOOL",
			toRotateStr("minecraft:wool", 16),
			"//RED_FLOWER",
			toRotateStr("minecraft:red_flower", 9),
			"//DOUBLE_STONE_SLAB",
			toRotateStr("minecraft:double_stone_slab", 10),
			"//STONE_SLAB",
			toRotateStr("minecraft:stone_slab", 8),
			"//STAINED_GLASS",
			toRotateStr("minecraft:stained_glass", 16),
			"//MONSTER_EGG",
			toRotateStr("minecraft:monster_egg", 6),
			"//STONEBRICK",
			toRotateStr("minecraft:stonebrick", 4),
			"//DOUBLE_WOODEN_SLAB",
			toRotateStr("minecraft:double_wooden_slab", 6),
			"//WOODEN_SLAB",
			toRotateStr("minecraft:wooden_slab", 6),
			"//COBBLESTONE_WALL",
			toRotateStr("minecraft:cobblestone_wall", 2),
			"//QUARTZ_BLOCK",
			toRotateStr("minecraft:quartz_block", 3),
			"//STAINED_HARDENED_CLAY",
			toRotateStr("minecraft:stained_hardened_clay", 16),
			"//STAINED_GLASS_PANE",
			toRotateStr("minecraft:stained_glass_pane", 16),
			"//PRISMARINE",
			toRotateStr("minecraft:prismarine", 3),
			"//CARPET",
			toRotateStr("minecraft:carpet", 16),
			"//DOUBLE_PLANT",
			toRotateStr("minecraft:DOUBLE_PLANT", 6),
			"//RED_SANDSTONE",
			toRotateStr("minecraft:red_sandstone", 3),
			"//CONCRETE",
			toRotateStr("minecraft:concrete", 16),
			"//CONCRETE_POWDER",
			toRotateStr("minecraft:concrete_powder", 16),
		};
	}
	
	@SuppressWarnings("deprecation")
	public static boolean canRotate(ItemStack itemStack) {
		if (itemStack == null || itemStack.isEmpty()) {
			return false;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == Blocks.AIR) {
			return false;
		}
		int meta = itemStack.getItemDamage();
		return BLOCK_CHAIN.containsKey(block.getStateFromMeta(meta));
	}
	
	@SuppressWarnings("deprecation")
	public static List<ItemStack> getAllRotateResult(ItemStack itemStack){
		List<ItemStack> stacks = new ArrayList<>();
		if (itemStack == null || itemStack.isEmpty()) {
			return null;
		}
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == Blocks.AIR) {
			return null;
		}
		int meta = itemStack.getItemDamage();
		IBlockState srcState = block.getStateFromMeta(meta);
		if (!BLOCK_CHAIN.containsKey(srcState)) {
			return null;
		}
		stacks.add(itemStack);
		IBlockState state = BLOCK_CHAIN.get(srcState);
		int counter = 0;
		while (state != srcState) {
			stacks.add(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)));
			state = BLOCK_CHAIN.get(state);
			counter++;
			if (counter >= 50000) {
				logger.error("infinite loop");
				return null;
			}
		}
		return stacks;
	}
	
	@SuppressWarnings("deprecation")
	private static void loadBlockGroups() {
		BLOCK_CHAIN.clear();
		for (String line: CONFIG.BLOCK_GROUPS) {
			if (line.startsWith("//")) {
				continue;
			}
			List<IBlockState> states = new ArrayList<>();
			for (String part: line.split(",")) {
				try {
					String[] name_meta = part.split("/");
					Block block = Block.getBlockFromName(name_meta[0]);
					int meta = Integer.parseInt(name_meta[1]);
					if (block == null || block == Blocks.AIR) {
						logger.error(String.format("Not found: '%s'", part));
						continue;
					}
					IBlockState state = block.getStateFromMeta(meta);
					states.add(state);
				} catch (Exception e) {
					logger.error(String.format("Failed to load: '%s'", part));
				}
			}
			if (states.size() <= 1) {
				logger.error("Failed to load group: '%s'", line);
				continue;
			}
			for (int i=0;i<states.size();i++) {
				if (BLOCK_CHAIN.containsKey(states.get(i))) {
					logger.error("BLOCK_GROUPS value is invalid.");
					BLOCK_CHAIN.clear();
					return;
				}
				BLOCK_CHAIN.put(states.get(i), states.get((i+1)%states.size()));
			}
		}
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		if (event.getSide() == Side.CLIENT) {
			LotTweaksClient.init();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		loadBlockGroups();
		ReplacePacketHandler.init();
	}

}
