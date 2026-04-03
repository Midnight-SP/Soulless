package com.midnightsp.soulless.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.midnightsp.soulless.SoullessMod;

public class SoullessEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, SoullessMod.MODID);
    
    public static final DeferredHolder<EntityType<?>, EntityType<GhostOrbEntity>> GHOST_ORB = ENTITY_TYPES.register("ghost_orb", () ->
        EntityType.Builder.<GhostOrbEntity>of(GhostOrbEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .build("soulless:ghost_orb")
    );
}
