package com.midnightsp.soulless.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PHANTOM_MEMBRANE, 2)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.LEATHER)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "phantom_membrane_from_lost_souls_and_leather"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LEATHER, 2)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.ROTTEN_FLESH)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "leather_from_lost_souls_and_rotten_flesh"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_BLOCK)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.BONE)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "bone_block_from_lost_souls_and_bone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.SOUL_SAND)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Blocks.SAND)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_sand_from_lost_souls_and_sand"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.SOUL_SOIL)
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Blocks.DIRT)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_soil_from_lost_souls_and_dirt"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GHAST_TEAR)
            .requires(SoullessMod.LOST_SOULS.get(), 8)
            .requires(Items.WATER_BUCKET)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghast_tear_from_lost_souls_and_water_bucket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.GHOSTPOWDER.get())
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(Items.SUGAR)
            .requires(Items.GUNPOWDER)
            .requires(Items.REDSTONE)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghostpowder_from_lost_souls_sugar_gunpowder_redstone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.SOULSTEEL_INGOT.get())
            .requires(Items.IRON_INGOT)
            .requires(Items.COAL, 2)
            .requires(SoullessMod.GHOSTPOWDER.get())
            .unlockedBy("has_ghostpowder", has(SoullessMod.GHOSTPOWDER.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_ingot_from_iron_coal_ghostpowder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SoullessMod.SOULSTEEL_BLOCK)
            .pattern("XXX")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_block_from_soulsteel_ingots"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.SOULSTEEL_INGOT.get(), 9)
            .requires(SoullessMod.SOULSTEEL_BLOCK)
            .unlockedBy("has_soulsteel_block", has(SoullessMod.SOULSTEEL_BLOCK))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_ingots_from_soulsteel_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.SOUL_REAPER.get())
            .pattern("XX")
            .pattern(" S")
            .pattern(" S")
            .define('X', SoullessMod.SOULSTEEL_INGOT.get())
            .define('S', Items.STICK)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_reaper"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.GHOST_ORB.get())
            .requires(SoullessMod.GHOSTPOWDER.get())
            .requires(Items.FIRE_CHARGE)
            .unlockedBy("has_ghostpowder", has(SoullessMod.GHOSTPOWDER.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghost_orb_from_ghostpowder_and_fire_charge"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SoullessMod.RIP.get())
            .pattern("TST")
            .pattern("SNS")
            .pattern("TST")
            .define('T', Blocks.TNT)
            .define('S', SoullessMod.SOULSTEEL_BLOCK.get())
            .define('N', Items.NETHER_STAR)
            .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "rip"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SoullessMod.BONEYARD.get())
            .pattern("BLB")
            .pattern("LDL")
            .pattern("BLB")
            .define('B', Items.BONE)
            .define('L', SoullessMod.LOST_SOULS.get())
            .define('D', Blocks.DIRT)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "boneyard"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SoullessMod.UNDEAD_CORE.get())
            .pattern("DOD")
            .pattern("OHO")
            .pattern("DOD")
            .define('D', Items.DIAMOND)
            .define('O', Blocks.OBSIDIAN)
            .define('H', SoullessMod.GHOST_HEART.get())
            .unlockedBy("has_ghost_heart", has(SoullessMod.GHOST_HEART.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "undead_core"));
    }
}
