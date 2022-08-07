package com.github.aruma256.lottweaks.client.keys;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.client.selector.CircleItemSelector;
import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;

import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;

class OpenPaletteKeyTest extends MinecraftTestBase {

	@Test @Disabled
	final void test_getMode() {
		fail("Not yet implemented"); // TODO
	}

	@Test @Disabled
	final void test_onKeyPressStart() {
		fail("Not yet implemented"); // TODO
	}

	@Test @Disabled
	final void test_onKeyReleased() {
		fail("Not yet implemented"); // TODO
	}

	@Test @Disabled
	final void test_openPaletteKey() {
		fail("Not yet implemented"); // TODO
	}

	@Test @Disabled
	final void test_onRenderTick() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void test_onMouseEvent() throws Exception {
		// set up
		OpenPaletteKey instance = spy(new OpenPaletteKey(0, null));
		CircleItemSelector selectorMock = mock(CircleItemSelector.class);
		Field selectorField = OpenPaletteKey.class.getDeclaredField("selector");
		selectorField.setAccessible(true);
		selectorField.set(instance, selectorMock);
		MouseScrollEvent eventMock = mock(MouseScrollEvent.class);
		when(eventMock.isCanceled()).thenReturn(false);
		// While this key is not pressed, the event is ignored.
		instance.pressTime = 0;
		doReturn(true).when(instance).isPlayerCreative();
		doReturn(120).when(eventMock).getScrollDelta();
		instance.onMouseEvent(eventMock);
		verify(selectorMock, never()).rotate(anyInt());
		verify(eventMock, never()).setCanceled(anyBoolean());
		// If the player is not in creative mode, events are ignored
		instance.pressTime = 1;
		doReturn(false).when(instance).isPlayerCreative();
		doReturn(120).when(eventMock).getScrollDelta();
		instance.onMouseEvent(eventMock);
		verify(selectorMock, never()).rotate(anyInt());
		verify(eventMock, never()).setCanceled(anyBoolean());
		// If Dwheel == 0, events are ignored
		instance.pressTime = 1;
		doReturn(true).when(instance).isPlayerCreative();
		doReturn(0).when(eventMock).getScrollDelta();
		instance.onMouseEvent(eventMock);
		verify(selectorMock, never()).rotate(anyInt());
		verify(eventMock, never()).setCanceled(anyBoolean());
		// If Dwheel != 0, rotate() is called with inverted value
		instance.pressTime = 1;
		doReturn(true).when(instance).isPlayerCreative();
		doReturn(120).when(eventMock).getScrollDelta();
		instance.onMouseEvent(eventMock);
		verify(selectorMock, times(1)).rotate(-120);
		verify(eventMock, times(1)).setCanceled(true);
	}

	@Test @Disabled
	final void test_onRenderOverlay() {
		fail("Not yet implemented"); // TODO
	}

}
