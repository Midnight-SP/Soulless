package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BloodOrbItem extends Item {
    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
        .nutrition(1)
        .saturationModifier(1.0F)
        .alwaysEdible()
        .build();

    public BloodOrbItem(Properties properties) {
        super(properties.food(FOOD_PROPERTIES).stacksTo(16));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(
                MobEffects.HEAL,
                1,
                1,
                false,
                true,
                true
            ));

            if (entity instanceof Player player) {
                CompoundTag tag = player.getPersistentData();
                int consumed = tag.getInt("SoullessBloodConsumed") + 1;
                tag.putInt("SoullessBloodConsumed", consumed);

                if (consumed >= 32) {
                    player.addEffect(new MobEffectInstance(
                        SoullessMod.VAMPIRISM_EFFECT,
                        MobEffectInstance.INFINITE_DURATION,
                        0,
                        false,
                        false,
                        true
                    ));

                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        SoullessMod.grantAdvancement(serverPlayer, "draculas_curse");
                    }

                    if (consumed == 32) {
                        player.getPersistentData().putBoolean("SoullessAwakeningLoreHandled", true);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.7F, 0.6F);
                        SoullessMod.sendVampireAwakeningLore(player);
                    }
                }
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
