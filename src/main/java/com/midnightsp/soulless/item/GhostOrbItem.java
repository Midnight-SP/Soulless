package com.midnightsp.soulless.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.midnightsp.soulless.entity.GhostOrbEntity;

public class GhostOrbItem extends Item {
    public GhostOrbItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            GhostOrbEntity ghostOrb = new GhostOrbEntity(level, player);
            ghostOrb.setDeltaMovement(player.getLookAngle().scale(1.0));
            level.addFreshEntity(ghostOrb);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        player.getCooldowns().addCooldown(this, 300);

        return InteractionResultHolder.success(itemStack);
    }
}
