package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class DraculasPendantItem extends Item {
    public DraculasPendantItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(SoullessMod.VAMPIRISM_EFFECT, 40, 0, false, false, true));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player.getHealth() >= player.getMaxHealth()) {
                return InteractionResultHolder.fail(stack);
            }

            AABB searchArea = player.getBoundingBox().inflate(15.0D);
            List<LivingEntity> potentialTargets = new ArrayList<>(level.getEntitiesOfClass(LivingEntity.class, searchArea,
                e -> e != player && e.isAlive() && !e.isInvulnerable() && player.distanceToSqr(e) <= 225.0D));

            if (potentialTargets.isEmpty()) {
                return InteractionResultHolder.fail(stack);
            }

            int siphonHits = 0;
            float totalHealed = 0.0F;

            // Repeatedly deal 1 damage to a random entity until player is full or no enemies remain
            while (player.getHealth() < player.getMaxHealth() && !potentialTargets.isEmpty() && siphonHits < 200) {
                potentialTargets.removeIf(e -> !e.isAlive());
                if (potentialTargets.isEmpty()) {
                    break;
                }

                LivingEntity target = potentialTargets.get(level.random.nextInt(potentialTargets.size()));
                float prevHealth = target.getHealth();

                target.invulnerableTime = 0;
                boolean hurt = target.hurt(player.damageSources().indirectMagic(player, player), 1.0F);

                if (!hurt || target.getHealth() >= prevHealth) {
                    potentialTargets.remove(target);
                    continue;
                }

                float actualDamage = Math.max(0.0F, prevHealth - target.getHealth());
                if (actualDamage > 0.0F) {
                    player.heal(actualDamage);
                    SoullessMod.feedOnEnemy(player, actualDamage);
                    totalHealed += actualDamage;
                    siphonHits++;

                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SOUL,
                            target.getX(), target.getY() + target.getBbHeight() / 2.0D, target.getZ(),
                            2, 0.1D, 0.1D, 0.1D, 0.02D
                        );
                    }
                } else {
                    potentialTargets.remove(target);
                }
            }

            if (siphonHits > 0) {
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART,
                        player.getX(), player.getY() + player.getBbHeight() + 0.3D, player.getZ(),
                        Math.min(10, Math.max(2, (int) totalHealed)), 0.3D, 0.3D, 0.3D, 0.0D
                    );
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.2F, 1.4F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.8F, 1.6F);

                player.getCooldowns().addCooldown(this, 300); // 15 second cooldown
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
