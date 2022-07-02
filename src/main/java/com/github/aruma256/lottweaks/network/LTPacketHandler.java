package com.github.aruma256.lottweaks.network;

import java.nio.charset.StandardCharsets;

import com.github.aruma256.lottweaks.AdjustRangeHelper;
import com.github.aruma256.lottweaks.LotTweaks;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
		INSTANCE.registerMessage(HelloMessageHandler.class, HelloMessage.class, id++, Side.CLIENT);
	}

	public static void sendReplaceMessage(BlockPos pos, Block block, int meta, Block checkBlock) {
		INSTANCE.sendToServer(new ReplaceMessage(pos, block, meta, checkBlock));
	}

	public static void sendReachRangeMessage(int dist) {
		INSTANCE.sendToServer(new AdjustRangeMessage(dist));
	}

	public static void sendHelloMessage(EntityPlayerMP player) {
		INSTANCE.sendTo(new HelloMessage(LotTweaks.VERSION), player);
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
			double dist = player.getPositionEyes(1.0F).distanceTo(new Vec3d(pos));
			if (dist > LotTweaks.CONFIG.MAX_RANGE) {
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

		private int dist;

		public AdjustRangeMessage(int dist) {
			this.dist = dist;
		}

		public AdjustRangeMessage() {
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(this.dist);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.dist = buf.readInt();
		}

	}

	public static class AdjustRangeMessageHandler implements IMessageHandler<AdjustRangeMessage, IMessage> {

		@Override
		public IMessage onMessage(AdjustRangeMessage message, MessageContext ctx) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			if (!player.isCreative()) {
				return null;
			}
			player.getServerWorld().addScheduledTask(() -> {
				int dist = message.dist;
				dist = Math.min(LotTweaks.CONFIG.MAX_RANGE, dist);
				AdjustRangeHelper.setV2Modifier(player, dist);
			});
			return null;
		}
	}


	// Hello

	public static class HelloMessage implements IMessage {

		private String version;

		public HelloMessage() {
		}

		public HelloMessage(String version) {
			this.version = version;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(version.length());
			buf.writeCharSequence(version, StandardCharsets.UTF_8);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.version = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
		}

	}

	public static class HelloMessageHandler implements IMessageHandler<HelloMessage, IMessage> {

		public static HelloCallback callback = null;

		public static interface HelloCallback {
			public void onHello(String version);
		}

		@Override
		public IMessage onMessage(HelloMessage message, MessageContext ctx) {
			if (callback != null) {
				callback.onHello(message.version);
			}
			return null;
		}

	}

}
