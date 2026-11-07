package com.midnightsp.soulless.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class SoulbreakerItem extends PickaxeItem {
    private static final ThreadLocal<Boolean> IS_GHOST_MINING = ThreadLocal.withInitial(() -> false);
    private static final int MAX_GHOST_MINE_COUNT = 4;

    public SoulbreakerItem(Properties properties) {
        super(SoulsteelTier.INSTANCE, properties.attributes(PickaxeItem.createAttributes(SoulsteelTier.INSTANCE, 1.0F, -2.8F)));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide() && !IS_GHOST_MINING.get() && entityLiving instanceof ServerPlayer player) {
            if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) && !player.isShiftKeyDown()) {
                triggerGhostMiners(stack, (ServerLevel) level, state, pos, player);
            }
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    private void triggerGhostMiners(ItemStack stack, ServerLevel level, BlockState targetState, BlockPos originPos, ServerPlayer player) {
        Block targetBlock = targetState.getBlock();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(originPos);
        visited.add(originPos);

        int minedCount = 0;
        IS_GHOST_MINING.set(true);
        try {
            while (!queue.isEmpty() && minedCount < MAX_GHOST_MINE_COUNT) {
                BlockPos current = queue.poll();

                for (BlockPos neighbor : BlockPos.betweenClosed(current.offset(-1, -1, -1), current.offset(1, 1, 1))) {
                    BlockPos immutableNeighbor = neighbor.immutable();
                    if (!visited.contains(immutableNeighbor)) {
                        visited.add(immutableNeighbor);
                        BlockState neighborState = level.getBlockState(immutableNeighbor);

                        if (neighborState.is(targetBlock)) {
                            level.destroyBlock(immutableNeighbor, true, player);
                            level.sendParticles(ParticleTypes.SOUL,
                                immutableNeighbor.getX() + 0.5D,
                                immutableNeighbor.getY() + 0.5D,
                                immutableNeighbor.getZ() + 0.5D,
                                6, 0.2D, 0.2D, 0.2D, 0.02D
                            );
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                            minedCount++;
                            queue.add(immutableNeighbor);

                            if (minedCount >= MAX_GHOST_MINE_COUNT || stack.isEmpty()) {
                                break;
                            }
                        }
                    }
                }
            }

            if (minedCount > 0) {
                level.playSound(null, originPos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 0.7F, 1.3F);
                com.midnightsp.soulless.SoullessMod.grantAdvancement(player, "ghostly_excavation");
            }
        } finally {
            IS_GHOST_MINING.set(false);
        }
    }
}

