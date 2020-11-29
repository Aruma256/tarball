package com.github.lotqwerty.lottweaks.client.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.network.LTPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ReplaceKey extends LTKeyBase {

	public ReplaceKey(int keyCode, String category) {
		super("Replace", keyCode, category);
	}

	@SubscribeEvent
	public void onRenderTick(final RenderTickEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) {
			return;
		}
		if (this.pressTime==1 || this.pressTime > LotTweaks.CONFIG.REPLACE_INTERVAL.get()) {
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
		RayTraceResult target = mc.objectMouseOver;
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
		BlockState newBlockState = block.getStateForPlacement(new DummyBlockItemUseContext(mc.world, mc.player, Hand.MAIN_HAND, itemStack, (BlockRayTraceResult)target));
		LTPacketHandler.sendReplaceMessage(pos, newBlockState, state);
	}
	
	protected static class DummyBlockItemUseContext extends BlockItemUseContext {
		protected DummyBlockItemUseContext(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stackIn,
				BlockRayTraceResult rayTraceResultIn) {
			super(worldIn, playerIn, handIn, stackIn, rayTraceResultIn);
			// TODO Auto-generated constructor stub
		}
	}

}