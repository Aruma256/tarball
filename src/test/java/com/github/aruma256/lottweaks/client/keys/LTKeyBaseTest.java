package com.github.aruma256.lottweaks.client.keys;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;

class LTKeyBaseTest extends MinecraftTestBase {

	@Test
	final void test_onClientTick() {
		TickEvent.ClientTickEvent event = new TickEvent.ClientTickEvent(Phase.END);
		event.setPhase(EventPriority.NORMAL);
		DummyLTKeyBase key;
		key = new DummyLTKeyBase("", 0, "");
		// if the key is not pressed, pressTime should be 0
		key.isKeyDown = false;
		for (int i=0; i<4; i++) key.onClientTick(event);
		assertEquals(0, key.pressTime);
		assertEquals(0, key.onKeyPressStart);
		assertEquals(0, key.whilePressed);
		assertEquals(0, key.onKeyReleased);
		// if the key is pressed, pressTime should be incremented
		key.isKeyDown = true;
		key.onClientTick(event);
		assertEquals(1, key.pressTime);
		assertEquals(1, key.onKeyPressStart);
		assertEquals(1, key.whilePressed);
		assertEquals(0, key.onKeyReleased);
		for (int i=0; i<3; i++) key.onClientTick(event);
		assertEquals(4, key.pressTime);
		assertEquals(1, key.onKeyPressStart);
		assertEquals(4, key.whilePressed);
		assertEquals(0, key.onKeyReleased);
		// if the key is released, pressTime should be 0
		key.isKeyDown = false;
		key.onClientTick(event);
		assertEquals(0, key.pressTime);
		assertEquals(1, key.onKeyPressStart);
		assertEquals(4, key.whilePressed);
		assertEquals(1, key.onKeyReleased);

		//////////////
		// test "mode" filed
		//////////////

		key = new DummyLTKeyBase("", 0, "");
		// default mode is 0
		key.isKeyDown = true;
		key.onClientTick(event);
		assertEquals(0, key.getMode());
		// mode is incremented if the key is pressed again within 4 ticks
		key.isKeyDown = false;
		for (int i=0; i<4; i++) key.onClientTick(event);
		key.isKeyDown = true;
		key.onClientTick(event);
		assertEquals(1, key.getMode());
		// mode is incremented if the key is pressed again within 4 ticks
		key.isKeyDown = false;
		for (int i=0; i<4; i++) key.onClientTick(event);
		key.isKeyDown = true;
		key.onClientTick(event);
		assertEquals(2, key.getMode());
		// mode is reset to 0 if the key is pressed again after 5 ticks
		key.isKeyDown = false;
		for (int i=0; i<5; i++) key.onClientTick(event);
		key.isKeyDown = true;
		key.onClientTick(event);
		assertEquals(0, key.getMode());
	}

	private static class DummyLTKeyBase extends LTKeyBase {

		public boolean isKeyDown = false;

		public int onKeyPressStart = 0;
		public int whilePressed = 0;
		public int onKeyReleased = 0;

		public DummyLTKeyBase(String description, int keyCode, String category) {
			super(description, keyCode, category);
		}

		@Override
		public boolean isDown() {
			return isKeyDown;
		}

		@Override
		protected void onKeyPressStart() {
			onKeyPressStart++;
		}

		@Override
		protected void whilePressed() {
			whilePressed++;
		}

		@Override
		protected void onKeyReleased() {
			onKeyReleased++;
		}

	}

}
