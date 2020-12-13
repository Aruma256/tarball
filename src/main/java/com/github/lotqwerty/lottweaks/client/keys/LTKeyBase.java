package com.github.lotqwerty.lottweaks.client.keys;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;

@Environment(EnvType.CLIENT)
public class LTKeyBase extends KeyBinding implements ClientTickEvents.EndTick {

	protected int pressTime = 0;
	
	public LTKeyBase(String description, int keyCode, String category) {
		super(description, keyCode, category);
	}

//	@SubscribeEvent
//	public void onKeyInput(final KeyInputEvent event) {
//		//
//	}

	@Override
	public void onEndTick(final MinecraftClient client) {
//		if (event.getPhase() == EventPriority.NORMAL) {
			if (this.isPressed()) {
				this.pressTime = Math.min(12345, this.pressTime + 1);
				if (this.pressTime == 1) {
					this.onKeyPressStart();
				}
				whilePressed();
			} else {
				if (this.pressTime > 0) {
					this.onKeyReleased();
					this.pressTime = 0;
				}
			}
//		}
	}

	protected void onKeyPressStart() {
	}
	
	protected void whilePressed() {
	}

	protected void onKeyReleased() {
	}

}