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
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

class ReachRangeManagerTest extends MinecraftTestBase {

	@Test
	final void test_onLogin() throws Exception {
		EntityPlayerMP player = getDummyPlayer();
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
		EntityPlayerMP player = getDummyPlayer();
		IAttributeInstance attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		ReachRangeManager.setV2Modifier(player, 30);
		// remove V2 modifier on logout
		ReachRangeManager.onLogout(player);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_onPlayerTick() throws Exception {
		EntityPlayerMP player = getDummyPlayer();
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
		player.setGameType(GameType.SURVIVAL);
		instance.onPlayerTick(event);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_setV2Modifier() throws Exception {
		EntityPlayerMP player;
		IAttributeInstance attributeInstance;

		// CREATIVE MODE

		player = getDummyPlayer();
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		assertNull(attributeInstance.getModifier(getV2UUID()));
		// `setV2Modifier` applies range modifier
		ReachRangeManager.setV2Modifier(player, 22);
		assertEquals(22, attributeInstance.getAttributeValue());
		// The modifier must be overwritten, not combined
		ReachRangeManager.setV2Modifier(player, 44);
		assertEquals(44, attributeInstance.getAttributeValue());

		// SURVIVAL MODE

		player = getDummyPlayer();
		player.setGameType(GameType.SURVIVAL);
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		// The modifier must not be applied to player entities that are not creative mode.
		ReachRangeManager.setV2Modifier(player, 33);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	// ######################
	//   SETUP METHODS
	// ######################

	private static EntityPlayerMP getDummyPlayer() throws Exception {
		MinecraftServer minecraftServerMock = mock(MinecraftServer.class);
		when(minecraftServerMock.getPlayerList()).thenReturn(mock(PlayerList.class));

		WorldServer worldServerMock = mock(WorldServer.class);
		Field providerField = World.class.getDeclaredField("provider");
		providerField.setAccessible(true);
		WorldProvider providerMock = mock(WorldProvider.class);
		when(providerMock.getRandomizedSpawnPoint()).thenReturn(mock(BlockPos.class));
		providerField.set(worldServerMock, providerMock);
		when(worldServerMock.getSpawnPoint()).thenReturn(mock(BlockPos.class));
		when(worldServerMock.getEntityTracker()).thenReturn(mock(EntityTracker.class));

		EntityPlayerMP dummyPlayer = spy(new EntityPlayerMP(minecraftServerMock, worldServerMock, new GameProfile(UUID.randomUUID(), null), new PlayerInteractionManager(worldServerMock)));
		dummyPlayer.connection = mock(NetHandlerPlayServer.class);
		dummyPlayer.setGameType(GameType.CREATIVE);
		return dummyPlayer;
	}

	private static UUID getV2UUID() throws Exception {
		Field uuidField = ReachRangeManager.class.getDeclaredField("_UUID");
		uuidField.setAccessible(true);
		return (UUID) uuidField.get(null);
	}

}
