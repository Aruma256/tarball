package com.github.lotqwerty.lottweaks.client.renderer;

import java.util.Collection;
import java.util.Deque;

import org.lwjgl.opengl.GL11;

import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class LTRenderer {

	/*
	private static double getTheta(int i, int size, double afterimage, byte direction, double shift) {
		return -((double)i + shift - afterimage*direction) / size * 2 * Math.PI + Math.PI / 2;
	}

	private static double getTheta(int i, int size, double afterimage, byte direction) {
		return getTheta(i, size, afterimage, direction, 0d);
	}

	public static void renderItemStackCircle(ScaledResolution sr, Collection<ItemStack> stacks, int slot, int t, float pt, int lt, byte direction, int mdx, int mdy, int selected) {
		int x = sr.getScaledWidth() / 2 - 90 + slot * 20 + 2;
		int y = sr.getScaledHeight() - 16 - 3;
		y -= 50 + (20 + stacks.size());
		if (LotTweaks.CONFIG.DISABLE_ANIMATIONS) {
			t = Integer.MAX_VALUE;
			pt = 0;
		}
		double max_r = 20 + stacks.size() * 1.2;
		double r = max_r * Math.tanh((t + pt) / 3);
		double afterimage = 1 - Math.tanh((t + pt - lt)/1.5);
		//
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1, 1, 1, 0.25f);
        GlStateManager.translate(8F, 8F, 8F);
        final double R_SCALE_MIN = 0.5;
        final double R_SCALE_MAX = 1.5;
        //
        bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        for(int i=0; i<=stacks.size(); i++) {
            double theta = getTheta(i, stacks.size(), afterimage, direction, -0.5);
            bufferbuilder.pos(x + R_SCALE_MIN*r*Math.cos(theta), y + R_SCALE_MIN*r*Math.sin(theta), 0).endVertex();
            bufferbuilder.pos(x + R_SCALE_MAX*r*Math.cos(theta), y + R_SCALE_MAX*r*Math.sin(theta), 0).endVertex();
        }
        tessellator.draw();
        //
        GlStateManager.color(1, 1, 1, 0.5f);
        bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        for(int i=0; i<=1; i++) {
            double theta = getTheta((selected+i) % stacks.size(), stacks.size(), afterimage, direction, -0.5);
            bufferbuilder.pos(x + R_SCALE_MIN*r*Math.cos(theta), y + R_SCALE_MIN*r*Math.sin(theta), 0).endVertex();
            bufferbuilder.pos(x + R_SCALE_MAX*r*Math.cos(theta), y + R_SCALE_MAX*r*Math.sin(theta), 0).endVertex();
        }
        tessellator.draw();
        //
        GlStateManager.color(1, 0, 1, 1f);
        bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION);
        for(int i=0; i<=stacks.size(); i++) {
            double angle = Math.atan2(mdy, mdx);
            double theta_a = getTheta(i, stacks.size(), afterimage, direction, -0.5);
            double theta_b = getTheta(i, stacks.size(), afterimage, direction, +0.5);
            if (theta_a < angle && angle < theta_b) {
                bufferbuilder.pos(x + R_SCALE_MIN*r*Math.cos(theta_a), y + R_SCALE_MIN*r*Math.sin(theta_a), 0).endVertex();
                bufferbuilder.pos(x + R_SCALE_MAX*r*Math.cos(theta_a), y + R_SCALE_MAX*r*Math.sin(theta_a), 0).endVertex();
                bufferbuilder.pos(x + R_SCALE_MIN*r*Math.cos(theta_b), y + R_SCALE_MIN*r*Math.sin(theta_b), 0).endVertex();
                bufferbuilder.pos(x + R_SCALE_MAX*r*Math.cos(theta_b), y + R_SCALE_MAX*r*Math.sin(theta_b), 0).endVertex();
            }
        }
        tessellator.draw();
        //
        GlStateManager.color(0, 0, 0, 1f);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        for(int i=0; i<stacks.size(); i++) {
            double theta = getTheta(i, stacks.size(), afterimage, direction, -0.5);
            bufferbuilder.pos(x, y, 0).endVertex();
            bufferbuilder.pos(x + R_SCALE_MAX*r*Math.cos(theta), y + R_SCALE_MAX*r*Math.sin(theta), 0).endVertex();
        }
        tessellator.draw();
        //
        GlStateManager.color(1, 0, 0, 1f);
        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y, 0).endVertex();
        bufferbuilder.pos(x + mdx, y + mdy, 0).endVertex();
        tessellator.draw();
        //
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
		//
		glItemRenderInitialize();
		int i = 0;
		for (ItemStack c: stacks) {
			double theta = getTheta(i, stacks.size(), afterimage, direction);
			double dx = r * Math.cos(theta);
			double dy = r * Math.sin(theta);
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(c, (int)Math.round(x + dx), (int)Math.round(y + dy));
			i++;
		}
		glItemRenderFinalize();
	}

	private static void linear(Collection<ItemStack> stacks, int x, int y, int t, float pt, int lt, byte direction) {
		int i = 0;
		int R = 16;
		double afterimage = 1 - Math.tanh((t + pt - lt)/1.5);
		for (ItemStack c: stacks) {
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(c, x, (int)Math.round(y - i*R + afterimage*direction*R));
			i++;
		}
	}

	private static void glItemRenderInitialize() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();
	}
	
	private static void glItemRenderFinalize() {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
	}


	*/
	public static void renderItemStacks(Deque<ItemStack> candidates, int x, int y, int pressTime, float partialTicks, int lastRotateTime, byte rotateDirection) {
	}
	
}
