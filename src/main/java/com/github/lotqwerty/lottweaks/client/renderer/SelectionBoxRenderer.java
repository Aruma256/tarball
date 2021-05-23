package com.github.lotqwerty.lottweaks.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class SelectionBoxRenderer {

	public static boolean render(BlockPos blockPos, float partialTicks, float r, float g, float b) {
		if (!Minecraft.getMinecraft().world.getWorldBorder().contains(blockPos)) {
			return false;
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);

		EntityPlayer player = Minecraft.getMinecraft().player;
		double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
		double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
		double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;

		RenderGlobal.drawSelectionBoundingBox(Block.FULL_BLOCK_AABB.offset(blockPos).grow(0.0020000000949949026D).offset(-d3, -d4, -d5), r, g, b, 0.4F);

		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();

		return true;
	}

}
