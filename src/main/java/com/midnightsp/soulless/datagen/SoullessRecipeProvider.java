package com.midnightsp.soulless.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GHAST_TEAR)
            .requires(SoullessMod.LOST_SOULS.get(), 8)
            .requires(Items.WATER_BUCKET)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghast_tear_from_lost_souls_and_water_bucket"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.GHOSTPOWDER.get())
            .requires(SoullessMod.LOST_SOULS.get())
            .requires(SoullessMod.SOULPOWDER.get())
            .requires(Items.GUNPOWDER)
            .requires(Items.REDSTONE)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghostpowder_from_lost_souls_soulpowder_gunpowder_redstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.BAT.get())
            .pattern("S")
            .pattern("S")
            .pattern("S")
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "bat"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.FIREBALL.get())
            .requires(Items.GHAST_TEAR)
            .requires(Items.FIRE_CHARGE)
            .unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "fireball"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.RITUAL_BLADE.get())
            .pattern(" S ")
            .pattern(" S ")
            .pattern(" A ")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('A', Items.STICK)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ritual_blade"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SoullessMod.GOLD_SOULS.get())
            .pattern(" L ")
            .pattern("LGL")
            .pattern(" L ")
            .define('L', SoullessMod.LOST_SOULS.get())
            .define('G', Items.GOLD_INGOT)
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "gold_souls"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GOLDEN_CARROT)
            .requires(Items.CARROT)
            .requires(SoullessMod.GOLD_SOULS.get())
            .unlockedBy("has_gold_souls", has(SoullessMod.GOLD_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "golden_carrot_from_gold_souls"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.GOLDEN_APPLE)
            .requires(Items.APPLE)
            .requires(SoullessMod.GOLD_SOULS.get(), 2)
            .unlockedBy("has_gold_souls", has(SoullessMod.GOLD_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "golden_apple_from_gold_souls"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SoullessMod.SOULPOWDER.get())
            .pattern("SL")
            .pattern("LS")
            .define('S', Blocks.SAND)
            .define('L', SoullessMod.LOST_SOULS.get())
            .unlockedBy("has_lost_souls", has(SoullessMod.LOST_SOULS.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulpowder_from_sand_and_lost_souls"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.SOUL_SAND)
            .pattern("PPP")
            .pattern("PPP")
            .pattern("PPP")
            .define('P', SoullessMod.SOULPOWDER.get())
            .unlockedBy("has_soulpowder", has(SoullessMod.SOULPOWDER.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_sand_from_soulpowder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.SOUL_SOIL, 4)
            .pattern("SS")
            .pattern("SS")
            .define('S', Blocks.SOUL_SAND)
            .unlockedBy("has_soul_sand", has(Blocks.SOUL_SAND))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soul_soil_from_soul_sand"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, SoullessMod.MONSTROUS_STEW.get())
            .requires(Items.BOWL)
            .requires(SoullessMod.BLOOD_ORB.get())
            .requires(Items.ROTTEN_FLESH)
            .requires(Items.FERMENTED_SPIDER_EYE)
            .unlockedBy("has_blood_orb", has(SoullessMod.BLOOD_ORB.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "monstrous_stew"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SoullessMod.RETURN_TOKEN.get())
            .pattern(" S ")
            .pattern("SXS")
            .pattern(" S ")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('X', Items.SKELETON_SKULL)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "return_token"));

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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.QUARTZ)
            .requires(Items.GLASS)
            .requires(SoullessMod.GHOSTPOWDER.get())
            .unlockedBy("has_ghostpowder", has(SoullessMod.GHOSTPOWDER.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "quartz_from_glass_and_ghostpowder"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.SKELETON_SKULL)
            .pattern("BBB")
            .pattern("BGB")
            .pattern("BBB")
            .define('B', Items.BONE)
            .define('G', SoullessMod.GHOSTPOWDER.get())
            .unlockedBy("has_ghostpowder", has(SoullessMod.GHOSTPOWDER.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "skeleton_skull_from_ghostpowder_and_bones"));

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

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SoullessMod.BLOOD_DIAMOND_BLOCK.get())
            .pattern("XXX")
            .pattern("XXX")
            .pattern("XXX")
            .define('X', SoullessMod.BLOOD_DIAMOND.get())
            .unlockedBy("has_blood_diamond", has(SoullessMod.BLOOD_DIAMOND.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "blood_diamond_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SoullessMod.BLOOD_DIAMOND.get(), 9)
            .requires(SoullessMod.BLOOD_DIAMOND_BLOCK.get())
            .unlockedBy("has_blood_diamond_block", has(SoullessMod.BLOOD_DIAMOND_BLOCK.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "blood_diamonds_from_blood_diamond_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.DRACULAS_BLADE.get())
            .pattern(" D ")
            .pattern(" D ")
            .pattern(" S ")
            .define('D', SoullessMod.BLOOD_DIAMOND.get())
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_blood_diamond", has(SoullessMod.BLOOD_DIAMOND.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "draculas_blade"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SoullessMod.GRAVEDIGGER.get())
            .pattern(" S ")
            .pattern(" A ")
            .pattern(" A ")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('A', Items.STICK)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "gravedigger"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SoullessMod.SOULBREAKER.get())
            .pattern("SSS")
            .pattern(" A ")
            .pattern(" A ")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('A', Items.STICK)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulbreaker"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.BEHEADER.get())
            .pattern("SS ")
            .pattern("SA ")
            .pattern(" A ")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('A', Items.STICK)
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "beheader"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SoullessMod.INJECTION_NEEDLE.get())
            .pattern("  I")
            .pattern(" G ")
            .pattern("S  ")
            .define('I', Items.IRON_NUGGET)
            .define('G', Items.GLASS_BOTTLE)
            .define('S', Items.IRON_INGOT)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "injection_needle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.SOULSTEEL_HELMET.get())
            .pattern("SSS")
            .pattern("S S")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.SOULSTEEL_CHESTPLATE.get())
            .pattern("S S")
            .pattern("SSS")
            .pattern("SSS")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_chestplate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.SOULSTEEL_LEGGINGS.get())
            .pattern("SSS")
            .pattern("S S")
            .pattern("S S")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.SOULSTEEL_BOOTS.get())
            .pattern("S S")
            .pattern("S S")
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .unlockedBy("has_soulsteel_ingot", has(SoullessMod.SOULSTEEL_INGOT.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "soulsteel_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SoullessMod.WOODEN_STAKE.get())
            .pattern(" S")
            .pattern("P ")
            .define('S', Items.STICK)
            .define('P', ItemTags.PLANKS)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "wooden_stake"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SoullessMod.DRACULAS_PENDANT.get())
            .pattern("DGD")
            .pattern("SHS")
            .pattern("DSD")
            .define('D', SoullessMod.BLOOD_DIAMOND.get())
            .define('G', SoullessMod.GOLD_SOULS.get())
            .define('S', SoullessMod.SOULSTEEL_INGOT.get())
            .define('H', SoullessMod.GHOST_HEART.get())
            .unlockedBy("has_ghost_heart", has(SoullessMod.GHOST_HEART.get()))
            .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "draculas_pendant"));
    }
}
