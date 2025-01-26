package com.mlinetles.nativeloader.minecraft;

import net.minecraft.resource.*;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Consumer;

public class NativeResourcePackProvider implements ResourcePackProvider {
    private final Map<String, ResourcePackProfile.PackFactory> _factories;
    private final ResourceType _type;
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

    public NativeResourcePackProvider(Map<String, ResourcePackProfile.PackFactory> factories, ResourceType type) {
        _factories = factories;
        _type = type;
    }

    public void register(Consumer<ResourcePackProfile> profileAdder) {
        _factories.forEach((name, factory) -> {
            var key = name.endsWith(".cnmd") ? name.substring(0, name.length() - 5) : name;
            var profile = ResourcePackProfile.create("nativeloader/" + name, Text.translatableWithFallback("nativeloader." + key.toLowerCase(), key), true, factory, _type, ResourcePackProfile.InsertionPosition.TOP, _source);
            if (profile == null) return;
            profileAdder.accept(profile);
        });
    }
}
