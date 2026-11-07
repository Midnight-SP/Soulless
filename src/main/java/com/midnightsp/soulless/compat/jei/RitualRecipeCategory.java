package com.midnightsp.soulless.compat.jei;

import com.midnightsp.soulless.SoullessMod;
import com.midnightsp.soulless.recipe.RitualRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RitualRecipeCategory implements IRecipeCategory<RitualRecipe> {
    public static final RecipeType<RitualRecipe> RECIPE_TYPE = RecipeType.create(SoullessMod.MODID, "ritual", RitualRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;

    public RitualRecipeCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei.soulless.ritual");
        this.background = helper.createBlankDrawable(120, 36);
        this.icon = helper.createDrawableItemStack(new ItemStack(SoullessMod.RITUAL_BLADE.get()));
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<RitualRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.slot.draw(guiGraphics, 8, 9);
        this.slot.draw(guiGraphics, 40, 9);
        this.slot.draw(guiGraphics, 88, 9);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 10)
            .addIngredients(recipe.getCatalyst());

        builder.addSlot(RecipeIngredientRole.CATALYST, 41, 10)
            .addItemStacks(List.of(
                new ItemStack(SoullessMod.RITUAL_BLADE.get()),
                new ItemStack(SoullessMod.INJECTION_NEEDLE.get())
            ));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 10)
            .addItemStack(recipe.getResult());
    }
}

