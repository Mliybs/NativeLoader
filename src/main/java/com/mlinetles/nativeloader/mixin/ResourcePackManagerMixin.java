package com.mlinetles.nativeloader.mixin;

import com.mlinetles.nativeloader.NativeLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/resource/ResourcePackProfile$Factory;[Lnet/minecraft/resource/ResourcePackProvider;)V")
	private void init(ResourcePackProfile.Factory factory, ResourcePackProvider[] providers, CallbackInfo info) {
        NativeLoader.loadResourcePacks((ResourcePackManager)(Object)this);
	}
}