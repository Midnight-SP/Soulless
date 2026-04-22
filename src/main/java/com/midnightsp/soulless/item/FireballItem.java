package com.midnightsp.soulless.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

import com.midnightsp.soulless.entity.FireballEntity;

public class FireballItem extends Item {
    public FireballItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            FireballEntity fireball = new FireballEntity(level, player);
            fireball.setDeltaMovement(player.getLookAngle().scale(1.0));
            level.addFreshEntity(fireball);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 2.5F, 1.0F);
        }
        player.getCooldowns().addCooldown(this, 20);
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
