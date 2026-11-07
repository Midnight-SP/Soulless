package com.midnightsp.soulless.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MonstrousStewItem extends Item {
    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
        .nutrition(10)
        .saturationModifier(1.0F)
        .alwaysEdible()
        .build();

    public MonstrousStewItem(Properties properties) {
        super(properties.food(FOOD_PROPERTIES).stacksTo(1));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1, false, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0, false, true, true));
        }

        ItemStack consumed = super.finishUsingItem(stack, level, entity);

        if (entity instanceof net.minecraft.world.entity.player.Player player && !player.getAbilities().instabuild) {
            return new ItemStack(Items.BOWL);
        }

        return consumed;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }
}