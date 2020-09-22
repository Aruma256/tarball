package com.github.lotqwerty.lotblocks;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
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

import com.github.lotqwerty.lotblocks.keys.RotateMetaKey;
import com.github.lotqwerty.lotblocks.keys.LongPickKey;

@Mod(modid = LotBlocks.MODID, name = LotBlocks.NAME, version = LotBlocks.VERSION)
public class LotBlocks
{
    public static final String MODID = "lotblocks";
    public static final String NAME = "Lot Blocks";
    public static final String VERSION = "1.0.1";
    
    public static final HashMap<Block, Integer> META_RANGE = new HashMap<>();
    static {
    	META_RANGE.put(Blocks.STONE, 7);
    	META_RANGE.put(Blocks.DIRT, 3);
    	META_RANGE.put(Blocks.PLANKS, 6);
    	META_RANGE.put(Blocks.SAPLING, 6);
    	META_RANGE.put(Blocks.LOG, 16);
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

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        if (event.getSide() == Side.CLIENT) {
        	KeyBinding key;
        	key = new LongPickKey(Keyboard.KEY_F, NAME);
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
        // some example code
    }
}
