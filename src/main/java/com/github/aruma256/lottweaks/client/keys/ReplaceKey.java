package com.github.aruma256.lottweaks.client.keys;

import static com.github.aruma256.lottweaks.client.ClientUtil.getClient;

import com.github.aruma256.lottweaks.LotTweaks;
import com.github.aruma256.lottweaks.client.CompatibilityChecker;
import com.github.aruma256.lottweaks.client.renderer.LTTextRenderer;
import com.github.aruma256.lottweaks.client.renderer.SelectionBoxRenderer;
import com.github.aruma256.lottweaks.network.LTPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReplaceKey extends LTKeyBase {

	private IBlockState lockedBlockState = null;

	public ReplaceKey(int keyCode, String category) {
		super("Replace", keyCode, category);
	}

	@Override
	protected void onKeyPressStart() {
		if (getClient().player.isSneaking() ^ LotTweaks.CONFIG.INVERT_REPLACE_LOCK) {
			RayTraceResult target = getClient().objectMouseOver;
			if (target != null && target.typeOfHit == RayTraceResult.Type.BLOCK){
				lockedBlockState = getClient().world.getBlockState(target.getBlockPos());
			} else {
				lockedBlockState = Blocks.AIR.getDefaultState();
			}
		} else {
			lockedBlockState = null;
		}
	}

	@Override
	protected void onKeyReleased() {
		lockedBlockState = null;
	}

	@SubscribeEvent
	public void onDrawBlockHighlightEvent(final DrawBlockHighlightEvent event) {
		if (this.pressTime == 0) {
			return;
		}
		if (lockedBlockState == null) {
			return;
		}
		RayTraceResult target = getClient().objectMouseOver;
		if (target != null && target.typeOfHit == RayTraceResult.Type.BLOCK){
			if (SelectionBoxRenderer.render(target.getBlockPos(), event.getPartialTicks(), 1f, 0f, 0f)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(final RenderTickEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) {
			return;
		}
		if (this.pressTime == 0) {
			return;
		}
		if (!CompatibilityChecker.instance.isServerCompatibleWith("2.2.1")) {
			LTTextRenderer.showServerSideRequiredMessage("2.2.1");
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
		if (!isPlayerCreative()) {
			return;
		}
		RayTraceResult target = getClient().objectMouseOver;
		if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK){
        	return;
        }
        IBlockState state = getClient().world.getBlockState(target.getBlockPos());
        if (state.getBlock().isAir(state, getClient().world, target.getBlockPos()))
        {
            return;
        }
        if (lockedBlockState != null && lockedBlockState != state) {
            return;
        }
		ItemStack itemStack = getClient().player.inventory.getCurrentItem();
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (itemStack.isEmpty() || block == Blocks.AIR) {
			return;
		}
		float hitX = (float) (target.hitVec.x - target.getBlockPos().getX());
		float hitY = (float) (target.hitVec.y - target.getBlockPos().getY());
		float hitZ = (float) (target.hitVec.z - target.getBlockPos().getZ());
		IBlockState newBlockState = block.getStateForPlacement(getClient().world, target.getBlockPos(), target.sideHit, hitX, hitY, hitZ, itemStack.getItemDamage(), getClient().player, EnumHand.MAIN_HAND);
		LTPacketHandler.sendReplaceMessage(target.getBlockPos(), block, block.getMetaFromState(newBlockState), getClient().world.getBlockState(target.getBlockPos()).getBlock());
		// add to history
		ExPickKey.addToHistory(state.getBlock().getPickBlock(state, target, getClient().world, target.getBlockPos(), getClient().player));
	}

}