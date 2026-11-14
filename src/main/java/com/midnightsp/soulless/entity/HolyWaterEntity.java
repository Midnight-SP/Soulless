package com.midnightsp.soulless.entity;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class HolyWaterEntity extends ThrowableItemProjectile {
    public HolyWaterEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public HolyWaterEntity(Level level, LivingEntity shooter) {
        super(SoullessEntities.HOLY_WATER.get(), shooter, level);
    }

    public HolyWaterEntity(Level level, double x, double y, double z) {
        super(SoullessEntities.HOLY_WATER.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return SoullessMod.HOLY_WATER.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            // Holy splash sound and radiant golden visual particles
            serverLevel.playSound(null, x, y, z, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.0F, 1.2F);
            serverLevel.playSound(null, x, y, z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.4F);

            // Radiant golden holy splash particles (no blue or green)
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 35, 0.5, 0.3, 0.5, 0.12);
            serverLevel.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.82F, 0.2F), 1.2F), x, y, z, 30, 0.4, 0.3, 0.4, 0.05);
            serverLevel.sendParticles(ParticleTypes.WAX_OFF, x, y, z, 15, 0.4, 0.3, 0.4, 0.05);

            double radius = 4.0;
            AABB area = new AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, area);

            for (LivingEntity entity : entities) {
                if (entity.distanceToSqr(x, y, z) <= radius * radius) {
                    boolean isUndead = entity.getType().is(EntityTypeTags.UNDEAD);
                    boolean isVampire = entity.hasEffect(SoullessMod.VAMPIRISM_EFFECT) || SoullessMod.hasDraculasPendant(entity);

                    if (isUndead) {
                        // Undead mobs: smite with holy magic damage and slowness
                        entity.hurt(serverLevel.damageSources().magic(), 8.0F);
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                        serverLevel.sendParticles(ParticleTypes.SMOKE, entity.getX(), entity.getY() + 0.5, entity.getZ(), 15, 0.2, 0.3, 0.2, 0.02);
                        serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 0.7F, 1.1F);
                    } else if (isVampire) {
                        if (SoullessMod.hasDraculasPendant(entity)) {
                            // Dracula's Pendant protects from being permanently cured or executed by holy water,
                            // but Holy Water sears their flesh and temporarily weakens them
                            entity.hurt(serverLevel.damageSources().magic(), 8.0F);
                            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.2, 0.3, 0.2, 0.02);
                            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.2, 0.3, 0.2, 0.05);
                            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 0.8F);
                            if (entity instanceof Player player) {
                                SoullessMod.sendVampirePendantProtectedLore(player);
                            }
                        } else {
                            // Afflicted vampire (drank 32 blood, potion, or infected living mob): PURIFY CURSE!
                            entity.hurt(serverLevel.damageSources().magic(), 6.0F);
                            entity.removeEffect(SoullessMod.VAMPIRISM_EFFECT);
                            entity.removeTag("soulless:vampire_mob");
                            if (this.getOwner() instanceof ServerPlayer thrower) {
                                SoullessMod.grantAdvancement(thrower, "bless_be_the_damned");
                            }
                            if (entity instanceof ServerPlayer curedPlayer) {
                                curedPlayer.getPersistentData().remove("SoullessBloodConsumed");
                                curedPlayer.getPersistentData().remove("SoullessContractedVampirism");
                                SoullessMod.sendVampireCuredLore(curedPlayer);
                                SoullessMod.grantAdvancement(curedPlayer, "bless_be_the_damned");
                            }
                            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY() + 1.0, entity.getZ(), 25, 0.3, 0.5, 0.3, 0.1);
                            serverLevel.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.85F, 0.25F), 1.0F), entity.getX(), entity.getY() + 1.0, entity.getZ(), 15, 0.3, 0.4, 0.3, 0.02);
                            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.8F, 1.5F);
                        }
                    } else {
                        // Living mortal ally: Cleanse harmful status effects (AoE milk effect)
                        List<MobEffectInstance> active = new ArrayList<>(entity.getActiveEffects());
                        boolean curedAny = false;
                        for (MobEffectInstance inst : active) {
                            if (inst.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                                entity.removeEffect(inst.getEffect());
                                curedAny = true;
                            }
                        }
                        if (curedAny) {
                            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, entity.getX(), entity.getY() + 1.0, entity.getZ(), 15, 0.3, 0.4, 0.3, 0.08);
                            serverLevel.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.85F, 0.25F), 1.0F), entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.3, 0.4, 0.3, 0.02);
                            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 1.4F);
                        }
                    }
                }
            }
            this.discard();
        }
    }
}
