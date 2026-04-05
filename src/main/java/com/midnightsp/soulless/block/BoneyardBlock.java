package com.midnightsp.soulless.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LightLayer;

public class BoneyardBlock extends Block {
    public BoneyardBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        trySpawnSkeleton(state, level, pos);

        if (level.getBlockState(pos).is(this)) {
            level.scheduleTick(pos, this, 20);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        trySpawnSkeleton(state, level, pos);
    }

    private void trySpawnSkeleton(BlockState state, ServerLevel level, BlockPos pos) {
        // Only block light matters for the base check.
        if (level.getBrightness(LightLayer.BLOCK, pos.above()) > 7) {
            return;
        }

        // If there's any sky light, it must be night time (12000-23999 ticks).
        if (level.getBrightness(LightLayer.SKY, pos.above()) > 0) {
            long dayTime = level.getDayTime() % 24000L;
            if (dayTime < 12000L) {
                return;  // Daytime
            }
        }

        Player nearbyPlayer = level.getNearestPlayer(
            pos.getX() + 0.5D,
            pos.getY() + 0.5D,
            pos.getZ() + 0.5D,
            8.0D,
            entity -> entity instanceof Player player && !player.isCreative() && !player.isSpectator()
        );

        if (nearbyPlayer == null) {
            return;
        }

        BlockPos spawnPos = pos.above();
        if (!level.getBlockState(spawnPos).isAir() || !level.getBlockState(spawnPos.above()).isAir()) {
            return;
        }

        Skeleton skeleton = EntityType.SKELETON.create(level);
        if (skeleton == null) {
            return;
        }

        level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        skeleton.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
        
        // Randomly give skeleton either a bow or stone sword (50/50).
        if (level.random.nextBoolean()) {
            skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        } else {
            skeleton.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
        
        level.addFreshEntity(skeleton);
    }
}
