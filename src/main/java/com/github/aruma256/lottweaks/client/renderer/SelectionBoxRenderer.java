package com.github.aruma256.lottweaks.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

public class SelectionBoxRenderer {

	private static final double growFactor = 0.0020000000949949026D / 2;
	private static final VoxelShape CUBE = VoxelShapes.box(-growFactor, -growFactor, -growFactor, 1+growFactor, 1+growFactor, 1+growFactor);
	
	public static boolean render(ActiveRenderInfo activeRenderInfo, MatrixStack matrixStack, IVertexBuilder buffer, BlockPos blockPos, float partialTicks, float r, float g, float b) {
		if (!Minecraft.getInstance().level.getWorldBorder().isWithinBounds(blockPos)) {
			return false;
		}

		Vector3d vector3d = activeRenderInfo.getPosition();
		double d0 = vector3d.x();
		double d1 = vector3d.y();
		double d2 = vector3d.z();

		drawSelectionBox(matrixStack, buffer, activeRenderInfo.getEntity(), d0, d1, d2, blockPos);

		return true;
	}

	//from WorldRenderer.class
	private static void drawSelectionBox(MatrixStack matrixStackIn, IVertexBuilder bufferIn, Entity entityIn, double xIn, double yIn, double zIn, BlockPos blockPosIn) {
		drawShape(matrixStackIn, bufferIn, CUBE, (double)blockPosIn.getX() - xIn, (double)blockPosIn.getY() - yIn, (double)blockPosIn.getZ() - zIn, 1.0F, 0.0F, 0.0F, 0.4F);
	}

	//from WorldRenderer.class
	private static void drawShape(MatrixStack matrixStackIn, IVertexBuilder bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixStackIn.last().pose();
		shapeIn.forAllEdges((p_230013_12_, p_230013_14_, p_230013_16_, p_230013_18_, p_230013_20_, p_230013_22_) -> {
			bufferIn.vertex(matrix4f, (float)(p_230013_12_ + xIn), (float)(p_230013_14_ + yIn), (float)(p_230013_16_ + zIn)).color(red, green, blue, alpha).endVertex();
			bufferIn.vertex(matrix4f, (float)(p_230013_18_ + xIn), (float)(p_230013_20_ + yIn), (float)(p_230013_22_ + zIn)).color(red, green, blue, alpha).endVertex();
		});
	}

}
