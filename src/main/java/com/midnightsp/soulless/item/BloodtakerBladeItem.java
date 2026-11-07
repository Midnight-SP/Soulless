package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;

public class BloodtakerBladeItem extends SwordItem {
    public BloodtakerBladeItem() {
        super(Tiers.NETHERITE, new Properties()
            .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 4, -2.4F))
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
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean canHarvest = !target.getType().is(EntityTypeTags.UNDEAD) || SoullessMod.hasDraculasPendant(attacker);
        if (canHarvest) {
            Level level = target.level();
            if (!level.isClientSide() && attacker instanceof Player player) {
                for (int i = 0; i < 3; i++) {
                    ItemEntity orb = new ItemEntity(
                        level,
                        target.getX() + (level.random.nextDouble() - 0.5) * 0.5,
                        target.getY() + 0.5,
                        target.getZ() + (level.random.nextDouble() - 0.5) * 0.5,
                        new ItemStack(SoullessMod.BLOOD_ORB.get(), 1)
                    );
                    orb.setPickUpDelay(10);
                    level.addFreshEntity(orb);
                }
                
                ItemStack ritualStack = new ItemStack(SoullessMod.RITUAL_BLADE.get(), 1);
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack invStack = player.getInventory().getItem(i);
                    if (invStack == stack) {
                        player.getInventory().setItem(i, ritualStack);
                        break;
                    }
                }
            }
        }
        
        return super.hurtEnemy(stack, target, attacker);
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