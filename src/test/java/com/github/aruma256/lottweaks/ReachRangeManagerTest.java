package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.aruma256.lottweaks.testhelper.MinecraftTestBase;
import com.github.aruma256.lottweaks.testhelper.TestHelper;
import com.github.aruma256.lottweaks.testhelper.TestHelper.DummyPlayer;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;

class ReachRangeManagerTest extends MinecraftTestBase {

	@Test @Disabled
	final void test_onLogin() throws Exception {
		DummyPlayer player = TestHelper.getDummyPlayer();
		ModifiableAttributeInstance attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		ReachRangeManager.setV2Modifier(player, 40);
		attributeInstance.addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "lottweaks", 20, AttributeModifier.Operation.ADDITION));
		attributeInstance.addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "other_mod", 10, AttributeModifier.Operation.ADDITION));
		assertEquals(40 + 20 + 10, attributeInstance.getValue());
		// remove V1 and V2 modifier on login
		ReachRangeManager.onLogin(player);
		assertEquals(10 + attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test @Disabled
	final void test_onLogout() throws Exception {
		DummyPlayer player = TestHelper.getDummyPlayer();
		ModifiableAttributeInstance attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		ReachRangeManager.setV2Modifier(player, 30);
		// remove V2 modifier on logout
		ReachRangeManager.onLogout(player);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test @Disabled
	final void test_onPlayerTick() throws Exception {
		DummyPlayer player = TestHelper.getDummyPlayer();
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
		player.isCreative = false;
		instance.onPlayerTick(event);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

	@Test @Disabled
	final void test_setV2Modifier() throws Exception {
		DummyPlayer player;
		ModifiableAttributeInstance attributeInstance;

		// CREATIVE MODE

		player = TestHelper.getDummyPlayer();
		attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		assertNull(attributeInstance.getModifier(TestHelper.getV2UUID()));
		// `setV2Modifier` applies range modifier
		ReachRangeManager.setV2Modifier(player, 22);
		assertEquals(22, attributeInstance.getValue());
		// The modifier must be overwritten, not combined
		ReachRangeManager.setV2Modifier(player, 44);
		assertEquals(44, attributeInstance.getValue());

		// SURVIVAL MODE

		player = TestHelper.getDummyPlayer();
		player.isCreative = false;
		attributeInstance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
		// The modifier must not be applied to player entities that are not creative mode.
		ReachRangeManager.setV2Modifier(player, 33);
		assertEquals(attributeInstance.getBaseValue(), attributeInstance.getValue());
	}

}
