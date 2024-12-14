package com.mlinetles.nativeloader.wrappers;

import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            return Arrays.stream(ItemStructure.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toUnmodifiableList());
        }

        public Pointer use;
        public Pointer getTranslationKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (structure.use == Pointer.NULL) return super.use(world, user, hand);
        return (TypedActionResult<ItemStack>)Function.getFunction(structure.use).invoke(Object.class, new Object[] { world, user, hand }, LoadedLibraries.options);
    }

    @Override
    public String getTranslationKey() {
        if (structure.getTranslationKey == Pointer.NULL) return super.getTranslationKey();
        return (String)Function.getFunction(structure.getTranslationKey).invoke(Object.class, null, LoadedLibraries.options);
    }
}
