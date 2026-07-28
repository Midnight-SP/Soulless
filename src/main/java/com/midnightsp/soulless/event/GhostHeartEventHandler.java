package com.midnightsp.soulless.event;

import com.midnightsp.soulless.SoullessMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class GhostHeartEventHandler {
    private static final ResourceLocation GHOST_HEART_MODIFIER_ID = 
        ResourceLocation.fromNamespaceAndPath(SoullessMod.MODID, "ghost_heart_health_boost");
    
    private static final int REGENERATION_DURATION = 100;
    private static final int FATIGUE_DURATION = 100;
    private static final int EFFECT_REFRESH_INTERVAL = 20;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null) return;

        boolean hasGhostHeart = isHoldingGhostHeart(player);

        if (hasGhostHeart) {
            applyGhostHeartEffects(player);
        } else {
            removeGhostHeartEffects(player);
        }
    }

    private boolean isHoldingGhostHeart(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        
        return (mainHand.getItem() == SoullessMod.GHOST_HEART.get() && !mainHand.isEmpty()) ||
               (offHand.getItem() == SoullessMod.GHOST_HEART.get() && !offHand.isEmpty());
    }

    private void applyGhostHeartEffects(Player player) {
        var healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute != null) {
            var existing = healthAttribute.getModifier(GHOST_HEART_MODIFIER_ID);
            if (existing == null) {
                AttributeModifier healthModifier = new AttributeModifier(
                    GHOST_HEART_MODIFIER_ID,
                    20.0,
                    AttributeModifier.Operation.ADD_VALUE
                );
                healthAttribute.addTransientModifier(healthModifier);
            }
        }

        MobEffectInstance currentRegen = player.getEffect(MobEffects.REGENERATION);
        if (currentRegen == null || currentRegen.getDuration() <= 20) {
            player.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                REGENERATION_DURATION,
                0,
                false,
                true,
                true
            ));
        }

        MobEffectInstance currentFatigue = player.getEffect(MobEffects.DIG_SLOWDOWN);
        if (currentFatigue == null || currentFatigue.getDuration() <= 20) {
            player.addEffect(new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                FATIGUE_DURATION,
                3,
                false,
                true,
                true
            ));
        }
    }

    private void removeGhostHeartEffects(Player player) {
        var healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.removeModifier(GHOST_HEART_MODIFIER_ID);
            
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }

        player.removeEffect(MobEffects.REGENERATION);
        player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }
}