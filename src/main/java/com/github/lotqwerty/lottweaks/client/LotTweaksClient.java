package com.github.lotqwerty.lottweaks.client;

import org.lwjgl.glfw.GLFW;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.client.keys.AdjustRangeKey;
import com.github.lotqwerty.lottweaks.client.keys.ExPickKey;
import com.github.lotqwerty.lottweaks.client.keys.ReplaceKey;
import com.github.lotqwerty.lottweaks.client.keys.RotateKey;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent;
import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent.ClientChatEventListener;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent.RenderHotbarListener;
import com.github.lotqwerty.lottweaks.fabric.ScrollEvent.ScrollListener;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

@Environment(EnvType.CLIENT)
public class LotTweaksClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient() {
		RotationHelper.loadAllFromFile();
		RotationHelper.loadAllItemGroupFromStrArray();
		//
    	KeyBinding key;
		key = new ExPickKey(GLFW.GLFW_KEY_V, LotTweaks.NAME);
		registerToMyEventBus(key);
		KeyBindingHelper.registerKeyBinding(key);
		key = new RotateKey(GLFW.GLFW_KEY_R, LotTweaks.NAME);
		registerToMyEventBus(key);
		KeyBindingHelper.registerKeyBinding(key);
		key = new ReplaceKey(GLFW.GLFW_KEY_G, LotTweaks.NAME);
		registerToMyEventBus(key);
		KeyBindingHelper.registerKeyBinding(key);
		key = new AdjustRangeKey(GLFW.GLFW_KEY_U, LotTweaks.NAME);
		registerToMyEventBus(key);
		KeyBindingHelper.registerKeyBinding(key);
		//
//		MinecraftForge.EVENT_BUS.register(new ConfigChangeHandler());
		//
		registerToMyEventBus(new LotTweaksCommand());
	}

	private static void registerToMyEventBus(Object obj) {
		if (obj instanceof ClientTickEvents.EndTick) {
			ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)obj);			
		}
		if (obj instanceof RenderHotbarListener) {
			RenderHotbarEvent.registerListener((RenderHotbarListener)obj);
		}
		if (obj instanceof ScrollListener) {
			ScrollEvent.registerListener((ScrollListener)obj);
		}
		if (obj instanceof ClientChatEventListener) {
			ClientChatEvent.registerListener((ClientChatEventListener)obj);
		}
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
