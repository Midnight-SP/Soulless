package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum SoulsteelTier implements Tier {
    INSTANCE;

    @Override
    public int getUses() {
        return 1561;
    }

    @Override
    public float getSpeed() {
        return 8.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return 14;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(SoullessMod.SOULSTEEL_INGOT.get());
    }
}

