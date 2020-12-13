package com.github.lotqwerty.lottweaks.network;

import com.github.lotqwerty.lottweaks.LotTweaks;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

//https://mcforge.readthedocs.io/en/1.16.x/networking/simpleimpl/

public class LTPacketHandler {

//	private static final String PROTOCOL_VERSION = "3";
//	private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//		new ResourceLocation(LotTweaks.MODID),
//		() -> PROTOCOL_VERSION,
//	    PROTOCOL_VERSION::equals,
//	    PROTOCOL_VERSION::equals
//	);
	private static final Identifier REPLACE_PACKET_ID = new Identifier(LotTweaks.MODID, "replace_packet");

	public static void init() {
		ServerSidePacketRegistry.INSTANCE.register(REPLACE_PACKET_ID, (ctx, buf) -> {new ReplaceMessage(buf).handle(ctx);});
//		INSTANCE.messageBuilder(AdjustRangeMessage.class, id++)
//			.encoder(AdjustRangeMessage::toBytes)
//			.decoder(AdjustRangeMessage::new)
//			.consumer(AdjustRangeMessage::handle)
//			.add();
	}

	public static void sendReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		new ReplaceMessage(pos, state, checkState).toBytes(buf);
		ClientSidePacketRegistry.INSTANCE.sendToServer(REPLACE_PACKET_ID, buf);
	}

//	public static void sendReachRangeMessage(double dist) {
//		INSTANCE.sendToServer(new AdjustRangeMessage(dist));
//	}

	//Replace

	public static class ReplaceMessage {

		private final BlockPos pos;
		private final BlockState state;
		private final BlockState checkState;

		public ReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
			this.pos = pos;
			this.state = state;
			this.checkState = checkState;
		}

		public ReplaceMessage(PacketByteBuf buf) {
			this(buf.readBlockPos(), Block.getStateFromRawId(buf.readInt()), Block.getStateFromRawId(buf.readInt()));
		}

		public void toBytes(PacketByteBuf buf) {
			buf.writeBlockPos(this.pos);
			buf.writeInt(Block.getRawIdFromState(state));
			buf.writeInt(Block.getRawIdFromState(checkState));
		}

		public void handle(PacketContext ctx) {
//			ctx.get().setPacketHandled(true);
			final ServerPlayerEntity player = (ServerPlayerEntity)ctx.getPlayer();
			if (!player.isCreative()) {
				return;
			}
//			if (player.getServerWorld().isRemote) {
//				// kore iru ??
//				return;
//			}
			if (LotTweaks.CONFIG.REQUIRE_OP_TO_USE_REPLACE && player.getServer().getPlayerManager().getOpList().get(player.getGameProfile())==null) {
				return;
			}
			// validation
			if (state.getBlock() == Blocks.AIR) {
				return;
			}
			double dist = player.getCameraPosVec(1.0F).distanceTo(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
			if (dist > LotTweaks.CONFIG.MAX_RANGE) {
				return;
			}
			if (player.getServerWorld().getBlockState(pos) != checkState) {
				return;
			}
			//
			ctx.getTaskQueue().execute(() -> {
				player.getServerWorld().setBlockState(pos, state, 2);
			});
			return;
		}
	}

//	// AdjustRange
//
//	public static class AdjustRangeMessage {
//
//		private double dist;
//
//		public AdjustRangeMessage(double dist) {
//			this.dist = dist;
//		}
//
//		public AdjustRangeMessage(PacketBuffer buf) {
//			this(buf.readDouble());
//		}
//
//		public void toBytes(PacketBuffer buf) {
//			buf.writeDouble(this.dist);
//		}
//
//		public void handle(Supplier<NetworkEvent.Context> ctx) {
//			ctx.get().setPacketHandled(true);
//			final ServerPlayerEntity player = ctx.get().getSender();
//			if (!player.isCreative()) {
//				return;
//			}
//			ctx.get().enqueueWork(() -> {
//				if (dist < 0) {
//					return;
//				}
//				dist = Math.min(LotTweaks.CONFIG.MAX_RANGE.get(), dist);
//				AdjustRangeHelper.changeRangeModifier(player, dist);
//			});
//			return;
//		}
//	}
}
