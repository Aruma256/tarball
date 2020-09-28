package com.github.lotqwerty.lottweaks;

import org.lwjgl.input.Keyboard;

import com.github.lotqwerty.lottweaks.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.keys.RotateMetaKey;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LotTweaksClient
{
	public static void init() {
    	KeyBinding key;
    	key = new ExPickKey(Keyboard.KEY_F, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateMetaKey(Keyboard.KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(Keyboard.KEY_C, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
	}
}
