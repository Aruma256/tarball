package com.github.lotqwerty.lottools;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.github.lotqwerty.lottools.keys.ExPickKey;
import com.github.lotqwerty.lottools.keys.RotateMetaKey;

@Mod(modid = LotTools.MODID, name = LotTools.NAME, version = LotTools.VERSION)
public class LotTools
{
	
	// World.setBlockState(BlockPos,  Block.getStateFromMeta)
	
    public static final String MODID = "lottools";
    public static final String NAME = "Lot Tools";
    public static final String VERSION = "1.0.2";
    public static Logger logger;
    public static final HashMap<Block, Integer> META_RANGE = new HashMap<>();
    static {
    	META_RANGE.put(Blocks.STONE, 7);
    	META_RANGE.put(Blocks.DIRT, 3);
    	META_RANGE.put(Blocks.PLANKS, 6);
    	META_RANGE.put(Blocks.SAPLING, 6);
    	META_RANGE.put(Blocks.LOG, 4);
    	META_RANGE.put(Blocks.LEAVES, 4);
    	META_RANGE.put(Blocks.SPONGE, 2);
    	META_RANGE.put(Blocks.SANDSTONE, 3);
    	META_RANGE.put(Blocks.WOOL, 16);
    	META_RANGE.put(Blocks.RED_FLOWER, 9);
    	META_RANGE.put(Blocks.DOUBLE_STONE_SLAB, 10);
    	META_RANGE.put(Blocks.STONE_SLAB, 8);
    	META_RANGE.put(Blocks.STAINED_GLASS, 16);
    	META_RANGE.put(Blocks.MONSTER_EGG, 6);
    	META_RANGE.put(Blocks.STONEBRICK, 4);
    	META_RANGE.put(Blocks.DOUBLE_WOODEN_SLAB, 6);
    	META_RANGE.put(Blocks.WOODEN_SLAB, 6);
    	META_RANGE.put(Blocks.COBBLESTONE_WALL, 2);
    	META_RANGE.put(Blocks.QUARTZ_BLOCK, 3);
    	META_RANGE.put(Blocks.STAINED_HARDENED_CLAY, 16);
    	META_RANGE.put(Blocks.STAINED_GLASS_PANE, 16);
    	META_RANGE.put(Blocks.LEAVES2, 2);
    	META_RANGE.put(Blocks.LOG2, 2);
    	META_RANGE.put(Blocks.PRISMARINE, 3);
    	META_RANGE.put(Blocks.CARPET, 16);
    	META_RANGE.put(Blocks.DOUBLE_PLANT, 6);
    	META_RANGE.put(Blocks.RED_SANDSTONE, 3);
    	META_RANGE.put(Blocks.CONCRETE, 16);
    	META_RANGE.put(Blocks.CONCRETE_POWDER, 16);
    }

    public static boolean isAllowedBlock(Block block) {
    	return META_RANGE.containsKey(block);
    }

    public static boolean isAllowedBlock(ItemStack itemStack) {
    	return isAllowedBlock(Block.getBlockFromItem(itemStack.getItem()));
    }

    public static int getRange(Block block) {
    	return META_RANGE.get(block);
    }

    public static int getRange(ItemStack itemStack) {
    	return getRange(Block.getBlockFromItem(itemStack.getItem()));
    }

    public static void rotateMeta(ItemStack itemStack, int shift) {
    	int meta_range = getRange(itemStack);
    	int next_meta = (itemStack.getItemDamage() + shift) % meta_range;
    	if (next_meta < 0) {
    		next_meta += meta_range;
    	}
    	itemStack.setItemDamage(next_meta);    	
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        if (event.getSide() == Side.CLIENT) {
        	KeyBinding key;
        	key = new ExPickKey(Keyboard.KEY_F, NAME);
			MinecraftForge.EVENT_BUS.register(key);
			ClientRegistry.registerKeyBinding(key);
			key = new RotateMetaKey(Keyboard.KEY_R, NAME);
			MinecraftForge.EVENT_BUS.register(key);
			ClientRegistry.registerKeyBinding(key);
        }
	}

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }


}
