package com.midnightsp.soulless.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ReturnTokenItem extends Item {
    private static final int COOLDOWN_TICKS = 20 * 60 * 15;

    public ReturnTokenItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(itemStack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(itemStack);
        }

        ServerLevel targetLevel = serverPlayer.serverLevel();
        BlockPos targetPos = serverPlayer.getRespawnPosition();

        if (targetPos == null) {
            targetLevel = serverPlayer.server.overworld();
            targetPos = targetLevel.getSharedSpawnPos();
        } else if (serverPlayer.getRespawnDimension() != null) {
            ServerLevel respawnLevel = serverPlayer.server.getLevel(serverPlayer.getRespawnDimension());
            if (respawnLevel != null) {
                targetLevel = respawnLevel;
            }
        }

        if (targetPos == null) {
            targetLevel = serverPlayer.server.overworld();
            targetPos = targetLevel.getSharedSpawnPos();
        }

        if (targetPos == null) {
            return InteractionResultHolder.fail(itemStack);
        }

        serverPlayer.teleportTo(
            targetLevel,
            targetPos.getX() + 0.5D,
            targetPos.getY(),
            targetPos.getZ() + 0.5D,
            serverPlayer.getYRot(),
            serverPlayer.getXRot()
        );
        serverPlayer.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        com.midnightsp.soulless.SoullessMod.grantAdvancement(serverPlayer, "ticket_home");
        targetLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        return InteractionResultHolder.success(itemStack);
    }
}