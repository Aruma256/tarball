package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;
import com.github.aruma256.lottweaks.testhelper.TestHelper;
import com.github.aruma256.lottweaks.testhelper.TestHelper.DummyPlayer;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

class ReachRangeManagerTest extends MinecraftTestBase {

	@Test
	final void test_onLogin() throws Exception {
		DummyPlayer player = TestHelper.getDummyPlayer();
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
		DummyPlayer player = TestHelper.getDummyPlayer();
		IAttributeInstance attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		ReachRangeManager.setV2Modifier(player, 30);
		// remove V2 modifier on logout
		ReachRangeManager.onLogout(player);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

	@Test
	final void test_onPlayerTick() throws Exception {
		DummyPlayer player = TestHelper.getDummyPlayer();
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
		DummyPlayer player;
		IAttributeInstance attributeInstance;

		// CREATIVE MODE

		player = TestHelper.getDummyPlayer();
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		assertNull(attributeInstance.getModifier(TestHelper.getV2UUID()));
		// `setV2Modifier` applies range modifier
		ReachRangeManager.setV2Modifier(player, 22);
		assertEquals(22, attributeInstance.getAttributeValue());
		// The modifier must be overwritten, not combined
		ReachRangeManager.setV2Modifier(player, 44);
		assertEquals(44, attributeInstance.getAttributeValue());

		// SURVIVAL MODE

		player = TestHelper.getDummyPlayer();
		player.isCreative = false;
		attributeInstance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
		// The modifier must not be applied to player entities that are not creative mode.
		ReachRangeManager.setV2Modifier(player, 33);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getAttributeValue());
	}

}
