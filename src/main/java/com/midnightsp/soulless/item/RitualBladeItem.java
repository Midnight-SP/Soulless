package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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