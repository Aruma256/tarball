package com.github.lotqwerty.lottweaks.client.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class LTKeyBase extends KeyBinding {

	protected int pressTime = 0;
	
	public LTKeyBase(String description, int keyCode, String category) {
		super(description, keyCode, category);
	}

	@SubscribeEvent
	public void onKeyInput(final KeyInputEvent event) {
		//キー押下をハンドル済みにする
	}

	@SubscribeEvent
	public void onClientTick(final ClientTickEvent event) {
		if (event.getPhase() == EventPriority.NORMAL) {
			if (this.isKeyDown()) {
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
		}
	}

	protected void onKeyPressStart() {
	}
	
	protected void whilePressed() {
	}

	protected void onKeyReleased() {
	}

}