package com.github.aruma256.lottweaks.network;

import java.util.function.Supplier;
import java.nio.charset.StandardCharsets;

import com.github.aruma256.lottweaks.ReachRangeManager;
import com.github.aruma256.lottweaks.LotTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

//https://mcforge.readthedocs.io/en/1.16.x/networking/simpleimpl/

public class LTPacketHandler {

	private static final String PROTOCOL_VERSION = 'v' + LotTweaks.VERSION;
	private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(LotTweaks.MODID),
		() -> PROTOCOL_VERSION,
		(serverVersion) -> serverVersion.equals(NetworkRegistry.ACCEPTVANILLA) || serverVersion.equals(NetworkRegistry.ABSENT) || serverVersion.compareTo(PROTOCOL_VERSION) <= 0,
		(clientVersion) -> true
	);

	public static void init() {
		int id = 0;
		INSTANCE.messageBuilder(ReplaceMessage.class, id++)
			.encoder(ReplaceMessage::toBytes)
			.decoder(ReplaceMessage::new)
			.consumer(ReplaceMessage::handle)
			.add();
		INSTANCE.messageBuilder(ReachRangeMessage.class, id++)
			.encoder(ReachRangeMessage::toBytes)
			.decoder(ReachRangeMessage::new)
			.consumer(ReachRangeMessage::handle)
			.add();
		INSTANCE.messageBuilder(HelloMessage.class, id++)
			.encoder(HelloMessage::toBytes)
			.decoder(HelloMessage::new)
			.consumer(HelloMessage::handle)
			.add();
	}

	public static void sendReplaceMessage(BlockPos pos, BlockState state, BlockState checkState) {
		INSTANCE.sendToServer(new ReplaceMessage(pos, state, checkState));
	}

	public static void sendReachRangeMessage(double dist) {
		INSTANCE.sendToServer(new ReachRangeMessage(dist));
	}

	public static void sendHelloMessage(ServerPlayerEntity player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new HelloMessage(LotTweaks.VERSION));
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
			this(buf.readBlockPos(), Block.stateById(buf.readInt()), Block.stateById(buf.readInt()));
		}

		public void toBytes(PacketBuffer buf) {
			buf.writeBlockPos(this.pos);
			buf.writeInt(Block.getId(state));
			buf.writeInt(Block.getId(checkState));
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			ctx.get().setPacketHandled(true);
			final ServerPlayerEntity player = ctx.get().getSender();
			if (!player.isCreative()) {
				return;
			}
			if (player.getLevel().isClientSide()) {
				// kore iru ??
				return;
			}
			if (LotTweaks.CONFIG.REQUIRE_OP_TO_USE_REPLACE.get() && player.getServer().getPlayerList().getOps().get(player.getGameProfile())==null) {
				return;
			}
			// validation
			if (state.getBlock() == Blocks.AIR) {
				return;
			}
			double dist = player.getEyePosition(1.0F).distanceTo(new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
			if (dist > LotTweaks.CONFIG.MAX_RANGE.get()) {
				return;
			}
			if (player.getLevel().getBlockState(pos) != checkState) {
				return;
			}
			//
			ctx.get().enqueueWork(() -> {
				player.getLevel().setBlock(pos, state, 2);
			});
			return;
		}
	}

	// ReachRange

	public static class ReachRangeMessage {

		private double dist;

		public ReachRangeMessage(double dist) {
			this.dist = dist;
		}

		public ReachRangeMessage(PacketBuffer buf) {
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
				dist = Math.min(LotTweaks.CONFIG.MAX_RANGE.get(), dist);
				ReachRangeManager.setV2Modifier(player, dist);
			});
			return;
		}
	}


	// Hello

	public static class HelloMessage {

		public static HelloCallback callback = null;

		public static interface HelloCallback {
			public void onHello(String version);
		}

		private String version;

		public HelloMessage(String version) {
			this.version = version;
		}

		public HelloMessage(PacketBuffer buf) {
			this.version = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
		}

		public void toBytes(PacketBuffer buf) {
			buf.writeInt(version.length());
			buf.writeCharSequence(version, StandardCharsets.UTF_8);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx) {
			ctx.get().setPacketHandled(true);
			if (callback != null) {
				callback.onHello(version);
			}
		}

	}

}
