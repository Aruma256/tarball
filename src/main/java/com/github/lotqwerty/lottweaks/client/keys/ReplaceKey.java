package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent;
import com.github.lotqwerty.lottweaks.fabric.RenderHotbarEvent.RenderHotbarListener;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class ReplaceKey extends LTKeyBase implements RenderHotbarListener {

	public ReplaceKey(int keyCode, String category) {
		super("Replace", keyCode, category);
	}

	@Override
	public void onRenderHotbar(RenderHotbarEvent event) {
//		if (event.getPhase() != EventPriority.NORMAL) {
//			return;
//		}
		if (this.pressTime==1 || this.pressTime > LotTweaks.CONFIG.REPLACE_INTERVAL) {
			this.execReplace();
			if (this.pressTime==1) {
				this.pressTime++;
			}
		}
	}

	private void execReplace() {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (!mc.player.isCreative()) {
			return;
		}
		HitResult target = mc.crosshairTarget;
		if (target == null || target.getType() != HitResult.Type.BLOCK){
        	return;
        }
		BlockPos pos = ((BlockHitResult)target).getBlockPos();
        BlockState state = mc.world.getBlockState(pos);
        if (state.getBlock() == Blocks.AIR)
        {
            return;
        }
		ItemStack itemStack = mc.player.getInventory().getMainHandStack();
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (itemStack.isEmpty() || block == Blocks.AIR) {
			return;
		}
		BlockState newBlockState = block.getPlacementState(new ItemPlacementContext(mc.player, Hand.MAIN_HAND, itemStack, (BlockHitResult)target));
		LTPacketHandler.sendReplaceMessage(pos, newBlockState, state);
	}

}