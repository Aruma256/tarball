package com.github.lotqwerty.lottweaks.fabric;

import java.util.ArrayList;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public class DrawBlockOutlineEvent {
	private static final ArrayList<DrawBlockOutlineListener> LISTENERS = new ArrayList<>();

	public static void registerListener(DrawBlockOutlineListener listener) {
		LISTENERS.add(listener);
	}

	public static boolean post(Camera activeRenderInfo, MatrixStack matrixStack, VertexConsumer buffer, BlockPos blockPos) {
		DrawBlockOutlineEvent event = new DrawBlockOutlineEvent(activeRenderInfo, matrixStack, buffer, blockPos);
		for (DrawBlockOutlineListener iListener : LISTENERS) {
			iListener.onDrawBlockHighlightEvent(event);
			if (event.isCanceled()) {
				return true;
			}
		}
		return false;
	}

	private boolean canceled = false;
	private final Camera activeRenderInfo;
	private final MatrixStack matrixStack;
	private final VertexConsumer buffer;
	private final BlockPos blockPos;

	private DrawBlockOutlineEvent(Camera activeRenderInfo, MatrixStack matrixStack, VertexConsumer buffer, BlockPos blockPos) {
		this.activeRenderInfo = activeRenderInfo;
		this.matrixStack = matrixStack;
		this.buffer = buffer;
		this.blockPos = blockPos;
	}

	public Camera getInfo() {
		return this.activeRenderInfo;
	}

	public MatrixStack getMatrix() {
		return this.matrixStack;
	}

	public VertexConsumer getBuffers() {
		return this.buffer;
	}

	public BlockPos getPos() {
		return this.blockPos;
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setCanceled(boolean value) {
		this.canceled = value;
	}

	public static interface DrawBlockOutlineListener {
		public void onDrawBlockHighlightEvent(DrawBlockOutlineEvent event);
	}

}
