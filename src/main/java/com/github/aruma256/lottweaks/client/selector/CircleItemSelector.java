package com.github.aruma256.lottweaks.client.selector;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class CircleItemSelector extends AbstractItemSelector {

    private static final double CENTER_CIRCLE_SCALE = 0.25;
    
    private double mouseDx = 0;
    private double mouseDy = 0;

    private int selectedId = 0;

	public CircleItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	private int getSelectedId() {
		if (mouseDx == 0 && mouseDy == 0) {
			return 0;
		}
		double theta = -2*Math.PI - Math.atan2(mouseDy, mouseDx);
		theta -= 0.5*Math.PI; //base-offset
		theta += 2*Math.PI / stacks.size() / 2; //half-section-offset
		while (theta < 0) {
			theta += 2*Math.PI;
		}
		int id = (int) (theta / (2*Math.PI) * stacks.size());
		return id;
	}

	private ItemStack getSelectedItemStack() {
		int id = getSelectedId();
		return this.stacks.get(id);
	}

	@Override
	protected void replaceInventory() {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack itemStack = this.getSelectedItemStack();
		mc.player.inventory.setInventorySlotContents(this.slot, itemStack);
        mc.playerController.sendSlotPacket(itemStack, 36 + this.slot);
	}

	private void normalizeMouseDxDy() {
		double r = Math.sqrt(mouseDx*mouseDx + mouseDy*mouseDy);
		if (r > 40) {
			mouseDx *= 40 / r;
			mouseDy *= 40 / r;
		}
	}

	public void notifyMouseMovement(int dx, int dy) {
		mouseDx += dx;
		mouseDy += dy;
		normalizeMouseDxDy();
		int newSelectedId = getSelectedId();
		if (this.selectedId != newSelectedId) {
			this.selectedId = newSelectedId;
			this.replaceInventory();
		}
	}

	private double convertIndexToAngle(int i) {
		return (2*Math.PI) * (((double)-i) / stacks.size()) - Math.PI/2;
	}

	@Override
	public void render(ScaledResolution sr) {
		int time = 500000;
		float partialTick = Minecraft.getMinecraft().getRenderPartialTicks();

		int cx = this.getCenterX(sr.getScaledWidth());
		int cy = sr.getScaledHeight() - 16 - 3 - 50;
		cy -= 50 + (20 + stacks.size());

		double maxRadius = 20 + stacks.size() * 1.2;
		double radius = maxRadius * Math.tanh((time + partialTick) / 3);

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.translate(8F, 8F, 8F);

        GlStateManager.color(1, 1, 1, 1);
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(cx, cy, 0).endVertex();
        for(int i=0; i<=256; i++) {
            double angle = -2*Math.PI * ((double)i)/256;
            bufferbuilder.pos(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(angle), cy + CENTER_CIRCLE_SCALE*radius*Math.sin(angle), 0).endVertex();
        }
        tessellator.draw();

        GlStateManager.color(1, 1, 1, 0.6f);
        bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        double pointedAngle = Math.atan2(mouseDy, mouseDx);
        bufferbuilder.pos(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(pointedAngle+Math.PI/2), cy - CENTER_CIRCLE_SCALE*radius*Math.sin(pointedAngle+Math.PI/2), 0).endVertex();
        bufferbuilder.pos(cx + CENTER_CIRCLE_SCALE*radius*Math.cos(pointedAngle-Math.PI/2), cy - CENTER_CIRCLE_SCALE*radius*Math.sin(pointedAngle-Math.PI/2), 0).endVertex();
        bufferbuilder.pos(cx + radius*Math.cos(pointedAngle), cy - radius*Math.sin(pointedAngle), 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

		glItemRenderInitialize();
		int selectedId = getSelectedId();
		int i = 0;
		for (ItemStack c: stacks) {
			double theta = convertIndexToAngle(i);
			double dx = (i==selectedId ? radius*1.3 : radius) * Math.cos(theta);
			double dy = (i==selectedId ? radius*1.3 : radius) * Math.sin(theta);
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(c, (int)Math.round(cx + dx), (int)Math.round(cy - dy));
			i++;
		}
		glItemRenderFinalize();

	}

}
