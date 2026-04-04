package com.midnightsp.soulless.block;

import javax.annotation.Nullable;

import com.midnightsp.soulless.entity.RipPrimedTntEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class RipBlock extends TntBlock {
    public RipBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // Intentionally ignore redstone-power checks used by vanilla TNT.
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        // Intentionally ignore redstone-power checks used by vanilla TNT.
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            RipPrimedTntEntity primed = new RipPrimedTntEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
            level.addFreshEntity(primed);
            level.playSound(null, primed.getX(), primed.getY(), primed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            RipPrimedTntEntity primed = new RipPrimedTntEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getIndirectSourceEntity() instanceof LivingEntity living ? living : null);
            primed.setFuse((short)(level.random.nextInt(primed.getFuse() / 4) + primed.getFuse() / 8));
            level.addFreshEntity(primed);
        }
    }
}
