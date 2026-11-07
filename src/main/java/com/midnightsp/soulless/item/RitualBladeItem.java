package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class RitualBladeItem extends SwordItem {
    public RitualBladeItem() {
        super(Tiers.WOOD, new Properties()
            .attributes(SwordItem.createAttributes(Tiers.WOOD, 1, -2.4F))
            .durability(0)
            .setNoRepair()
            .stacksTo(1)
        );
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide()) {
            if (player.getHealth() > 14.0F) {
                player.hurt(player.damageSources().magic(), 14.0F);

                player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST,
                    600,
                    1,
                    false,
                    true,
                    true
                ));
                
                player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    600,
                    1,
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
                        net.minecraft.world.item.alchemy.PotionContents contents = offhand.get(net.minecraft.core.component.DataComponents.POTION_CONTENTS);
                        if (contents != null && contents.is(net.minecraft.world.item.alchemy.Potions.WATER)) {
                            offhand.shrink(1);
                            ItemStack vampiricPotion = net.minecraft.world.item.alchemy.PotionContents.createItemStack(Items.POTION, SoullessMod.VAMPIRISM_POTION);
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
                
                ItemStack bloodtakerStack = new ItemStack(SoullessMod.BLOODTAKER_BLADE.get(), 1);
                player.setItemInHand(hand, bloodtakerStack);
                
                return InteractionResultHolder.success(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
        
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
