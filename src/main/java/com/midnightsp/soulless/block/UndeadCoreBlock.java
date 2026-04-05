package com.midnightsp.soulless.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UndeadCoreBlock extends Block {
    private static final double RADIUS = 50.0D;
    private static final int PULSE_INTERVAL_TICKS = 40;

    public UndeadCoreBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, PULSE_INTERVAL_TICKS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        applyAura(level, pos);

        if (level.getBlockState(pos).is(this)) {
            level.scheduleTick(pos, this, PULSE_INTERVAL_TICKS);
        }
    }

    private void applyAura(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(RADIUS);
        List<LivingEntity> undeadInRange = level.getEntitiesOfClass(LivingEntity.class, area, target -> target.getType().is(EntityTypeTags.UNDEAD));

        if (undeadInRange.isEmpty()) {
            return;
        }

        level.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.45F, 0.75F);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, 24, 0.35D, 0.2D, 0.35D, 0.01D);

        for (LivingEntity entity : undeadInRange) {
            // Instant Health damages undead; reapply every pulse.
            entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0, true, true));
            level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY() + entity.getBbHeight() * 0.6D, entity.getZ(), 2, 0.25D, 0.2D, 0.25D, 0.0D);
        }
    }
}
