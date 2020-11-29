package com.github.lotqwerty.lottweaks.network;

import java.util.function.Supplier;

import com.github.lotqwerty.lottweaks.AdjustRangeHelper;
import com.github.lotqwerty.lottweaks.LotTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

//https://mcforge.readthedocs.io/en/1.16.x/networking/simpleimpl/

public class LTPacketHandler {

	private static final String PROTOCOL_VERSION = "3";
	private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(LotTweaks.MODID),
		() -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);

	public static void init() {
		int id = 0;
		INSTANCE.messageBuilder(ReplaceMessage.class, id++)
			.encoder(ReplaceMessage::toBytes)
			.decoder(ReplaceMessage::new)
			.consumer(ReplaceMessage::handle)
			.add();
		INSTANCE.messageBuilder(AdjustRangeMessage.class, id++)
			.encoder(AdjustRangeMessage::toBytes)
			.decoder(AdjustRangeMessage::new)
			.consumer(AdjustRangeMessage::handle)
			.add();
	}

	public static void sendReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
		INSTANCE.sendToServer(new ReplaceMessage(pos, state, checkState));
	}

	public static void sendReachRangeMessage(double dist) {
		INSTANCE.sendToServer(new AdjustRangeMessage(dist));
	}

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

		public ReplaceMessage(PacketBuffer buf) {
			this(buf.readBlockPos(), Block.getStateById(buf.readInt()), Block.getStateById(buf.readInt()));
		}

		public void toBytes(PacketBuffer buf) {
			buf.writeBlockPos(this.pos);
			buf.writeInt(Block.getStateId(state));
			buf.writeInt(Block.getStateId(checkState));
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			ctx.get().setPacketHandled(true);
			final ServerPlayerEntity player = ctx.get().getSender();
			if (!player.isCreative()) {
				return;
			}
			if (player.getServerWorld().isRemote) {
				// kore iru ??
				return;
			}
			if (LotTweaks.CONFIG.REQUIRE_OP_TO_USE_REPLACE.get() && player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile())==null) {
				return;
			}
			// validation
			if (state.getBlock() == Blocks.AIR) {
				return;
			}
			double dist = player.getEyePosition(1.0F).distanceTo(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
			if (dist > LotTweaks.CONFIG.MAX_RANGE.get()) {
				return;
			}
			if (player.getServerWorld().getBlockState(pos) != checkState) {
				return;
			}
			//
			ctx.get().enqueueWork(() -> {
				player.getServerWorld().setBlockState(pos, state, 2);
			});
			return;
		}
	}

	// AdjustRange

	public static class AdjustRangeMessage {

		private double dist;

		public AdjustRangeMessage(double dist) {
			this.dist = dist;
		}

		public AdjustRangeMessage(PacketBuffer buf) {
			this(buf.readDouble());
		}

		public void toBytes(PacketBuffer buf) {
			buf.writeDouble(this.dist);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			ctx.get().setPacketHandled(true);
			final ServerPlayerEntity player = ctx.get().getSender();
			if (!player.isCreative()) {
				return;
			}
			ctx.get().enqueueWork(() -> {
				if (dist < 0) {
					return;
				}
				dist = Math.min(LotTweaks.CONFIG.MAX_RANGE.get(), dist);
				AdjustRangeHelper.changeRangeModifier(player, dist);
			});
			return;
		}
	}
}
