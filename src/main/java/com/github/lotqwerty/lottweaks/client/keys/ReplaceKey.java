package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ReplaceKey extends AbstractLTKey {

	public ReplaceKey(int keyCode, String category) {
		super("Replace", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
	}

	@Override
	protected void onKeyReleased() {
	}

	@SubscribeEvent
	public void onRenderTick(final RenderTickEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) {
			return;
		}
		if (this.pressTime==1 || this.pressTime > LotTweaks.CONFIG.REPLACE_INTERVAL) {
			this.execReplace();
			if (this.pressTime==1) {
				this.pressTime++;
			}
		}
	}

	private void execReplace() {
		Minecraft mc = Minecraft.getInstance();
		if (!mc.player.isCreative()) {
			return;
		}
		RayTraceResult target = mc.getRenderViewEntity().pick(LotTweaks.CONFIG.REPLACE_RANGE, mc.getRenderPartialTicks(), false);
		if (target == null || target.getType() != RayTraceResult.Type.BLOCK){
        	return;
        }
		BlockPos pos = ((BlockRayTraceResult)target).getPos();
        BlockState state = mc.world.getBlockState(pos);
        if (state.getBlock().isAir(state, mc.world, pos))
        {
            return;
        }
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (itemStack.isEmpty() || block == Blocks.AIR) {
			return;
		}
		BlockState newBlockState = block.getStateForPlacement(new BlockItemUseContext(mc.player, Hand.MAIN_HAND, itemStack, (BlockRayTraceResult)target));
		LTPacketHandler.sendReplaceMessage(pos, newBlockState, state);
	}

}