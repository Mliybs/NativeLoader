package com.mlinetles.nativeloader.minecraft;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

import java.util.Set;
import java.util.function.Consumer;

public class NativeResourcePackProvider implements ResourcePackProvider {
    private final Set<ResourcePack> _paths;
    private final ResourcePackSource _source = x -> x;

    public NativeResourcePackProvider(Set<ResourcePack> paths) {
        _paths = paths;
    }

    public void register(Consumer<ResourcePackProfile> profileAdder, ResourcePackProfile.Factory factory) {
        for (ResourcePack pack : _paths) {
            var profile = ResourcePackProfile.of("nativeloader/" + pack.getName(), true, () -> pack, factory, ResourcePackProfile.InsertionPosition.TOP, _source);
            if (profile != null) {
                profileAdder.accept(profile);
            }
        }
    }
}
