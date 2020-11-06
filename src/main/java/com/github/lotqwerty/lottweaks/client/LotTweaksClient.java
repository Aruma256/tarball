package com.github.lotqwerty.lottweaks.client;

import org.lwjgl.input.Keyboard;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;
import com.github.lotqwerty.lottweaks.client.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.client.keys.RotateKey;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LotTweaksClient
{
	public static void init() {
    	KeyBinding key;
		key = new ExPickKey(Keyboard.KEY_V, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateKey(Keyboard.KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(Keyboard.KEY_G, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new AdjustRangeKey(Keyboard.KEY_U, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		//
		MinecraftForge.EVENT_BUS.register(new ConfigChangeHandler());
		//
		ClientCommandHandler.instance.registerCommand(new LotTweaksCommand());
	}

	private static class ConfigChangeHandler {
		@SubscribeEvent
		public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(LotTweaks.MODID)) {
				LotTweaks.onConfigUpdate();
			}
		}
		
	}
	

}
