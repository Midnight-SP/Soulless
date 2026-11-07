package com.midnightsp.soulless.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GhostHeartItem extends Item {
    public GhostHeartItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 900, 3)); // 45 seconds, +16 HP (8 hearts)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));  // 10 seconds of Regen II
            player.heal(16.0F);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL,
                    player.getX(), player.getY() + 1.0D, player.getZ(),
                    30, 0.5D, 0.5D, 0.5D, 0.05D
                );
            }

            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                com.midnightsp.soulless.SoullessMod.grantAdvancement(serverPlayer, "heart_of_the_spirit");
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.2F, 0.9F);
        }

        player.getCooldowns().addCooldown(this, 1200); // 60s cooldown

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
