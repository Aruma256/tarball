package com.github.aruma256.lottweaks.testhelper;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.UUID;

import com.github.aruma256.lottweaks.ReachRangeManager;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestHelper {

	public static ItemStack createNBTstack(ItemStack itemStack, String nbtString) {
		try {
			itemStack.setTag(JsonToNBT.parseTag(nbtString));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		return itemStack;
	}

	public static World getDummyWorld() {
		return mock(World.class);	//FIXME
	}

	public static class DummyPlayer extends PlayerEntity {

		public boolean isSpectator = false;
		public boolean isCreative = true;

		public DummyPlayer(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
			super(world, blockPos, f, gameProfile);
		}

		@Override
		public boolean isSpectator() {
			return isSpectator;
		}

		@Override
		public boolean isCreative() {
			return isCreative;
		}

	}

	public static DummyPlayer getDummyPlayer() {
		return mock(DummyPlayer.class);
	}

	public static UUID getV2UUID() throws Exception {
		Field uuidField = ReachRangeManager.class.getDeclaredField("_UUID");
		uuidField.setAccessible(true);
		return (UUID) uuidField.get(null);
	}

}
