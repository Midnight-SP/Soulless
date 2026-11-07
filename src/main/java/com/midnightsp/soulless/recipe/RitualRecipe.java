package com.midnightsp.soulless.recipe;

import com.midnightsp.soulless.SoullessMod;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class RitualRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient catalyst;
    private final ItemStack result;

    public RitualRecipe(Ingredient catalyst, ItemStack result) {
        this.catalyst = catalyst;
        this.result = result;
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return this.catalyst.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SoullessMod.RITUAL_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SoullessMod.RITUAL_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("catalyst").forGetter(RitualRecipe::getCatalyst),
            ItemStack.CODEC.fieldOf("result").forGetter(RitualRecipe::getResult)
        ).apply(inst, RitualRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, RitualRecipe::getCatalyst,
            ItemStack.STREAM_CODEC, RitualRecipe::getResult,
            RitualRecipe::new
        );

        @Override
        public MapCodec<RitualRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

