package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;
import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

class ReachRangeManagerTest extends MinecraftTestBase {

	@Test
	final void test_onLogin() throws Exception {
		ServerPlayerEntity player = getDummyPlayer();
		ModifiableAttributeInstance attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		ReachRangeManager.setV2Modifier(player, 40);
		attributeInstance.addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "lottweaks", 20, AttributeModifier.Operation.ADDITION));
		attributeInstance.addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "other_mod", 10, AttributeModifier.Operation.ADDITION));
		assertEquals(40 + 20 + 10, attributeInstance.getValue());
		// remove V1 and V2 modifier on login
		ReachRangeManager.onLogin(player);
		assertEquals(10 + attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test
	final void test_onLogout() throws Exception {
		ServerPlayerEntity player = getDummyPlayer();
		ModifiableAttributeInstance attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		ReachRangeManager.setV2Modifier(player, 30);
		// remove V2 modifier on logout
		ReachRangeManager.onLogout(player);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test
	final void test_onPlayerTick() throws Exception {
		ServerPlayerEntity player = getDummyPlayer();
		ModifiableAttributeInstance attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		ReachRangeManager instance = new ReachRangeManager();
		PlayerTickEvent event = new PlayerTickEvent(null, player);
//		Field sideField = TickEvent.class.getDeclaredField("side");
//		sideField.setAccessible(true);
//		sideField.set(event, Side.SERVER);
		ReachRangeManager.setV2Modifier(player, 30);
		// keep modifier if player's gamemode is creative
		instance.onPlayerTick(event);
		assertEquals(30, attributeInstance.getValue());
		// remove modifier if player's gamemode is NOT creative
		player.setGameMode(GameType.SURVIVAL);
		instance.onPlayerTick(event);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test
	final void test_setV2Modifier() throws Exception {
		ServerPlayerEntity player;
		ModifiableAttributeInstance attributeInstance;

		// CREATIVE MODE

		player = getDummyPlayer();
		attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		assertNull(attributeInstance.getModifier(getV2UUID()));
		// `setV2Modifier` applies range modifier
		ReachRangeManager.setV2Modifier(player, 22);
		assertEquals(22, attributeInstance.getValue());
		// The modifier must be overwritten, not combined
		ReachRangeManager.setV2Modifier(player, 44);
		assertEquals(44, attributeInstance.getValue());

		// SURVIVAL MODE

		player = getDummyPlayer();
		player.setGameMode(GameType.SURVIVAL);
		attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		// The modifier must not be applied to player entities that are not creative mode.
		ReachRangeManager.setV2Modifier(player, 33);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	// ######################
	//   SETUP METHODS
	// ######################

	private static ServerPlayerEntity getDummyPlayer() throws Exception {
		MinecraftServer minecraftServerMock = mock(MinecraftServer.class);
		when(minecraftServerMock.getPlayerList()).thenReturn(mock(PlayerList.class));

		ServerWorld worldServerMock = mock(ServerWorld.class);
//		Field providerField = World.class.getDeclaredField("provider");
//		providerField.setAccessible(true);
//		WorldProvider providerMock = mock(WorldProvider.class);
//		when(providerMock.getRandomizedSpawnPoint()).thenReturn(mock(BlockPos.class));
//		providerField.set(worldServerMock, providerMock);
//		when(worldServerMock.getSpawnPoint()).thenReturn(mock(BlockPos.class));
//		when(worldServerMock.getEntityTracker()).thenReturn(mock(EntityTracker.class));

		ServerPlayerEntity dummyPlayer = spy(new ServerPlayerEntity(minecraftServerMock, worldServerMock, new GameProfile(UUID.randomUUID(), null), new PlayerInteractionManager(worldServerMock)));
//		dummyPlayer.connection = mock(NetHandlerPlayServer.class);
//		dummyPlayer.setGameType(GameType.CREATIVE);
		return dummyPlayer;
	}

	private static UUID getV2UUID() throws Exception {
		Field uuidField = ReachRangeManager.class.getDeclaredField("_UUID");
		uuidField.setAccessible(true);
		return (UUID) uuidField.get(null);
	}

}
