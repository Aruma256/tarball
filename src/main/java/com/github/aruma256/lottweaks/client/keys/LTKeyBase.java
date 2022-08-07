package com.github.aruma256.lottweaks.client.keys;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class LTKeyBase extends KeyBinding {

	protected int pressTime = 0;
	private int mode = 0;
	private int modeSwitchTick = 0;
	private static final int MODE_SWITCH_TICKS_THRESHOLD = 5;
	
	public LTKeyBase(String description, int keyCode, String category) {
		super(description, keyCode, category);
	}

	protected int getMode() {
		return this.mode - 1;
	}

	@SubscribeEvent
	public void onKeyInput(final KeyInputEvent event) {
		//Mark this key as handled.
	}

	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.getPhase() == EventPriority.NORMAL) {
			if (this.isDown()) {
				this.pressTime = Math.min(12345, this.pressTime + 1);
				if (this.pressTime == 1) {
					this.mode++;
					this.onKeyPressStart();
					this.modeSwitchTick = MODE_SWITCH_TICKS_THRESHOLD;
				}
				whilePressed();
			} else {
				if (this.pressTime > 0) {
					this.onKeyReleased();
					this.pressTime = 0;
				}
				if (this.modeSwitchTick > 0) {
					this.modeSwitchTick--;
					if (this.modeSwitchTick == 0) {
						this.mode = 0;
					}
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

	protected boolean isPlayerCreative() {
		return getClientPlayer().isCreative();
	}

}