package com.midnightsp.soulless.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import com.midnightsp.soulless.SoullessMod;
import java.util.Set;

public class SoullessBlockLootTableProvider extends BlockLootSubProvider {
    public SoullessBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // Boneyard: Drops 2-4 bones
        this.add(SoullessMod.BONEYARD.get(), 
            LootTable.lootTable()
                .withPool(this.applyExplosionDecay(SoullessMod.BONEYARD.get(),
                    LootPool.lootPool()
                        .setRolls(UniformGenerator.between(2.0F, 4.0F))
                        .add(LootItem.lootTableItem(Items.BONE))))
        );

        // RIP: Drops itself
        this.dropSelf(SoullessMod.RIP.get());

        // Soulsteel Block: Drops itself
        this.dropSelf(SoullessMod.SOULSTEEL_BLOCK.get());

        // Undead Core: Drops itself
        this.dropSelf(SoullessMod.UNDEAD_CORE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Set.of(
            SoullessMod.BONEYARD.get(),
            SoullessMod.RIP.get(),
            SoullessMod.SOULSTEEL_BLOCK.get(),
            SoullessMod.UNDEAD_CORE.get()
        );
    }
}
