package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.lotqwerty.lottweaks.fabric.ScrollEvent;

import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;

@Mixin(Mouse.class)
public abstract class MouseScrollRedirector {

	@Redirect(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
	private void lottweaks_redirectedOnMouseScroll(PlayerInventory playerInventory, double scrollAmount) {
		if (ScrollEvent.post(new ScrollEvent(scrollAmount))) {
			return;
		}
		playerInventory.scrollInHotbar(scrollAmount);
	}

}
