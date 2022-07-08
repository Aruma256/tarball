package com.github.aruma256.lottweaks.client.keys;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

class LTKeyBaseTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	final void test_onClientTick() {
		DummyLTKeyBase key = new DummyLTKeyBase("", 0, "");
		ClientTickEvent event = new ClientTickEvent(Phase.END);
		event.setPhase(EventPriority.NORMAL);
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
		public boolean isKeyDown() {
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
