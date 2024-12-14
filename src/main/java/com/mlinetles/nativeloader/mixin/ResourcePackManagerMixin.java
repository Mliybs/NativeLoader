package com.mlinetles.nativeloader.mixin;

import com.mlinetles.nativeloader.NativeLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
	@SuppressWarnings("DataFlowIssue")
    @Inject(at = @At("TAIL"), method = "<init>([Lnet/minecraft/resource/ResourcePackProvider;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V")
	private void init(ResourcePackProvider[] providers, CallbackInfo info) {
        NativeLoader.loadResourcePacks((ResourcePackManager)(Object)this);
	}
}