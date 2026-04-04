package com.midnightsp.soulless.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class SoulBurstUtil {
    private SoulBurstUtil() {
    }

    public static void apply(ServerLevel level, Vec3 center, int radius, int particleCount, int effectDurationTicks, boolean includeOwner, Entity source, Entity owner) {
        applyEffects(level, center, radius, effectDurationTicks, includeOwner, source, owner);
        transformNearbyBlocks(level, BlockPos.containing(center), radius);
        spawnParticles(level, center, radius, particleCount);
    }

    private static void applyEffects(ServerLevel level, Vec3 center, int radius, int effectDurationTicks, boolean includeOwner, Entity source, Entity owner) {
        AABB area = new AABB(center.x - radius, center.y - radius, center.z - radius, center.x + radius, center.y + radius, center.z + radius);

        level.getEntities(source, area)
            .stream()
            .filter(entity -> entity instanceof LivingEntity && (includeOwner || entity != owner))
            .map(entity -> (LivingEntity) entity)
            .forEach(living -> {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, effectDurationTicks, 1, false, true));
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, effectDurationTicks, 0, false, true));
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING, effectDurationTicks, 0, false, true));
            });
    }

    private static void transformNearbyBlocks(ServerLevel level, BlockPos centerPos, int radius) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        float witherRoseResistance = Blocks.WITHER_ROSE.defaultBlockState().getDestroySpeed(level, centerPos);
        float dirtResistance = Blocks.DIRT.defaultBlockState().getDestroySpeed(level, centerPos);
        float stoneResistance = Blocks.STONE.defaultBlockState().getDestroySpeed(level, centerPos);
        float logResistance = Blocks.OAK_LOG.defaultBlockState().getDestroySpeed(level, centerPos);
        int radiusSq = radius * radius;
        List<BlockPos> witherRoseCandidates = new ArrayList<>();
        List<BlockPos> soulSandCandidates = new ArrayList<>();
        List<BlockPos> soulSoilCandidates = new ArrayList<>();
        List<BlockPos> boneBlockCandidates = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if ((dx * dx) + (dy * dy) + (dz * dz) > radiusSq) {
                        continue;
                    }

                    mutablePos.set(centerPos.getX() + dx, centerPos.getY() + dy, centerPos.getZ() + dz);
                    BlockState blockState = level.getBlockState(mutablePos);

                    if (blockState.isAir() || !blockState.getFluidState().isEmpty()) {
                        continue;
                    }

                    if (blockState.is(Blocks.WITHER_ROSE) || blockState.is(Blocks.SOUL_SAND) || blockState.is(Blocks.SOUL_SOIL) || blockState.is(Blocks.BONE_BLOCK)) {
                        continue;
                    }

                    float blockResistance = blockState.getDestroySpeed(level, mutablePos);
                    if (blockResistance < 0.0F) {
                        continue;
                    }

                    if (blockResistance <= witherRoseResistance) {
                        witherRoseCandidates.add(mutablePos.immutable());
                    } else if (blockResistance <= dirtResistance) {
                        soulSandCandidates.add(mutablePos.immutable());
                    } else if (blockResistance <= stoneResistance) {
                        soulSoilCandidates.add(mutablePos.immutable());
                    } else if (blockResistance <= logResistance) {
                        boneBlockCandidates.add(mutablePos.immutable());
                    }
                }
            }
        }

        // Apply wither roses first so foliage is preserved before supporting blocks are transformed.
        for (BlockPos pos : witherRoseCandidates) {
            BlockState currentState = level.getBlockState(pos);
            if (currentState.isAir() || !currentState.getFluidState().isEmpty()) {
                continue;
            }

            if (currentState.is(Blocks.WITHER_ROSE) || currentState.is(Blocks.SOUL_SAND) || currentState.is(Blocks.SOUL_SOIL) || currentState.is(Blocks.BONE_BLOCK)) {
                continue;
            }

            if (Blocks.WITHER_ROSE.defaultBlockState().canSurvive(level, pos)) {
                level.setBlockAndUpdate(pos, Blocks.WITHER_ROSE.defaultBlockState());
            }
        }

        placeConvertedBlocks(level, soulSandCandidates, Blocks.SOUL_SAND.defaultBlockState());
        placeConvertedBlocks(level, soulSoilCandidates, Blocks.SOUL_SOIL.defaultBlockState());
        placeConvertedBlocks(level, boneBlockCandidates, Blocks.BONE_BLOCK.defaultBlockState());
    }

    private static void placeConvertedBlocks(ServerLevel level, List<BlockPos> candidates, BlockState replacement) {
        for (BlockPos pos : candidates) {
            BlockState currentState = level.getBlockState(pos);
            if (currentState.isAir() || !currentState.getFluidState().isEmpty()) {
                continue;
            }

            if (currentState.is(Blocks.WITHER_ROSE) || currentState.is(Blocks.SOUL_SAND) || currentState.is(Blocks.SOUL_SOIL) || currentState.is(Blocks.BONE_BLOCK)) {
                continue;
            }

            level.setBlockAndUpdate(pos, replacement);
        }
    }

    private static void spawnParticles(ServerLevel level, Vec3 center, int radius, int particleCount) {
        double diameter = radius * 2.0;
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * diameter;
            double offsetY = (level.random.nextDouble() - 0.5) * diameter;
            double offsetZ = (level.random.nextDouble() - 0.5) * diameter;
            level.sendParticles(ParticleTypes.SOUL, center.x + offsetX, center.y + offsetY, center.z + offsetZ, 1, 0.0, 0.0, 0.0, 0.1);
        }
    }
}
