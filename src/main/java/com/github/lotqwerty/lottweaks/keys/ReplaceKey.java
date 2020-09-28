package com.github.lotqwerty.lottweaks.keys;

import com.github.lotqwerty.lottweaks.LotTweaks;
import com.github.lotqwerty.lottweaks.ReplacePacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReplaceKey extends LTKeyBase {

	public ReplaceKey(int keyCode, String category) {
		super("Replace", keyCode, category);
	}

	@SubscribeEvent
	public void onRenderTick(final RenderTickEvent event) {
		if (event.getPhase() == EventPriority.NORMAL) {
			if (this.pressTime==1 || this.pressTime > LotTweaks.CONFIG.REPLACE_INTERVAL) {
				this.onKeyPress2();
				if (this.pressTime==1) {
					this.pressTime++;
				}
			}
		}
	}

	private void onKeyPress2() {
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.capabilities.isCreativeMode) {
			return;
		}
		RayTraceResult target = mc.getRenderViewEntity().rayTrace(LotTweaks.CONFIG.REPLACE_RANGE, mc.getRenderPartialTicks());
		if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK){
        	return;
        }
        IBlockState state = mc.world.getBlockState(target.getBlockPos());
        if (state.getBlock().isAir(state, mc.world, target.getBlockPos()))
        {
            return;
        }
		ItemStack itemStack = mc.player.inventory.getCurrentItem();
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (itemStack.isEmpty() || block == Blocks.AIR) {
			return;
		}
		ReplacePacketHandler.sendReplaceMessage(target.getBlockPos(), block, itemStack.getItemDamage(), mc.world.getBlockState(target.getBlockPos()).getBlock());
	}


}