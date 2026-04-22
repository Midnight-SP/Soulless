package com.midnightsp.soulless.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.midnightsp.soulless.SoullessMod;

public class FireballEntity extends LargeFireball {
    public FireballEntity(EntityType<? extends LargeFireball> type, Level level) {
        super(type, level);
    }

    public FireballEntity(Level level, LivingEntity thrower) {
        super(level, thrower, thrower.getLookAngle(), 1);
        this.setOwner(thrower);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        // Custom explosion logic can be added here if needed
    }
}
