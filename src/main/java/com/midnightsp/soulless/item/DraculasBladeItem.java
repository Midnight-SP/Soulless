package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class DraculasBladeItem extends SwordItem {
    public DraculasBladeItem(Properties properties) {
        super(SoulsteelTier.INSTANCE, properties.attributes(SwordItem.createAttributes(SoulsteelTier.INSTANCE, 4, -2.4F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean hasPendant = SoullessMod.hasDraculasPendant(attacker);

        if (hasPendant) {
            // Empowered by Dracula's Pendant: strong 25% lifesteal (works on undead too)
            if (attacker instanceof Player player) {
                float healAmount = Math.max(1.5F, 9.0F * 0.25F);
                player.heal(healAmount);
                SoullessMod.feedOnEnemy(player, healAmount);
                if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                    SoullessMod.grantAdvancement(serverPlayer, "the_counts_blade");
                }

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART,
                        player.getX(), player.getY() + player.getBbHeight() + 0.2D, player.getZ(),
                        2, 0.2D, 0.2D, 0.2D, 0.0D
                    );
                    serverLevel.sendParticles(ParticleTypes.SOUL,
                        target.getX(), target.getY() + target.getBbHeight() / 2.0D, target.getZ(),
                        5, 0.2D, 0.2D, 0.2D, 0.05D
                    );
                }

                if (target.isDeadOrDying()) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0));
                }
            }
        } else {
            // Without pendant: weapon is cursed, deals backlash damage to wielder
            if (!attacker.level().isClientSide()) {
                attacker.hurt(attacker.damageSources().magic(), 2.0F);
                attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                    SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0F, 0.7F);
                if (attacker.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SMOKE,
                        attacker.getX(), attacker.getY() + 1.0D, attacker.getZ(),
                        10, 0.2D, 0.2D, 0.2D, 0.02D
                    );
                }
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(SoullessMod.BLOOD_DIAMOND.get()) || repair.is(SoullessMod.SOULSTEEL_INGOT.get()) || super.isValidRepairItem(toRepair, repair);
    }
}

