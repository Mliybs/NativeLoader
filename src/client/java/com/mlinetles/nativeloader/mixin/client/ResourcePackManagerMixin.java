package com.mlinetles.nativeloader.mixin.client;

import com.google.common.collect.ImmutableSet;
import com.mlinetles.nativeloader.minecraft.NativeResourcePackProvider;
import net.minecraft.client.resource.DefaultClientResourcePackProvider;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

import static com.mlinetles.nativeloader.NativeLoader.LOADED;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
    @Shadow
    private Set<ResourcePackProvider> providers;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(ResourcePackProvider[] providers, CallbackInfo info) {
        if (this.providers.stream().anyMatch(x -> x instanceof DefaultClientResourcePackProvider) && this.providers.stream().noneMatch(x -> x instanceof NativeResourcePackProvider)) {
            this.providers = new ImmutableSet.Builder<ResourcePackProvider>()
                    .add(new NativeResourcePackProvider(LOADED, ResourceType.CLIENT_RESOURCES))
                    .addAll(this.providers).build();
        }
    }
}
