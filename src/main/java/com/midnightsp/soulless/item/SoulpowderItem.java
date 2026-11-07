package com.midnightsp.soulless.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SoulpowderItem extends Item {
    public SoulpowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            AABB area = player.getBoundingBox().inflate(5.0D);
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e != player && e.isAlive() && player.distanceToSqr(e) <= 25.0D);

            for (LivingEntity target : targets) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            }

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL,
                    player.getX(), player.getY() + 0.5D, player.getZ(),
                    35, 1.2D, 0.6D, 1.2D, 0.05D
                );
                serverLevel.sendParticles(ParticleTypes.SQUID_INK,
                    player.getX(), player.getY() + 0.5D, player.getZ(),
                    25, 1.0D, 0.5D, 1.0D, 0.02D
                );
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.0F, 1.2F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SQUID_SQUIRT, SoundSource.PLAYERS, 0.8F, 0.8F);

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                com.midnightsp.soulless.SoullessMod.grantAdvancement(serverPlayer, "blinding_dust");
            }
        }

        player.getCooldowns().addCooldown(this, 20);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

