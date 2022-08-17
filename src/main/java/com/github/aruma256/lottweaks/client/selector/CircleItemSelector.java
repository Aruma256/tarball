package com.github.aruma256.lottweaks.client.selector;

import static com.github.aruma256.lottweaks.client.ClientUtil.*;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class CircleItemSelector extends AbstractItemSelector {

    private static final double CENTER_CIRCLE_SCALE = 0.25;

    private Angle angle = new Angle(-0.5*Math.PI);
    private double mouseDx = 0;
    private double mouseDy = 0;

    private int selectedId = 0;

	public CircleItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	private int getSelectedId() {
		Angle theta = new Angle(-2*Math.PI - angle.value());
		theta.add(-0.5*Math.PI); //base-offset
		theta.add(2*Math.PI / stacks.size() / 2); //half-section-offset
		int id = (int) (theta.value() / (2*Math.PI) * stacks.size());
		return id;
	}

	private ItemStack getSelectedItemStack() {
		int id = getSelectedId();
		return this.stacks.get(id);
	}

	@Override
	public void rotate(int direction) {
		overwriteSelectedIndex((selectedId + Integer.signum(direction)) % stacks.size());
	}

	public void overwriteSelectedIndex(int index) {
		angle.set(convertIndexToAngle(index));
		mouseDx = 0;
		mouseDy = 0;
		updateSelectedItem();
	}

	@Override
	protected void replaceInventory() {
		ItemStack itemStack = this.getSelectedItemStack();
		getClientPlayer().inventory.setItem(this.slot, itemStack);
		getClient().gameMode.handleCreativeModeItemAdd(itemStack, 36 + this.slot);
	}

	private void normalizeMouseDxDy() {
		double r = Math.sqrt(mouseDx*mouseDx + mouseDy*mouseDy);
		if (r > 40) {
			mouseDx *= 40 / r;
			mouseDy *= 40 / r;
		}
	}

	private boolean updateSelectedItem() {
		int newSelectedId = getSelectedId();
		if (this.selectedId != newSelectedId) {
			this.selectedId = newSelectedId;
			this.replaceInventory();
			return true;
		}
		return false;
	}

	public void notifyMouseMovement(double dx, double dy) {
		if (dx == 0 && dy == 0) return;
		mouseDx += dx;
		mouseDy += dy;
		normalizeMouseDxDy();
		angle.set(Math.atan2(mouseDy, mouseDx));
		updateSelectedItem();
	}

	private double convertIndexToAngle(int i) {
		return (2*Math.PI) * (((double)-i) / stacks.size()) - Math.PI/2;
	}

	@Override
	public void render(MainWindow sr) {
		PlayerInventory.getSelectionSize();
		int time = 500000;
		float partialTick = getClient().getDeltaFrameTime();

		int cx = this.getCenterX(sr.getGuiScaledWidth());
//		int cy = sr.getScaledHeight() - 16 - 3 - 50;
//		cy -= 50 + (20 + stacks.size());
		int cy = sr.getGuiScaledHeight() / 2;

		double maxRadius = 20 + stacks.size() * 1.2;
		double radius = maxRadius * Math.tanh((time + partialTick) / 3);

		// DebugOverlayGui#drawChart

		RenderSystem.pushTextureAttributes();
		RenderSystem.pushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.translatef(8F, 8F, 8F);

        RenderSystem.color4f(1, 1, 1, 1);
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        bufferbuilder.vertex(cx, cy, 0).endVertex();
        for(int i=0; i<=256; i++) {
            double angle = -2*Math.PI * ((double)i)/256;
            bufferbuilder.vertex(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(angle), cy + CENTER_CIRCLE_SCALE*radius*Math.sin(angle), 0).endVertex();
        }
        tessellator.end();

        RenderSystem.color4f(1, 1, 1, 0.6f);
        bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        double pointedAngle = angle.value();
        bufferbuilder.vertex(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(pointedAngle+Math.PI/2), cy - CENTER_CIRCLE_SCALE*radius*Math.sin(pointedAngle+Math.PI/2), 0).endVertex();
        bufferbuilder.vertex(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(pointedAngle-Math.PI/2), cy - CENTER_CIRCLE_SCALE*radius*Math.sin(pointedAngle-Math.PI/2), 0).endVertex();
        bufferbuilder.vertex(cx + radius*Math.cos(pointedAngle), cy - radius*Math.sin(pointedAngle), 0).endVertex();
        tessellator.end();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
		RenderSystem.popMatrix();
		RenderSystem.popAttributes();

		glItemRenderInitialize();
		int selectedId = getSelectedId();
		int i = 0;
		for (ItemStack c: stacks) {
			double theta = convertIndexToAngle(i);
			double dx = (i==selectedId ? radius*1.3 : radius) * Math.cos(theta);
			double dy = (i==selectedId ? radius*1.3 : radius) * Math.sin(theta);
			getClient().getItemRenderer().renderAndDecorateItem(c, (int)Math.round(cx + dx), (int)Math.round(cy - dy));
			i++;
		}
		glItemRenderFinalize();
	}

	public static class Angle {

		private double angle = 0;

		public Angle(double initialAngle) {
			add(initialAngle);
		}

		public void set(double angle) {
			this.angle = angle;
		}

		public void add(double theta) {
			angle += theta;
			normalize();
		}

		public double value() {
			return angle;
		}

		private void normalize() {
			while(angle < 0) {
				angle += 2*Math.PI;
			}
			while(angle >= 2*Math.PI) {
				angle -= 2*Math.PI;
			}
		}

	}

}
