package com.midnightsp.soulless.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;

import com.midnightsp.soulless.SoullessMod;

public class GhostOrbEntity extends Projectile implements ItemSupplier {
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(GhostOrbEntity.class, EntityDataSerializers.BYTE);

    public GhostOrbEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public GhostOrbEntity(Level level, LivingEntity thrower) {
        super(SoullessEntities.GHOST_ORB.get(), level);
        this.setOwner(thrower);
        this.setNoGravity(true);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ID_FLAGS, (byte) 0);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(SoullessMod.GHOST_ORB.get());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Vec3 movement = this.getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, target -> target != this.getOwner());

        if (hitResult.getType() != HitResult.Type.MISS) {
            onHit(hitResult);
        }

        this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
    }

    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = this.position();
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;

            // Apply effects to all entities in 5 block radius
            serverLevel.getEntities(this, this.getBoundingBox().inflate(5))
                .stream()
                .filter(entity -> entity instanceof LivingEntity && entity != this.getOwner())
                .forEach(entity -> {
                    LivingEntity living = (LivingEntity) entity;
                    living.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 1, false, true));
                    living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 0, false, true));
                    living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0, false, true));
                });

            transformNearbyBlocks(serverLevel, BlockPos.containing(pos));

            // Spawn particles in 5 block radius
            for (int i = 0; i < 30; i++) {
                double offsetX = (serverLevel.random.nextDouble() - 0.5) * 10.0;
                double offsetY = (serverLevel.random.nextDouble() - 0.5) * 10.0;
                double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 10.0;
                serverLevel.sendParticles(ParticleTypes.SOUL, x + offsetX, y + offsetY, z + offsetZ, 1, 0.0, 0.0, 0.0, 0.1);
            }
        }

        this.discard();
    }

    private void transformNearbyBlocks(ServerLevel serverLevel, BlockPos centerPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        float dirtResistance = Blocks.DIRT.defaultBlockState().getDestroySpeed(serverLevel, centerPos);
        float stoneResistance = Blocks.STONE.defaultBlockState().getDestroySpeed(serverLevel, centerPos);

        for (int dx = -5; dx <= 5; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -5; dz <= 5; dz++) {
                    if ((dx * dx) + (dy * dy) + (dz * dz) > 25) {
                        continue;
                    }

                    mutablePos.set(centerPos.getX() + dx, centerPos.getY() + dy, centerPos.getZ() + dz);
                    BlockState blockState = serverLevel.getBlockState(mutablePos);

                    if (blockState.isAir() || !blockState.getFluidState().isEmpty()) {
                        continue;
                    }

                    if (blockState.is(Blocks.SOUL_SAND) || blockState.is(Blocks.SOUL_SOIL)) {
                        continue;
                    }

                    float blockResistance = blockState.getDestroySpeed(serverLevel, mutablePos);

                    if (blockResistance < 0.0F) {
                        continue;
                    }

                    if (blockResistance <= dirtResistance) {
                        serverLevel.setBlockAndUpdate(mutablePos, Blocks.SOUL_SAND.defaultBlockState());
                    } else if (blockResistance <= stoneResistance) {
                        serverLevel.setBlockAndUpdate(mutablePos, Blocks.SOUL_SOIL.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        onHit(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        onHit(result);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}
