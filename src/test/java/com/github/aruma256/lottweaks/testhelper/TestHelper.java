package com.github.aruma256.lottweaks.testhelper;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.UUID;

import com.github.aruma256.lottweaks.ReachRangeManager;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class TestHelper {

	public static ItemStack createNBTstack(ItemStack itemStack, String nbtString) {
		try {
			itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtString));
		} catch (NBTException e) {
			throw new RuntimeException(e);
		}
		return itemStack;
	}

	public static class DummyWorld extends World {

		DummyWorld(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
			super(saveHandlerIn, info, providerIn, profilerIn, client);
		}

		@Override
		protected IChunkProvider createChunkProvider() {
			return null;
		}

		@Override
		protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
			return false;
		}

		@Override
		public BlockPos getSpawnPoint() {
			return new BlockPos(0, 0, 0);
		}

	}

	public static DummyWorld getDummyWorld() {
		WorldProvider providerMock = mock(WorldProvider.class);
		return new DummyWorld(null, null, providerMock, null, false);
	}

	public static class DummyPlayer extends EntityPlayer {

		public boolean isSpectator = false;
		public boolean isCreative = true;

		DummyPlayer(World worldIn, GameProfile gameProfileIn) {
			super(worldIn, gameProfileIn);
		}

		@Override
		public boolean isSpectator() {
			return isSpectator;
		}

		@Override
		public boolean isCreative() {
			// TODO Auto-generated method stub
			return isCreative;
		}

	}

	public static DummyPlayer getDummyPlayer() {
		return new DummyPlayer(getDummyWorld(), new GameProfile(UUID.randomUUID(), null));
	}

	public static UUID getV2UUID() throws Exception {
		Field uuidField = ReachRangeManager.class.getDeclaredField("_UUID");
		uuidField.setAccessible(true);
		return (UUID) uuidField.get(null);
	}

}
