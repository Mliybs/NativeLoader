package com.mlinetles.nativeloader.wrappers;

import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.List;

public class ItemWrapper extends Item {
    private final ItemStructure structure;

    public ItemWrapper(Settings settings, long handle) {
        super(settings);
        structure = new ItemStructure(new Pointer(handle));
        structure.read();
    }

    // 血的教训，不要把这个结构类设为private
    public static class ItemStructure extends Structure {
        public ItemStructure() {}

        public ItemStructure(Pointer p) {
            super(p);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("getTranslationKey");
        }

        public Pointer getTranslationKey;
    }

    @Override
    public String getTranslationKey() {
        if (structure.getTranslationKey == Pointer.NULL) return super.getTranslationKey();
        return (String)Function.getFunction(structure.getTranslationKey).invoke(Object.class, null, LoadedLibraries.options);
    }
}
