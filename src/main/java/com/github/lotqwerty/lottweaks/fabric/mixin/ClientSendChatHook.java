package com.github.lotqwerty.lottweaks.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.lotqwerty.lottweaks.fabric.ClientChatEvent;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientSendChatHook {

	@Inject(method = "sendChatMessage(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void lottweaks_sendChatMessage(String message, CallbackInfo ci) {
		if (ClientChatEvent.post(new ClientChatEvent(message))) {
			ci.cancel();
		}
	}

}
