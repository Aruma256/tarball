package com.github.lotqwerty.lottweaks.client.selector;

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

    private static final double R_SCALE_MIN = 0.5;
    private static final double R_SCALE_MAX = 1.5;
    
    private int mouseDx = 0;
    private int mouseDy = 0;

    private int selectedId = 0;

	public CircleItemSelector(List<ItemStack> stacks, int slot) {
		super(stacks, slot);
	}

	private int getSelectedId() {
		if (mouseDx == 0 && mouseDy == 0) {
			return 0;
		}
		double theta = 2*Math.PI - Math.atan2(mouseDy, mouseDx);
		theta += 0.5*Math.PI;
		theta += 2*Math.PI / (2*stacks.size());
		while (theta > 2*Math.PI) {
			theta -= 2*Math.PI;
		}
		double ratio = theta / (2*Math.PI);
		double zone = stacks.size() * ratio;
		int id = (((int)zone) + stacks.size()) % stacks.size();
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

	public void notifyMouseMovement(int dx, int dy) {
		double x = this.mouseDx + dx;
		double y = this.mouseDy - dy;
		double r = Math.sqrt(x*x + y*y);
		if (r > 40) {
			x /= r / 40;
			y /= r / 40;
		}
		this.mouseDx = (int) x;
		this.mouseDy = (int) y;
		//
		int newSelectedId = getSelectedId();
		if (this.selectedId != newSelectedId) {
			this.selectedId = newSelectedId;
			this.replaceInventory();
		}
	}

	private double getTheta(int i, double shift) {
		return -((double)i + shift) / stacks.size() * 2 * Math.PI + Math.PI / 2;
	}

	private double getTheta(int i) {
		return getTheta(i, 0d);
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

        GlStateManager.color(1, 1, 1, 0.25f);
        bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        for(int i=0; i<=stacks.size(); i++) {
            double theta = getTheta(i, 0.5);
            bufferbuilder.pos(cx + R_SCALE_MIN*radius*Math.cos(theta), cy + R_SCALE_MIN*radius*Math.sin(theta), 0).endVertex();
            bufferbuilder.pos(cx + R_SCALE_MAX*radius*Math.cos(theta), cy + R_SCALE_MAX*radius*Math.sin(theta), 0).endVertex();
        }
        tessellator.draw();

        GlStateManager.color(1, 0, 0, 1f);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(cx, cy, 0).endVertex();
        bufferbuilder.pos(cx + mouseDx, cy + mouseDy, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

		glItemRenderInitialize();
		int selectedId = getSelectedId();
		int i = 0;
		for (ItemStack c: stacks) {
			double theta = getTheta(i);
			double dx = (i==selectedId ? radius*1.3 : radius) * Math.cos(theta);
			double dy = (i==selectedId ? radius*1.3 : radius) * Math.sin(theta);
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(c, (int)Math.round(cx + dx), (int)Math.round(cy + dy));
			i++;
		}
		glItemRenderFinalize();

	}

}
