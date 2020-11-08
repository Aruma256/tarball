package com.github.lotqwerty.lottweaks.client;

import org.lwjgl.glfw.GLFW;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;
import com.github.lotqwerty.lottweaks.client.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.client.keys.RotateKey;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class LotTweaksClient
{
	public static void init() {
    	KeyBinding key;
		key = new ExPickKey(GLFW.GLFW_KEY_V, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new RotateKey(GLFW.GLFW_KEY_R, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new ReplaceKey(GLFW.GLFW_KEY_G, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		key = new AdjustRangeKey(GLFW.GLFW_KEY_U, LotTweaks.NAME);
		MinecraftForge.EVENT_BUS.register(key);
		ClientRegistry.registerKeyBinding(key);
		//
//		MinecraftForge.EVENT_BUS.register(new ConfigChangeHandler());
		//
		MinecraftForge.EVENT_BUS.register(new LotTweaksCommand());
	}

//	private static class ConfigChangeHandler {
//		@SubscribeEvent
//		public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
//			if (event.getModID().equals(LotTweaks.MODID)) {
//				LotTweaks.onConfigUpdate();
//			}
//		}
//		
//	}
	

}
