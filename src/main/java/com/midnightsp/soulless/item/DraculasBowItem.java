package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DraculasBowItem extends BowItem {
    public DraculasBowItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, LivingEntity target) {
        boolean hasPendant = SoullessMod.hasDraculasPendant(shooter);

        if (hasPendant) {
            projectile.addTag("draculas_arrow_empowered");
            super.shootProjectile(shooter, projectile, index, velocity * 1.25F, inaccuracy, angle, target);
        } else {
            projectile.addTag("draculas_arrow_cursed");
            if (!shooter.level().isClientSide()) {
                shooter.hurt(shooter.damageSources().magic(), 2.0F);
                shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                    SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0F, 0.7F);
                if (shooter.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SMOKE,
                        shooter.getX(), shooter.getY() + 1.0D, shooter.getZ(),
                        10, 0.2D, 0.2D, 0.2D, 0.02D
                    );
                }
            }
            super.shootProjectile(shooter, projectile, index, velocity * 0.75F, inaccuracy * 1.5F, angle, target);
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(SoullessMod.BLOOD_DIAMOND.get()) || repair.is(Items.STRING) || super.isValidRepairItem(toRepair, repair);
    }
}

