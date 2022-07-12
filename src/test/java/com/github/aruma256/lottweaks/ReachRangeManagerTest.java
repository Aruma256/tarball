package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

class ReachRangeManagerTest extends MinecraftTestBase {

	@Test
	final void test_onLogin() throws Exception {
		DummyEntityPlayer player = new DummyEntityPlayer();
		IAttributeInstance attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		ReachRangeManager.setV2Modifier(player, 40);
		attributeInstance.applyModifier(new AttributeModifier(UUID.randomUUID(), "lottweaks", 20, 0));
		attributeInstance.applyModifier(new AttributeModifier(UUID.randomUUID(), "other_mod", 10, 0));
		assertEquals(40 + 20 + 10, attributeInstance.getAttributeValue());
		// remove V1 and V2 modifier on login
		ReachRangeManager.onLogin(player);
		assertEquals(10 + attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_onLogout() throws Exception {
		DummyEntityPlayer player = new DummyEntityPlayer();
		IAttributeInstance attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		ReachRangeManager.setV2Modifier(player, 30);
		// remove V2 modifier on logout
		ReachRangeManager.onLogout(player);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_onPlayerTick() throws Exception {
		DummyEntityPlayer player = new DummyEntityPlayer();
		IAttributeInstance attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		ReachRangeManager instance = new ReachRangeManager();
		PlayerTickEvent event = new PlayerTickEvent(null, player);
		Field sideField = TickEvent.class.getDeclaredField("side");
		sideField.setAccessible(true);
		sideField.set(event, Side.SERVER);
		ReachRangeManager.setV2Modifier(player, 30);
		// keep modifier if player's gamemode is creative
		instance.onPlayerTick(event);
		assertEquals(30, attributeInstance.getAttributeValue());
		// remove modifier if player's gamemode is NOT creative
		player.isCreative = false;
		instance.onPlayerTick(event);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_setV2Modifier() throws Exception {
		DummyEntityPlayer player;
		IAttributeInstance attributeInstance;

		// CREATIVE MODE

		player = new DummyEntityPlayer();
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		assertNull(attributeInstance.getModifier(getV2UUID()));
		// `setV2Modifier` applies range modifier
		ReachRangeManager.setV2Modifier(player, 22);
		assertEquals(22, attributeInstance.getAttributeValue());
		// The modifier must be overwritten, not combined
		ReachRangeManager.setV2Modifier(player, 44);
		assertEquals(44, attributeInstance.getAttributeValue());

		// SURVIVAL MODE

		player = new DummyEntityPlayer();
		player.isCreative = false;
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		// The modifier must not be applied to player entities that are not creative mode.
		ReachRangeManager.setV2Modifier(player, 33);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	// ######################
	//   SETUP METHODS
	// ######################

	private static World getDummyWorld() {
		WorldSettings worldSettings = new WorldSettings(0, null, false, false, WorldType.FLAT);
		WorldInfo worldInfo = new WorldInfo(worldSettings, null);
		WorldProvider worldProvider = new WorldProviderSurface();
		World world = new World(null, worldInfo, worldProvider, null, false) {
			
			@Override
			protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
				return false;
			}
			
			@Override
			protected IChunkProvider createChunkProvider() {
				return null;
			}
		};
		worldProvider.setWorld(world);
		return world;
	}

	private static class DummyEntityPlayer extends EntityPlayer {

		public boolean isCreative = true;

		public DummyEntityPlayer() {
			super(getDummyWorld(), new GameProfile(UUID.randomUUID(), null));
		}

		@Override
		public boolean isSpectator() {
			return false;
		}

		@Override
		public boolean isCreative() {
			return isCreative;
		}
	}

	private static UUID getV2UUID() throws Exception {
		Field uuidField = ReachRangeManager.class.getDeclaredField("_UUID");
		uuidField.setAccessible(true);
		return (UUID) uuidField.get(null);
	}

}
