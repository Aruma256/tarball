package com.github.lotqwerty.lottweaks.fabric;

import java.util.ArrayList;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;

public class RenderHotbarEvent {
	private static final ArrayList<RenderHotbarListener> listeners = new ArrayList<>();

	public static void registerListener(RenderHotbarListener listener) {
		listeners.add(listener);
	}

	public static void post(PoseStack matrixStack, float tickDelta) {
		RenderHotbarEvent event = new RenderHotbarEvent(matrixStack, tickDelta);
		for (RenderHotbarListener iListener : listeners) {
			iListener.onRenderHotbar(event);
		}
	}

	private final PoseStack matrixStack;
	private final float tickDelta;

	private RenderHotbarEvent(PoseStack matrixStack, float tickDelta) {
		this.matrixStack = matrixStack;
		this.tickDelta = tickDelta;
	}
	
	public PoseStack getMatrixStack() {
		return this.matrixStack;
	}

	public float getPartialTicks() {
		return this.tickDelta;
	}
	
	public Window getWindow() {
		return Minecraft.getInstance().getWindow();
	}

	public static interface RenderHotbarListener {
		public void onRenderHotbar(RenderHotbarEvent event);
	}

}
