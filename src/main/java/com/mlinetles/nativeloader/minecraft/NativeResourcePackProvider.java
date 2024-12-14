package com.mlinetles.nativeloader.minecraft;

import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.util.Set;
import java.util.function.Consumer;

public class NativeResourcePackProvider implements ResourcePackProvider {
    private final Set<ResourcePack> _paths;
    private static final ResourcePackSource _source = new ResourcePackSource() {
        @Override
        public Text decorate(Text packName) {
            return packName;
        }

        @Override
        public boolean canBeEnabledLater() {
            return true;
        }
    };

    public NativeResourcePackProvider(Set<ResourcePack> paths) {
        _paths = paths;
    }

    public void register(Consumer<ResourcePackProfile> profileAdder) {
        for (ResourcePack pack : _paths) {
            var profile = ResourcePackProfile.create("nativeloader/" + pack.getName(), Text.literal(pack.getName()), true, new ResourcePackProfile.PackFactory() {
                @Override
                public ResourcePack open(String name) {
                    return pack;
                }

                @Override
                public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
                    return pack;
                }
            }, ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.TOP, _source);
            if (profile == null) return;
            profileAdder.accept(profile);
        }
    }
}
