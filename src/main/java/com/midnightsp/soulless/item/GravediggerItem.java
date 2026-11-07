package com.midnightsp.soulless.item;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GravediggerItem extends ShovelItem {
    public GravediggerItem(Properties properties) {
        super(SoulsteelTier.INSTANCE, properties.attributes(ShovelItem.createAttributes(SoulsteelTier.INSTANCE, 1.5F, -3.0F)));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide() && state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            if (level.random.nextFloat() < 0.10F) {
                ItemEntity boneEntity = new ItemEntity(
                    level,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    new ItemStack(Items.BONE, 1)
                );
                boneEntity.setDefaultPickUpDelay();
                level.addFreshEntity(boneEntity);
            }
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(pos);
        Player player = context.getPlayer();

        if (context.getClickedFace() != Direction.DOWN && level.getBlockState(pos.above()).isAir()) {
            if (clickedState.is(Blocks.GRASS_BLOCK) || clickedState.is(Blocks.DIRT) || 
                clickedState.is(Blocks.COARSE_DIRT) || clickedState.is(Blocks.ROOTED_DIRT) || 
                clickedState.is(Blocks.PODZOL) || clickedState.is(Blocks.MYCELIUM)) {
                
                level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide()) {
                    level.setBlock(pos, SoullessMod.BONEYARD.get().defaultBlockState(), 11);
                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        SoullessMod.grantAdvancement(serverPlayer, "graveyard_shift");
                    }
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(10, player, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return super.useOn(context);
    }
}

