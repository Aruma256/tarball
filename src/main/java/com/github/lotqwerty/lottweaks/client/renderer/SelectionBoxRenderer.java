package com.github.lotqwerty.lottweaks.client.renderer;

import com.github.lotqwerty.lottweaks.fabric.mixin.DrawShapeOutlineInvoker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class SelectionBoxRenderer {

	private static final double growFactor = 0.0020000000949949026D / 2;
	private static final VoxelShape CUBE = VoxelShapes.cuboid(-growFactor, -growFactor, -growFactor, 1+growFactor, 1+growFactor, 1+growFactor);

	public static boolean render(Camera activeRenderInfo, MatrixStack matrixStack, VertexConsumer buffer, BlockPos blockPos, float partialTicks, float r, float g, float b) {
		if (!MinecraftClient.getInstance().world.getWorldBorder().contains(blockPos)) {
			return false;
		}

		Vec3d vector3d = activeRenderInfo.getPos();
		double d0 = vector3d.x;
		double d1 = vector3d.y;
		double d2 = vector3d.z;

		drawSelectionBox(matrixStack, buffer, activeRenderInfo.getFocusedEntity(), d0, d1, d2, blockPos);

		return true;
	}

	//from WorldRenderer.class (drawBlockOutline)
	private static void drawSelectionBox(MatrixStack matrixStackIn, VertexConsumer bufferIn, Entity entityIn, double xIn, double yIn, double zIn, BlockPos blockPosIn) {
		DrawShapeOutlineInvoker.lottweaks_invoke_drawShapeOutline(matrixStackIn, bufferIn, CUBE, (double)blockPosIn.getX() - xIn, (double)blockPosIn.getY() - yIn, (double)blockPosIn.getZ() - zIn, 1.0F, 0.0F, 0.0F, 0.4F);
	}

}
