package com.github.lotqwerty.lottweaks.fabric;

import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class RenderHotbarEvent {
	private static final ArrayList<RenderHotbarListener> listeners = new ArrayList<>();

	public static void registerListener(RenderHotbarListener listener) {
		listeners.add(listener);
	}

	public static void post(MatrixStack matrixStack, float tickDelta) {
		RenderHotbarEvent event = new RenderHotbarEvent(matrixStack, tickDelta);
		for (RenderHotbarListener iListener : listeners) {
			iListener.onRenderHotbar(event);
		}
	}

	private final MatrixStack matrixStack;
	private final float tickDelta;

	private RenderHotbarEvent(MatrixStack matrixStack, float tickDelta) {
		this.matrixStack = matrixStack;
		this.tickDelta = tickDelta;
	}
	
	public MatrixStack getMatrixStack() {
		return this.matrixStack;
	}

	public float getPartialTicks() {
		return this.tickDelta;
	}
	
	public Window getWindow() {
		return MinecraftClient.getInstance().getWindow();
	}

	public static interface RenderHotbarListener {
		public void onRenderHotbar(RenderHotbarEvent event);
	}

}
