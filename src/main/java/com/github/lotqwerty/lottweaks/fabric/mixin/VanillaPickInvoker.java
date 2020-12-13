package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public interface VanillaPickInvoker {
	
	@Invoker("doItemPick")
	public void lottweaks_invokeVanillaItemPick();

}
