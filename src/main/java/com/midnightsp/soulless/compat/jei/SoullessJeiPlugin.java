package com.midnightsp.soulless.compat.jei;

import com.midnightsp.soulless.SoullessMod;
import com.midnightsp.soulless.recipe.RitualRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class SoullessJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RitualRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RitualRecipe> recipes = new ArrayList<>();
        recipes.add(new RitualRecipe(
            Ingredient.of(Items.DIAMOND),
            new ItemStack(SoullessMod.BLOOD_DIAMOND.get())
        ));
        recipes.add(new RitualRecipe(
            Ingredient.of(Items.BOW),
            new ItemStack(SoullessMod.DRACULAS_BOW.get())
        ));
        ItemStack waterBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
        ItemStack vampiricPotion = PotionContents.createItemStack(Items.POTION, SoullessMod.VAMPIRISM_POTION);
        recipes.add(new RitualRecipe(
            Ingredient.of(waterBottle),
            vampiricPotion
        ));

        registration.addRecipes(RitualRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SoullessMod.RITUAL_BLADE.get()), RitualRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(SoullessMod.INJECTION_NEEDLE.get()), RitualRecipeCategory.RECIPE_TYPE);
    }
}

