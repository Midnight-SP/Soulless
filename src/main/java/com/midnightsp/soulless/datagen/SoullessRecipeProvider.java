package com.midnightsp.soulless.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import com.midnightsp.soulless.SoullessMod;
import java.util.concurrent.CompletableFuture;

public class SoullessRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public SoullessRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Phantom Membrane: Lost Souls + Leather -> 2x Phantom Membrane
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PHANTOM_MEMBRANE, 2)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.LEATHER)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "phantom_membrane_from_lost_souls_and_leather"));

        // Leather: Lost Souls + Rotten Flesh -> 2x Leather
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LEATHER, 2)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.ROTTEN_FLESH)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "leather_from_lost_souls_and_rotten_flesh"));

        // Bone Block: Lost Souls + Bone -> 1x Bone Block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_BLOCK)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.BONE)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "bone_block_from_lost_souls_and_bone"));

        // Soul Sand: Lost Souls + Sand -> 1x Soul Sand
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.SOUL_SAND)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Blocks.SAND)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_sand_from_lost_souls_and_sand"));

        // Soul Soil: Lost Souls + Dirt -> 1x Soul Soil
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.SOUL_SOIL)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Blocks.DIRT)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_soil_from_lost_souls_and_dirt"));

        // Ghast Tear: 8x Lost Souls + Water Bucket -> 1x Ghast Tear
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GHAST_TEAR)
            .requires(SoullessMod.LOST_SOULS.get(), 8)
            .requires(Items.WATER_BUCKET)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghast_tear_from_lost_souls_and_water_bucket"));
    }
}
