package com.midnightsp.soulless.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.midnightsp.soulless.util.SoulBurstUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import com.midnightsp.soulless.SoullessMod;

public class RipPrimedTntEntity extends PrimedTnt {
    @Nullable
    private UUID detonatorUuid;

    public RipPrimedTntEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    public RipPrimedTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        super(SoullessEntities.RIP_PRIMED.get(), level);
        this.setPos(x, y, z);
        BlockState ripState = SoullessMod.RIP.get().defaultBlockState();
        this.setBlockState(ripState);
        if (owner != null) {
            this.detonatorUuid = owner.getUUID();
        }
    }

    @Override
    protected void explode() {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 12.5F, 1.0F);
            SoulBurstUtil.apply(serverLevel, this.position(), 50, 300, 600, true, this, this.getOwner());
            awardDetonationAdvancement(serverLevel);
        }
    }

    private void awardDetonationAdvancement(ServerLevel serverLevel) {
        if (this.detonatorUuid == null) {
            return;
        }

        ServerPlayer player = serverLevel.getServer().getPlayerList().getPlayer(this.detonatorUuid);
        if (player == null) {
            return;
        }

        var advancement = player.server.getAdvancements().get(ResourceLocation.fromNamespaceAndPath("soulless", "the_ghostlands"));
        if (advancement == null) {
            return;
        }

        var progress = player.getAdvancements().getOrStartProgress(advancement);
        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.detonatorUuid != null) {
            tag.putUUID("Detonator", this.detonatorUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Detonator")) {
            this.detonatorUuid = tag.getUUID("Detonator");
        }
    }
}
