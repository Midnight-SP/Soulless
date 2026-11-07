package com.midnightsp.soulless.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class BeheaderItem extends AxeItem {
    public BeheaderItem(Properties properties) {
        super(SoulsteelTier.INSTANCE, properties.attributes(AxeItem.createAttributes(SoulsteelTier.INSTANCE, 6.0F, -3.0F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            boolean isCritical = player.fallDistance > 0.0F 
                && !player.onGround() 
                && !player.onClimbable() 
                && !player.isInWater() 
                && !player.hasEffect(MobEffects.BLINDNESS) 
                && !player.isPassenger();

            if (isCritical) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
                if (target.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT,
                        target.getX(), target.getY() + target.getBbHeight() / 2.0D, target.getZ(),
                        10, 0.2D, 0.2D, 0.2D, 0.1D
                    );
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}

