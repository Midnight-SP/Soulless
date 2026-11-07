package com.midnightsp.soulless.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;

public class BatItem extends SwordItem {
    public BatItem(Item.Properties properties) {
        super(Tiers.WOOD, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Apply extra knockback
        target.knockback(3.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        if (target instanceof net.minecraft.world.entity.monster.Ghast && attacker instanceof net.minecraft.server.level.ServerPlayer player) {
            com.midnightsp.soulless.SoullessMod.grantAdvancement(player, "home_run");
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
