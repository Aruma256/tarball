package com.github.lotqwerty.lottweaks.network;

import com.github.lotqwerty.lottweaks.LotTweaks;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//https://mcforge.readthedocs.io/en/1.12.x/networking/simpleimpl/

public class LTPacketHandler {

	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LotTweaks.MODID);

	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(ReplaceMessageHandler.class, ReplaceMessage.class, id++, Side.SERVER);
		INSTANCE.registerMessage(AdjustRangeMessageHandler.class, AdjustRangeMessage.class, id++, Side.SERVER);
	}

	public static void sendReplaceMessage(BlockPos pos, Block block, int meta, Block checkBlock) {
		INSTANCE.sendToServer(new ReplaceMessage(pos, block, meta, checkBlock));
	}

	public static void sendReachRangeMessage(double dist) {
		INSTANCE.sendToServer(new AdjustRangeMessage(dist));
	}

	//Replace

	public static class ReplaceMessage implements IMessage {

		private BlockPos pos;
		private Block block;
		private int meta;
		private Block checkBlock;

		public ReplaceMessage(BlockPos pos, Block block, int meta, Block checkBlock) {
			this.pos = pos;
			this.block = block;
			this.meta = meta;
			this.checkBlock = checkBlock;
		}

		public ReplaceMessage() {
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(this.pos.getX());
			buf.writeInt(this.pos.getY());
			buf.writeInt(this.pos.getZ());
			buf.writeInt(Block.getIdFromBlock(this.block));
			buf.writeInt(this.meta);
			buf.writeInt(Block.getIdFromBlock(this.checkBlock));
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			this.block = Block.getBlockById(buf.readInt());
			this.meta = buf.readInt();
			this.checkBlock = Block.getBlockById(buf.readInt());
		}
	}

	public static class ReplaceMessageHandler implements IMessageHandler<ReplaceMessage, IMessage> {

		@SuppressWarnings("deprecation")
		@Override
		public IMessage onMessage(ReplaceMessage message, MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			final BlockPos pos = message.pos;
			final Block block = message.block;
			final int meta = message.meta;
			final Block checkBlock = message.checkBlock;
			if (!player.isCreative()) {
				return null;
			}
			if (player.getServerWorld().isRemote) {
				// kore iru ??
				return null;
			}
			if (LotTweaks.CONFIG.REQUIRE_OP_TO_USE_REPLACE && FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile())==null) {
				return null;
			}
			// validation
			if (block == Blocks.AIR) {
				return null;
			}
			double dist = Math.sqrt(player.getDistanceSq(pos));
			if (dist > LotTweaks.CONFIG.REPLACE_RANGE) {
				return null;
			}
			if (player.getServerWorld().getBlockState(pos).getBlock() != checkBlock) {
				return null;
			}
			//
			player.getServerWorld().addScheduledTask(() -> {
				player.getServerWorld().setBlockState(pos, block.getStateFromMeta(meta), 2);
			});
			return null;
		}
	}

	// AdjustRange

	public static class AdjustRangeMessage implements IMessage {

		private double dist;

		public AdjustRangeMessage(double dist) {
			this.dist = dist;
		}

		public AdjustRangeMessage() {
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeDouble(this.dist);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.dist = buf.readDouble();
		}

	}

	public static class AdjustRangeMessageHandler implements IMessageHandler<AdjustRangeMessage, IMessage> {

		@Override
		public IMessage onMessage(AdjustRangeMessage message, MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			double dist = message.dist;
			if (dist < 0 || 256 < dist) {
				return null;
			}
			player.getServerWorld().addScheduledTask(() -> {
				IAttributeInstance instance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
				for (AttributeModifier modifier: instance.getModifiers()) {
					if (modifier.getName().equals(LotTweaks.MODID)) {
						instance.removeModifier(modifier);
					}
				}
				instance.applyModifier(new AttributeModifier(LotTweaks.MODID, dist, 0));
			});
			return null;
		}
	}
}
