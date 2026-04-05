package com.midnightsp.soulless.worldgen;

import com.midnightsp.soulless.SoullessMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class SoullessWorldgen {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, SoullessMod.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<RuinsStructure>> RUINS_STRUCTURE_TYPE =
        STRUCTURE_TYPES.register("ruins", () -> () -> RuinsStructure.CODEC);

    private SoullessWorldgen() {
    }

    public static void register(IEventBus modEventBus) {
        STRUCTURE_TYPES.register(modEventBus);
    }
}
