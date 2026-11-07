package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class WoodenStakeItem extends SwordItem {
    public WoodenStakeItem(Properties properties) {
        super(Tiers.WOOD, properties.durability(48).attributes(SwordItem.createAttributes(Tiers.WOOD, 2, -2.0F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean isVampireOrUndead = target.getType().is(EntityTypeTags.UNDEAD)
            || target.hasEffect(SoullessMod.VAMPIRISM_EFFECT)
            || SoullessMod.hasDraculasPendant(target);

        if (isVampireOrUndead) {
            attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 1.0F, 1.4F);

            if (attacker.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CRIT,
                    target.getX(), target.getY() + target.getBbHeight() / 2.0D, target.getZ(),
                    15, 0.3D, 0.3D, 0.3D, 0.1D
                );
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}

