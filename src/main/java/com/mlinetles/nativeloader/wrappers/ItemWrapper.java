package com.mlinetles.nativeloader.wrappers;

import com.google.common.collect.Multimap;
import com.mlinetles.nativeloader.LoadedLibraries;
import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemWrapper extends Item {
    public ItemWrapper(Settings settings, long handle) {
        super(settings);
        var structure = new ItemStructure(new Pointer(handle));
        structure.read();

        var id = structure.id;
        if (id == 0) throw new NullPointerException();
        WrapperStatics.CLEANER.register(this, () -> onFinalize.invokeVoid(new Object[] { id }));

        asItem = structure.asItem == Pointer.NULL ? null : Function.getFunction(structure.asItem);
        getRequiredFeatures = structure.getRequiredFeatures == Pointer.NULL ? null : Function.getFunction(structure.getRequiredFeatures);
        usageTick = structure.usageTick == Pointer.NULL ? null : Function.getFunction(structure.usageTick);
        onItemEntityDestroyed = structure.onItemEntityDestroyed == Pointer.NULL ? null : Function.getFunction(structure.onItemEntityDestroyed);
        postProcessNbt = structure.postProcessNbt == Pointer.NULL ? null : Function.getFunction(structure.postProcessNbt);
        canMine = structure.canMine == Pointer.NULL ? null : Function.getFunction(structure.canMine);
        useOnBlock = structure.useOnBlock == Pointer.NULL ? null : Function.getFunction(structure.useOnBlock);
        getMiningSpeedMultiplier = structure.getMiningSpeedMultiplier == Pointer.NULL ? null : Function.getFunction(structure.getMiningSpeedMultiplier);
        use = structure.use == Pointer.NULL ? null : Function.getFunction(structure.use);
        finishUsing = structure.finishUsing == Pointer.NULL ? null : Function.getFunction(structure.finishUsing);
        isDamageable = structure.isDamageable == Pointer.NULL ? null : Function.getFunction(structure.isDamageable);
        isItemBarVisible = structure.isItemBarVisible == Pointer.NULL ? null : Function.getFunction(structure.isItemBarVisible);
        getItemBarStep = structure.getItemBarStep == Pointer.NULL ? null : Function.getFunction(structure.getItemBarStep);
        getItemBarColor = structure.getItemBarColor == Pointer.NULL ? null : Function.getFunction(structure.getItemBarColor);
        onStackClicked = structure.onStackClicked == Pointer.NULL ? null : Function.getFunction(structure.onStackClicked);
        onClicked = structure.onClicked == Pointer.NULL ? null : Function.getFunction(structure.onClicked);
        postHit = structure.postHit == Pointer.NULL ? null : Function.getFunction(structure.postHit);
        postMine = structure.postMine == Pointer.NULL ? null : Function.getFunction(structure.postMine);
        isSuitableFor = structure.isSuitableFor == Pointer.NULL ? null : Function.getFunction(structure.isSuitableFor);
        useOnEntity = structure.useOnEntity == Pointer.NULL ? null : Function.getFunction(structure.useOnEntity);
        getName = structure.getName == Pointer.NULL ? null : Function.getFunction(structure.getName);
        getOrCreateTranslationKey = structure.getOrCreateTranslationKey == Pointer.NULL ? null : Function.getFunction(structure.getOrCreateTranslationKey);
        getTranslationKey = structure.getTranslationKey == Pointer.NULL ? null : Function.getFunction(structure.getTranslationKey);
        getTranslationKey_ItemStack = structure.getTranslationKey_ItemStack == Pointer.NULL ? null : Function.getFunction(structure.getTranslationKey_ItemStack);
        isNbtSynced = structure.isNbtSynced == Pointer.NULL ? null : Function.getFunction(structure.isNbtSynced);
        hasRecipeRemainder = structure.hasRecipeRemainder == Pointer.NULL ? null : Function.getFunction(structure.hasRecipeRemainder);
        inventoryTick = structure.inventoryTick == Pointer.NULL ? null : Function.getFunction(structure.inventoryTick);
        onCraftByPlayer = structure.onCraftByPlayer == Pointer.NULL ? null : Function.getFunction(structure.onCraftByPlayer);
        onCraft = structure.onCraft == Pointer.NULL ? null : Function.getFunction(structure.onCraft);
        isNetworkSynced = structure.isNetworkSynced == Pointer.NULL ? null : Function.getFunction(structure.isNetworkSynced);
        getUseAction = structure.getUseAction == Pointer.NULL ? null : Function.getFunction(structure.getUseAction);
        getMaxUseTime = structure.getMaxUseTime == Pointer.NULL ? null : Function.getFunction(structure.getMaxUseTime);
        onStoppedUsing = structure.onStoppedUsing == Pointer.NULL ? null : Function.getFunction(structure.onStoppedUsing);
        appendTooltip = structure.appendTooltip == Pointer.NULL ? null : Function.getFunction(structure.appendTooltip);
        getTooltipData = structure.getTooltipData == Pointer.NULL ? null : Function.getFunction(structure.getTooltipData);
        getName_ItemStack = structure.getName_ItemStack == Pointer.NULL ? null : Function.getFunction(structure.getName_ItemStack);
        hasGlint = structure.hasGlint == Pointer.NULL ? null : Function.getFunction(structure.hasGlint);
        getRarity = structure.getRarity == Pointer.NULL ? null : Function.getFunction(structure.getRarity);
        isEnchantable = structure.isEnchantable == Pointer.NULL ? null : Function.getFunction(structure.isEnchantable);
        getEnchantability = structure.getEnchantability == Pointer.NULL ? null : Function.getFunction(structure.getEnchantability);
        canRepair = structure.canRepair == Pointer.NULL ? null : Function.getFunction(structure.canRepair);
        getAttributeModifiers = structure.getAttributeModifiers == Pointer.NULL ? null : Function.getFunction(structure.getAttributeModifiers);
        isUsedOnRelease = structure.isUsedOnRelease == Pointer.NULL ? null : Function.getFunction(structure.isUsedOnRelease);
        getDefaultStack = structure.getDefaultStack == Pointer.NULL ? null : Function.getFunction(structure.getDefaultStack);
        isFood = structure.isFood == Pointer.NULL ? null : Function.getFunction(structure.isFood);
        getFoodComponent = structure.getFoodComponent == Pointer.NULL ? null : Function.getFunction(structure.getFoodComponent);
        getDrinkSound = structure.getDrinkSound == Pointer.NULL ? null : Function.getFunction(structure.getDrinkSound);
        getEatSound = structure.getEatSound == Pointer.NULL ? null : Function.getFunction(structure.getEatSound);
        isFireproof = structure.isFireproof == Pointer.NULL ? null : Function.getFunction(structure.isFireproof);
        damage = structure.damage == Pointer.NULL ? null : Function.getFunction(structure.damage);
        canBeNested = structure.canBeNested == Pointer.NULL ? null : Function.getFunction(structure.canBeNested);
    }

    private final Function asItem;
    private final Function getRequiredFeatures;
    private final Function usageTick;
    private final Function onItemEntityDestroyed;
    private final Function postProcessNbt;
    private final Function canMine;
    private final Function useOnBlock;
    private final Function getMiningSpeedMultiplier;
    private final Function use;
    private final Function finishUsing;
    private final Function isDamageable;
    private final Function isItemBarVisible;
    private final Function getItemBarStep;
    private final Function getItemBarColor;
    private final Function onStackClicked;
    private final Function onClicked;
    private final Function postHit;
    private final Function postMine;
    private final Function isSuitableFor;
    private final Function useOnEntity;
    private final Function getName;
    private final Function getOrCreateTranslationKey;
    private final Function getTranslationKey;
    private final Function getTranslationKey_ItemStack;
    private final Function isNbtSynced;
    private final Function hasRecipeRemainder;
    private final Function inventoryTick;
    private final Function onCraftByPlayer;
    private final Function onCraft;
    private final Function isNetworkSynced;
    private final Function getUseAction;
    private final Function getMaxUseTime;
    private final Function onStoppedUsing;
    private final Function appendTooltip;
    private final Function getTooltipData;
    private final Function getName_ItemStack;
    private final Function hasGlint;
    private final Function getRarity;
    private final Function isEnchantable;
    private final Function getEnchantability;
    private final Function canRepair;
    private final Function getAttributeModifiers;
    private final Function isUsedOnRelease;
    private final Function getDefaultStack;
    private final Function isFood;
    private final Function getFoodComponent;
    private final Function getDrinkSound;
    private final Function getEatSound;
    private final Function isFireproof;
    private final Function damage;
    private final Function canBeNested;

    private static final List<String> fieldOrder = Arrays.stream(ItemStructure.class.getDeclaredFields()).map(Field::getName).toList();;

    // 血的教训，不要把这个结构类设为private
    public static class ItemStructure extends Structure {
        public ItemStructure() {}

        public ItemStructure(Pointer p) {
            super(p);
        }

        @Override
        protected List<String> getFieldOrder() {
            return fieldOrder;
        }

        public int id;
        public Pointer asItem;
        public Pointer getRequiredFeatures;
        public Pointer usageTick;
        public Pointer onItemEntityDestroyed;
        public Pointer postProcessNbt;
        public Pointer canMine;
        public Pointer useOnBlock;
        public Pointer getMiningSpeedMultiplier;
        public Pointer use;
        public Pointer finishUsing;
        public Pointer isDamageable;
        public Pointer isItemBarVisible;
        public Pointer getItemBarStep;
        public Pointer getItemBarColor;
        public Pointer onStackClicked;
        public Pointer onClicked;
        public Pointer postHit;
        public Pointer postMine;
        public Pointer isSuitableFor;
        public Pointer useOnEntity;
        public Pointer getName;
        public Pointer getOrCreateTranslationKey;
        public Pointer getTranslationKey;
        public Pointer getTranslationKey_ItemStack;
        public Pointer isNbtSynced;
        public Pointer hasRecipeRemainder;
        public Pointer inventoryTick;
        public Pointer onCraftByPlayer;
        public Pointer onCraft;
        public Pointer isNetworkSynced;
        public Pointer getUseAction;
        public Pointer getMaxUseTime;
        public Pointer onStoppedUsing;
        public Pointer appendTooltip;
        public Pointer getTooltipData;
        public Pointer getName_ItemStack;
        public Pointer hasGlint;
        public Pointer getRarity;
        public Pointer isEnchantable;
        public Pointer getEnchantability;
        public Pointer canRepair;
        public Pointer getAttributeModifiers;
        public Pointer isUsedOnRelease;
        public Pointer getDefaultStack;
        public Pointer isFood;
        public Pointer getFoodComponent;
        public Pointer getDrinkSound;
        public Pointer getEatSound;
        public Pointer isFireproof;
        public Pointer damage;
        public Pointer canBeNested;
    }

    private static Function onFinalize = null;

    private static void setOnFinalize(long handle) {
        onFinalize = Function.getFunction(new Pointer(handle));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (usageTick == null) super.usageTick(world, user, stack, remainingUseTicks);
        else usageTick.invoke(Object.class, new Object[] { world, user, stack, remainingUseTicks }, LoadedLibraries.options );
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (onItemEntityDestroyed == null) super.onItemEntityDestroyed(entity);
        else onItemEntityDestroyed.invoke(Object.class, new Object[] { entity }, LoadedLibraries.options );
    }

    @Override
    public void postProcessNbt(NbtCompound nbt) {
        if (postProcessNbt == null) super.postProcessNbt(nbt);
        else postProcessNbt.invoke(Object.class, new Object[] { nbt }, LoadedLibraries.options);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (canMine == null) return super.canMine(state, world, pos, miner);
        else return (Boolean) canMine.invoke(Boolean.class, new Object[] { state, world, pos, miner }, LoadedLibraries.options);
    }

    @Override
    public Item asItem() {
        if (asItem == null) return super.asItem();
        else return (Item) asItem.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (useOnBlock == null) return super.useOnBlock(context);
        else return (ActionResult) useOnBlock.invoke(Object.class, new Object[] { context }, LoadedLibraries.options);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (getMiningSpeedMultiplier == null) return super.getMiningSpeedMultiplier(stack, state);
        else return (Float) getMiningSpeedMultiplier.invoke(Float.class, new Object[] { stack, state }, LoadedLibraries.options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (use == null) return super.use(world, user, hand);
        else return (TypedActionResult<ItemStack>) use.invoke(Object.class, new Object[] { world, user, hand }, LoadedLibraries.options);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (finishUsing == null) return super.finishUsing(stack, world, user);
        else return (ItemStack) finishUsing.invoke(Object.class, new Object[] { stack, world, user }, LoadedLibraries.options);
    }

    @Override
    public boolean isDamageable() {
        if (isDamageable == null) return super.isDamageable();
        else return (Boolean) isDamageable.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (isItemBarVisible == null) return super.isItemBarVisible(stack);
        else return (Boolean) isItemBarVisible.invoke(Boolean.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (getItemBarStep == null) return super.getItemBarStep(stack);
        else return (Integer) getItemBarStep.invoke(Integer.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (getItemBarColor == null) return super.getItemBarColor(stack);
        else return (Integer) getItemBarColor.invoke(Integer.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (onStackClicked == null) return super.onStackClicked(stack, slot, clickType, player);
        else return (Boolean) onStackClicked.invoke(Boolean.class, new Object[] { stack, slot, clickType, player }, LoadedLibraries.options);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (onClicked == null) return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
        else return (Boolean) onClicked.invoke(Boolean.class, new Object[] { stack, otherStack, slot, clickType, player, cursorStackReference }, LoadedLibraries.options);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (postHit == null) return super.postHit(stack, target, attacker);
        else return (Boolean) postHit.invoke(Boolean.class, new Object[] { stack, target, attacker }, LoadedLibraries.options);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (postMine == null) return super.postMine(stack, world, state, pos, miner);
        else return (Boolean) postMine.invoke(Boolean.class, new Object[] { stack, world, state, pos, miner }, LoadedLibraries.options);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        if (isSuitableFor == null) return super.isSuitableFor(state);
        else return (Boolean) isSuitableFor.invoke(Boolean.class, new Object[] { state }, LoadedLibraries.options);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (useOnEntity == null) return super.useOnEntity(stack, user, entity, hand);
        else return (ActionResult) useOnEntity.invoke(Object.class, new Object[] { stack, user, entity, hand }, LoadedLibraries.options);
    }

    @Override
    public Text getName() {
        if (getName == null) return super.getName();
        else return (Text) getName.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    protected String getOrCreateTranslationKey() {
        if (getOrCreateTranslationKey == null) return super.getOrCreateTranslationKey();
        else return (String) getOrCreateTranslationKey.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public String getTranslationKey() {
        if (getTranslationKey == null) return super.getTranslationKey();
        else return (String) getTranslationKey.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (getTranslationKey_ItemStack == null) return super.getTranslationKey(stack);
        else return (String) getTranslationKey_ItemStack.invoke(Object.class, new Object[] { stack }, LoadedLibraries.options );
    }

    @Override
    public boolean isNbtSynced() {
        if (isNbtSynced == null) return super.isNbtSynced();
        else return (Boolean) isNbtSynced.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean hasRecipeRemainder() {
        if (hasRecipeRemainder == null) return super.hasRecipeRemainder();
        else return (Boolean) hasRecipeRemainder.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (inventoryTick == null) super.inventoryTick(stack, world, entity, slot, selected);
        else inventoryTick.invoke(Object.class, new Object[] { stack, world, entity, slot, selected }, LoadedLibraries.options);
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        if (onCraftByPlayer == null) super.onCraftByPlayer(stack, world, player);
        else onCraftByPlayer.invoke(Object.class, new Object[] { stack, world, player }, LoadedLibraries.options);
    }

    @Override
    public void onCraft(ItemStack stack, World world) {
        if (onCraft == null) super.onCraft(stack, world);
        else onCraft.invoke(Object.class, new Object[] { stack, world }, LoadedLibraries.options);
    }

    @Override
    public boolean isNetworkSynced() {
        if (isNetworkSynced == null) return super.isNetworkSynced();
        else return (Boolean) isNetworkSynced.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (getUseAction == null) return super.getUseAction(stack);
        else return (UseAction) getUseAction.invoke(Object.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (getMaxUseTime == null) return super.getMaxUseTime(stack);
        else return (Integer) getMaxUseTime.invoke(Integer.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (onStoppedUsing == null) super.onStoppedUsing(stack, world, user, remainingUseTicks);
        else onStoppedUsing.invoke(Object.class, new Object[] { stack, world, user, remainingUseTicks }, LoadedLibraries.options);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (appendTooltip == null) super.appendTooltip(stack, world, tooltip, context);
        else appendTooltip.invoke(Object.class, new Object[] { stack, world, tooltip, context }, LoadedLibraries.options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (getTooltipData == null) return super.getTooltipData(stack);
        else return (Optional<TooltipData>) getTooltipData.invoke(Object.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (getName_ItemStack == null) return super.getName(stack);
        else return (Text) getName_ItemStack.invoke(Object.class, new Object[] { stack }, LoadedLibraries.options );
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (hasGlint == null) return super.hasGlint(stack);
        else return (Boolean) hasGlint.invoke(Boolean.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        if (getRarity == null) return super.getRarity(stack);
        else return (Rarity) getRarity.invoke(Object.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        if (isEnchantable == null) return super.isEnchantable(stack);
        else return (Boolean) isEnchantable.invoke(Boolean.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public int getEnchantability() {
        if (getEnchantability == null) return super.getEnchantability();
        else return (Integer) getEnchantability.invoke(Integer.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (canRepair == null) return super.canRepair(stack, ingredient);
        else return (Boolean) canRepair.invoke(Boolean.class, new Object[] { stack, ingredient }, LoadedLibraries.options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (getAttributeModifiers == null) return super.getAttributeModifiers(slot);
        else return (Multimap<EntityAttribute, EntityAttributeModifier>) getAttributeModifiers.invoke(Object.class, new Object[] { slot }, LoadedLibraries.options);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        if (isUsedOnRelease == null) return super.isUsedOnRelease(stack);
        else return (Boolean) isUsedOnRelease.invoke(Boolean.class, new Object[] { stack }, LoadedLibraries.options);
    }

    @Override
    public ItemStack getDefaultStack() {
        if (getDefaultStack == null) return super.getDefaultStack();
        else return (ItemStack) getDefaultStack.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean isFood() {
        if (isFood == null) return super.isFood();
        else return (Boolean) isFood.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Nullable
    @Override
    public FoodComponent getFoodComponent() {
        if (getFoodComponent == null) return super.getFoodComponent();
        else return (FoodComponent) getFoodComponent.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public SoundEvent getDrinkSound() {
        if (getDrinkSound == null) return super.getDrinkSound();
        else return (SoundEvent) getDrinkSound.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public SoundEvent getEatSound() {
        if (getEatSound == null) return super.getEatSound();
        else return (SoundEvent) getEatSound.invoke(Object.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean isFireproof() {
        if (isFireproof == null) return super.isFireproof();
        else return (Boolean) isFireproof.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public boolean damage(DamageSource source) {
        if (damage == null) return super.damage(source);
        else return (Boolean) damage.invoke(Boolean.class, new Object[] { source }, LoadedLibraries.options);
    }

    @Override
    public boolean canBeNested() {
        if (canBeNested == null) return super.canBeNested();
        else return (Boolean) canBeNested.invoke(Boolean.class, null, LoadedLibraries.options);
    }

    @Override
    public FeatureSet getRequiredFeatures() {
        if (getRequiredFeatures == null) return super.getRequiredFeatures();
        else return (FeatureSet) getRequiredFeatures.invoke(Object.class, null, LoadedLibraries.options);
    }
}
