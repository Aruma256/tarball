package com.github.lotqwerty.lottweaks.client.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.getPhase() == EventPriority.NORMAL) {
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