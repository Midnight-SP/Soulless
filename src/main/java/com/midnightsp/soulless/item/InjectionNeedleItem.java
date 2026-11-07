package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class InjectionNeedleItem extends Item {
    public InjectionNeedleItem(Properties properties) {
        super(properties.durability(4).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player.getHealth() > 6.0F) {
                player.hurt(player.damageSources().magic(), 6.0F);

                player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    300,
                    0,
                    false,
                    true,
                    true
                ));

                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    SoullessMod.grantAdvancement(serverPlayer, "bloody_ritual");
                }

                // Handle ritual transmutation with offhand item
                if (hand == InteractionHand.MAIN_HAND) {
                    ItemStack offhand = player.getOffhandItem();
                    if (offhand.is(Items.DIAMOND)) {
                        offhand.shrink(1);
                        ItemStack bloodDiamond = new ItemStack(SoullessMod.BLOOD_DIAMOND.get(), 1);
                        if (offhand.isEmpty()) {
                            player.setItemInHand(InteractionHand.OFF_HAND, bloodDiamond);
                        } else if (!player.getInventory().add(bloodDiamond)) {
                            player.drop(bloodDiamond, false);
                        }
                        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                            SoullessMod.grantAdvancement(serverPlayer, "heart_of_crimson");
                        }
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 0.7F);
                    } else if (offhand.is(Items.BOW)) {
                        offhand.shrink(1);
                        ItemStack draculasBow = new ItemStack(SoullessMod.DRACULAS_BOW.get(), 1);
                        if (offhand.isEmpty()) {
                            player.setItemInHand(InteractionHand.OFF_HAND, draculasBow);
                        } else if (!player.getInventory().add(draculasBow)) {
                            player.drop(draculasBow, false);
                        }
                        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                            SoullessMod.grantAdvancement(serverPlayer, "heart_of_crimson");
                        }
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.8F, 1.4F);
                    } else if (offhand.is(Items.POTION)) {
                        PotionContents contents = offhand.get(DataComponents.POTION_CONTENTS);
                        if (contents != null && contents.is(Potions.WATER)) {
                            offhand.shrink(1);
                            ItemStack vampiricPotion = PotionContents.createItemStack(Items.POTION, SoullessMod.VAMPIRISM_POTION);
                            if (offhand.isEmpty()) {
                                player.setItemInHand(InteractionHand.OFF_HAND, vampiricPotion);
                            } else if (!player.getInventory().add(vampiricPotion)) {
                                player.drop(vampiricPotion, false);
                            }
                            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                                SoullessMod.grantAdvancement(serverPlayer, "heart_of_crimson");
                            }
                            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0F, 1.2F);
                        }
                    }
                }

                player.getInventory().add(new ItemStack(SoullessMod.BLOOD_ORB.get(), 1));

                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEE_STING, SoundSource.PLAYERS, 0.9F, 1.3F);

                return InteractionResultHolder.success(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}

