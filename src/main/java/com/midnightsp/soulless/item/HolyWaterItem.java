package com.midnightsp.soulless.item;

import com.midnightsp.soulless.entity.HolyWaterEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class HolyWaterItem extends Item implements ProjectileItem {
    public HolyWaterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.SPLASH_POTION_THROW,
            SoundSource.PLAYERS,
            0.5F,
            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!level.isClientSide) {
            HolyWaterEntity projectile = new HolyWaterEntity(level, player);
            projectile.setItem(itemstack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        HolyWaterEntity projectile = new HolyWaterEntity(level, pos.x(), pos.y(), pos.z());
        projectile.setItem(stack);
        return projectile;
    }
}

