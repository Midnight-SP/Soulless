package com.midnightsp.soulless;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.midnightsp.soulless.block.BoneyardBlock;
import com.midnightsp.soulless.block.RipBlock;
import com.midnightsp.soulless.block.UndeadCoreBlock;
import com.midnightsp.soulless.datagen.SoullessRecipeProvider;
import com.midnightsp.soulless.datagen.SoullessLootTableProvider;
import com.midnightsp.soulless.effect.VampirismMobEffect;
import com.midnightsp.soulless.entity.FireballEntity;
import com.midnightsp.soulless.entity.SoullessEntities;
import com.midnightsp.soulless.item.GhostHeartItem;
import com.midnightsp.soulless.item.BloodOrbItem;
import com.midnightsp.soulless.item.GhostOrbItem;
import com.midnightsp.soulless.item.MonstrousStewItem;
import com.midnightsp.soulless.item.ReturnTokenItem;
import com.midnightsp.soulless.item.RitualBladeItem;
import com.midnightsp.soulless.item.BloodtakerBladeItem;
import com.midnightsp.soulless.item.DraculasBladeItem;
import com.midnightsp.soulless.item.DraculasBowItem;
import com.midnightsp.soulless.item.GravediggerItem;
import com.midnightsp.soulless.item.SoulbreakerItem;
import com.midnightsp.soulless.item.BeheaderItem;
import com.midnightsp.soulless.item.InjectionNeedleItem;
import com.midnightsp.soulless.item.DraculasPendantItem;
import com.midnightsp.soulless.item.WoodenStakeItem;
import com.midnightsp.soulless.item.HolyWaterItem;
import com.midnightsp.soulless.item.SoulpowderItem;
import com.midnightsp.soulless.recipe.RitualRecipe;
import com.midnightsp.soulless.worldgen.SoullessWorldgen;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.joml.Vector3f;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

@Mod(SoullessMod.MODID)
public class SoullessMod {
    public static final String MODID = "soulless";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, MODID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<RitualRecipe>> RITUAL_RECIPE_TYPE = RECIPE_TYPES.register("ritual", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "ritual")));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RitualRecipe>> RITUAL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("ritual", RitualRecipe.Serializer::new);

    public static ResourceLocation SOULLESS_TAB_KEY;

    // Effects & Potions
    public static final DeferredHolder<MobEffect, MobEffect> VAMPIRISM_EFFECT = MOB_EFFECTS.register("vampirism", VampirismMobEffect::new);
    public static final DeferredHolder<Potion, Potion> VAMPIRISM_POTION = POTIONS.register("vampirism", () ->
        new Potion("vampirism", new MobEffectInstance(VAMPIRISM_EFFECT, 3600, 0))
    );
    public static final DeferredHolder<Potion, Potion> LONG_VAMPIRISM_POTION = POTIONS.register("long_vampirism", () ->
        new Potion("vampirism", new MobEffectInstance(VAMPIRISM_EFFECT, 9600, 0))
    );
    public static final DeferredHolder<Potion, Potion> STRONG_VAMPIRISM_POTION = POTIONS.register("strong_vampirism", () ->
        new Potion("vampirism", new MobEffectInstance(VAMPIRISM_EFFECT, 1800, 1))
    );

    // Armor Material
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SOULSTEEL_ARMOR_MATERIAL = ARMOR_MATERIALS.register("soulsteel", () -> new ArmorMaterial(
        Map.of(
            ArmorItem.Type.BOOTS, 3,
            ArmorItem.Type.LEGGINGS, 6,
            ArmorItem.Type.CHESTPLATE, 8,
            ArmorItem.Type.HELMET, 3
        ),
        15,
        SoundEvents.ARMOR_EQUIP_DIAMOND,
        () -> Ingredient.of(SoullessMod.SOULSTEEL_INGOT.get()),
        List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MODID, "soulsteel"))),
        2.0F,
        0.0F
    ));

    // Base Soulless Items
    public static final DeferredItem<Item> LOST_SOULS = ITEMS.registerSimpleItem("lost_souls", new Item.Properties());
    public static final DeferredItem<Item> GHOST_HEART = ITEMS.register("ghost_heart", () ->
        new GhostHeartItem(new Item.Properties().stacksTo(1).fireResistant())
    );
    public static final DeferredItem<Item> GHOSTPOWDER = ITEMS.registerSimpleItem("ghostpowder", new Item.Properties());
    public static final DeferredItem<Item> GOLD_SOULS = ITEMS.registerSimpleItem("gold_souls", new Item.Properties());
    public static final DeferredItem<Item> SOULPOWDER = ITEMS.register("soulpowder", () ->
        new SoulpowderItem(new Item.Properties())
    );
    public static final DeferredItem<Item> SOULSTEEL_INGOT = ITEMS.registerSimpleItem("soulsteel_ingot", new Item.Properties());
    public static final DeferredItem<Item> SOUL_REAPER = ITEMS.register("soul_reaper", () ->
        new SwordItem(Tiers.DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F)))
    );
    public static final DeferredItem<Item> BAT = ITEMS.register("bat", () ->
        new com.midnightsp.soulless.item.BatItem(new Item.Properties().attributes(
            SwordItem.createAttributes(Tiers.WOOD, 2, -2.0F)
        ))
    );
    public static final DeferredItem<Item> GHOST_ORB = ITEMS.register("ghost_orb", () ->
        new GhostOrbItem(new Item.Properties().stacksTo(16))
    );
    public static final DeferredItem<Item> FIREBALL = ITEMS.register("fireball", () ->
        new com.midnightsp.soulless.item.FireballItem(new Item.Properties().stacksTo(16))
    );
    public static final DeferredItem<Item> BLOOD_ORB = ITEMS.register("blood_orb", () ->
        new BloodOrbItem(new Item.Properties().stacksTo(16))
    );
    public static final DeferredItem<Item> MONSTROUS_STEW = ITEMS.register("monstrous_stew", () ->
        new MonstrousStewItem(new Item.Properties())
    );
    public static final DeferredItem<Item> RETURN_TOKEN = ITEMS.register("return_token", () ->
        new ReturnTokenItem(new Item.Properties().stacksTo(1))
    );
    public static final DeferredItem<Item> RITUAL_BLADE = ITEMS.register("ritual_blade", 
        RitualBladeItem::new
    );
    public static final DeferredItem<Item> BLOODTAKER_BLADE = ITEMS.register("bloodtaker_blade", 
        BloodtakerBladeItem::new
    );

    // Update 2.0 Items
    public static final DeferredItem<Item> BLOOD_DIAMOND = ITEMS.registerSimpleItem("blood_diamond", new Item.Properties());
    public static final DeferredItem<Item> DRACULAS_BLADE = ITEMS.register("draculas_blade", () ->
        new DraculasBladeItem(new Item.Properties())
    );
    public static final DeferredItem<Item> DRACULAS_BOW = ITEMS.register("draculas_bow", () ->
        new DraculasBowItem(new Item.Properties().durability(384))
    );
    public static final DeferredItem<Item> GRAVEDIGGER = ITEMS.register("gravedigger", () ->
        new GravediggerItem(new Item.Properties())
    );
    public static final DeferredItem<Item> SOULBREAKER = ITEMS.register("soulbreaker", () ->
        new SoulbreakerItem(new Item.Properties())
    );
    public static final DeferredItem<Item> BEHEADER = ITEMS.register("beheader", () ->
        new BeheaderItem(new Item.Properties())
    );
    public static final DeferredItem<Item> INJECTION_NEEDLE = ITEMS.register("injection_needle", () ->
        new InjectionNeedleItem(new Item.Properties())
    );
    public static final DeferredItem<Item> DRACULAS_PENDANT = ITEMS.register("draculas_pendant", () ->
        new DraculasPendantItem(new Item.Properties())
    );
    public static final DeferredItem<Item> WOODEN_STAKE = ITEMS.register("wooden_stake", () ->
        new WoodenStakeItem(new Item.Properties())
    );
    public static final DeferredItem<Item> HOLY_WATER = ITEMS.register("holy_water", () ->
        new HolyWaterItem(new Item.Properties().stacksTo(16))
    );

    // Soulsteel Armor
    public static final DeferredItem<ArmorItem> SOULSTEEL_HELMET = ITEMS.register("soulsteel_helmet", () ->
        new ArmorItem(SOULSTEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(33)))
    );
    public static final DeferredItem<ArmorItem> SOULSTEEL_CHESTPLATE = ITEMS.register("soulsteel_chestplate", () ->
        new ArmorItem(SOULSTEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(33)))
    );
    public static final DeferredItem<ArmorItem> SOULSTEEL_LEGGINGS = ITEMS.register("soulsteel_leggings", () ->
        new ArmorItem(SOULSTEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(33)))
    );
    public static final DeferredItem<ArmorItem> SOULSTEEL_BOOTS = ITEMS.register("soulsteel_boots", () ->
        new ArmorItem(SOULSTEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(33)))
    );

    // Blocks
    public static final DeferredBlock<Block> BONEYARD = BLOCKS.register("boneyard", () -> new BoneyardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).randomTicks()));
    public static final DeferredItem<BlockItem> BONEYARD_ITEM = ITEMS.registerSimpleBlockItem("boneyard", BONEYARD);
    public static final DeferredBlock<Block> RIP = BLOCKS.register("rip", () -> new RipBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static final DeferredItem<BlockItem> RIP_ITEM = ITEMS.registerSimpleBlockItem("rip", RIP);
    public static final DeferredBlock<Block> UNDEAD_CORE = BLOCKS.register("undead_core", () -> new UndeadCoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(4.0F, 6.0F)));
    public static final DeferredItem<BlockItem> UNDEAD_CORE_ITEM = ITEMS.registerSimpleBlockItem("undead_core", UNDEAD_CORE);
    public static final DeferredBlock<Block> SOULSTEEL_BLOCK = BLOCKS.registerSimpleBlock("soulsteel_block", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(3.0F, 6.0F));
    public static final DeferredItem<BlockItem> SOULSTEEL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("soulsteel_block", SOULSTEEL_BLOCK);
    public static final DeferredBlock<Block> BLOOD_DIAMOND_BLOCK = BLOCKS.registerSimpleBlock("blood_diamond_block", BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(5.0F, 6.0F));
    public static final DeferredItem<BlockItem> BLOOD_DIAMOND_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("blood_diamond_block", BLOOD_DIAMOND_BLOCK);

    public SoullessMod(IEventBus modEventBus, ModContainer modContainer) {
        var tab = CREATIVE_MODE_TABS.register("soulless_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(LOST_SOULS.get()))
            .title(Component.translatable("itemGroup.soulless"))
            .displayItems((params, output) -> {
                output.accept(LOST_SOULS.get());
                output.accept(GHOST_HEART.get());
                output.accept(GHOSTPOWDER.get());
                output.accept(GOLD_SOULS.get());
                output.accept(SOULPOWDER.get());
                output.accept(SOULSTEEL_INGOT.get());
                output.accept(SOUL_REAPER.get());
                output.accept(BAT.get());
                output.accept(GHOST_ORB.get());
                output.accept(FIREBALL.get());
                output.accept(BLOOD_ORB.get());
                output.accept(BLOOD_DIAMOND.get());
                output.accept(DRACULAS_BLADE.get());
                output.accept(DRACULAS_BOW.get());
                output.accept(GRAVEDIGGER.get());
                output.accept(SOULBREAKER.get());
                output.accept(BEHEADER.get());
                output.accept(INJECTION_NEEDLE.get());
                output.accept(DRACULAS_PENDANT.get());
                output.accept(WOODEN_STAKE.get());
                output.accept(HOLY_WATER.get());
                output.accept(SOULSTEEL_HELMET.get());
                output.accept(SOULSTEEL_CHESTPLATE.get());
                output.accept(SOULSTEEL_LEGGINGS.get());
                output.accept(SOULSTEEL_BOOTS.get());
                output.accept(MONSTROUS_STEW.get());
                output.accept(RETURN_TOKEN.get());
                output.accept(RITUAL_BLADE.get());
                output.accept(BLOODTAKER_BLADE.get());
                output.accept(SOULSTEEL_BLOCK_ITEM.get());
                output.accept(BLOOD_DIAMOND_BLOCK_ITEM.get());
                output.accept(BONEYARD_ITEM.get());
                output.accept(UNDEAD_CORE_ITEM.get());
                output.accept(RIP_ITEM.get());
            })
            .build());
        SOULLESS_TAB_KEY = tab.getId();
        modEventBus.addListener(this::commonSetup);

        MOB_EFFECTS.register(modEventBus);
        POTIONS.register(modEventBus);
        ARMOR_MATERIALS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        SoullessEntities.ENTITY_TYPES.register(modEventBus);
        SoullessWorldgen.register(modEventBus);

        modEventBus.addListener(this::gatherData);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(this::onRegisterBrewingRecipes);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(FIREBALL.get(), new DefaultDispenseItemBehavior() {
                @Override
                public ItemStack execute(BlockSource source, ItemStack stack) {
                    Direction direction = source.state().getValue(DispenserBlock.FACING);
                    Position position = DispenserBlock.getDispensePosition(source);
                    double x = position.x() + (double)((float)direction.getStepX() * 0.3F);
                    double y = position.y() + (double)((float)direction.getStepY() * 0.3F);
                    double z = position.z() + (double)((float)direction.getStepZ() * 0.3F);
                    ServerLevel level = source.level();
                    var random = level.random;
                    double speed = 1.0D;
                    Vec3 motion = new Vec3(
                        direction.getStepX() * speed + random.triangle(0.0D, 0.05D),
                        direction.getStepY() * speed + random.triangle(0.0D, 0.05D),
                        direction.getStepZ() * speed + random.triangle(0.0D, 0.05D)
                    );
                    FireballEntity fireball = new FireballEntity(level, x, y, z, motion);
                    level.addFreshEntity(fireball);
                    stack.shrink(1);
                    return stack;
                }

                @Override
                protected void playSound(BlockSource source) {
                    source.level().levelEvent(1018, source.pos(), 0);
                }
            });
        });

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(SOULLESS_TAB_KEY)) {
            event.accept(LOST_SOULS);
            event.accept(GHOST_HEART);
            event.accept(GHOSTPOWDER);
            event.accept(GOLD_SOULS);
            event.accept(SOULPOWDER);
            event.accept(SOULSTEEL_INGOT);
            event.accept(SOUL_REAPER);
            event.accept(BAT);
            event.accept(GHOST_ORB);
            event.accept(FIREBALL);
            event.accept(BLOOD_ORB);
            event.accept(BLOOD_DIAMOND);
            event.accept(DRACULAS_BLADE);
            event.accept(DRACULAS_BOW);
            event.accept(GRAVEDIGGER);
            event.accept(SOULBREAKER);
            event.accept(BEHEADER);
            event.accept(INJECTION_NEEDLE);
            event.accept(DRACULAS_PENDANT);
            event.accept(WOODEN_STAKE);
            event.accept(SOULSTEEL_HELMET);
            event.accept(SOULSTEEL_CHESTPLATE);
            event.accept(SOULSTEEL_LEGGINGS);
            event.accept(SOULSTEEL_BOOTS);
            event.accept(MONSTROUS_STEW);
            event.accept(RETURN_TOKEN);
            event.accept(SOULSTEEL_BLOCK_ITEM);
            event.accept(BLOOD_DIAMOND_BLOCK_ITEM);
            event.accept(BONEYARD_ITEM);
            event.accept(UNDEAD_CORE_ITEM);
            event.accept(RIP_ITEM);
        }
    }

    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            generator.addProvider(event.includeServer(), new SoullessRecipeProvider(packOutput, lookupProvider));
            generator.addProvider(event.includeServer(), new SoullessLootTableProvider(packOutput, lookupProvider));
        }
    }

    private void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, BLOOD_ORB.get(), VAMPIRISM_POTION);
        event.getBuilder().addMix(VAMPIRISM_POTION, Items.REDSTONE, LONG_VAMPIRISM_POTION);
        event.getBuilder().addMix(VAMPIRISM_POTION, Items.GLOWSTONE_DUST, STRONG_VAMPIRISM_POTION);
        event.getBuilder().addMix(Potions.AWKWARD, GHOSTPOWDER.get(), Potions.INVISIBILITY);
        event.getBuilder().addMix(Potions.NIGHT_VISION, GHOSTPOWDER.get(), Potions.INVISIBILITY);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    public static boolean hasDraculasPendant(LivingEntity entity) {
        if (entity instanceof Player player) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack.is(DRACULAS_PENDANT.get())) return true;
            }
            for (ItemStack stack : player.getInventory().offhand) {
                if (stack.is(DRACULAS_PENDANT.get())) return true;
            }
        } else if (entity != null) {
            return entity.getMainHandItem().is(DRACULAS_PENDANT.get()) || entity.getOffhandItem().is(DRACULAS_PENDANT.get());
        }
        return false;
    }

    public static int countDraculasPendants(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(DRACULAS_PENDANT.get())) count += stack.getCount();
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.is(DRACULAS_PENDANT.get())) count += stack.getCount();
        }
        return count;
    }

    public static boolean removeOneDraculasPendant(Player player) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.is(DRACULAS_PENDANT.get())) {
                stack.shrink(1);
                return true;
            }
        }
        for (int i = 0; i < player.getInventory().offhand.size(); i++) {
            ItemStack stack = player.getInventory().offhand.get(i);
            if (stack.is(DRACULAS_PENDANT.get())) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    public static int countItem(Player player, Item item) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) count += stack.getCount();
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.is(item)) count += stack.getCount();
        }
        return count;
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        // Prevent Dracula's Pendant, Blade, and Bow from dropping on death if kept
        if (event.getEntity() instanceof ServerPlayer player) {
            int keptPendants = player.getPersistentData().getInt("SoullessKeptPendants");
            if (keptPendants > 0) {
                event.getDrops().removeIf(itemEntity -> itemEntity.getItem().is(DRACULAS_PENDANT.get()));

                int keptBlades = player.getPersistentData().getInt("SoullessKeptBlades");
                if (keptBlades > 0) {
                    event.getDrops().removeIf(itemEntity -> itemEntity.getItem().is(DRACULAS_BLADE.get()));
                }

                int keptBows = player.getPersistentData().getInt("SoullessKeptBows");
                if (keptBows > 0) {
                    event.getDrops().removeIf(itemEntity -> itemEntity.getItem().is(DRACULAS_BOW.get()));
                }
            }
        }

        // Lost Souls drop from undead when killed by a player
        if (event.getEntity().getType().is(EntityTypeTags.UNDEAD) && event.getSource().getEntity() instanceof Player killer) {
            int dropCount = killer.getMainHandItem().is(SOUL_REAPER.get()) ? 2 : 1;
            ItemStack lostSoulDrop = new ItemStack(LOST_SOULS.get(), dropCount);
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), lostSoulDrop));
        }

        // Beheader decapitation chance (10%)
        if (event.getSource().getEntity() instanceof Player killer && killer.getMainHandItem().is(BEHEADER.get())) {
            LivingEntity victim = event.getEntity();
            if (victim.level().random.nextFloat() < 0.10F) {
                ItemStack skullDrop = ItemStack.EMPTY;
                if (victim instanceof WitherSkeleton) {
                    skullDrop = new ItemStack(Items.WITHER_SKELETON_SKULL);
                } else if (victim instanceof Skeleton) {
                    skullDrop = new ItemStack(Items.SKELETON_SKULL);
                } else if (victim instanceof Zombie && !(victim instanceof ZombifiedPiglin)) {
                    skullDrop = new ItemStack(Items.ZOMBIE_HEAD);
                } else if (victim instanceof Creeper) {
                    skullDrop = new ItemStack(Items.CREEPER_HEAD);
                } else if (victim instanceof Piglin) {
                    skullDrop = new ItemStack(Items.PIGLIN_HEAD);
                } else if (victim instanceof Player) {
                    skullDrop = new ItemStack(Items.PLAYER_HEAD);
                }

                if (!skullDrop.isEmpty()) {
                    event.getDrops().add(new ItemEntity(victim.level(), victim.getX(), victim.getY(), victim.getZ(), skullDrop));
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamagePre(LivingDamageEvent.Pre event) {
        // Wooden Stake bonus damage against undead and vampires
        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker.getMainHandItem().is(WOODEN_STAKE.get())) {
            boolean isUndead = event.getEntity().getType().is(EntityTypeTags.UNDEAD);
            boolean isVampire = event.getEntity().hasEffect(VAMPIRISM_EFFECT) || hasDraculasPendant(event.getEntity());
            if (isUndead && isVampire) {
                event.setNewDamage(event.getNewDamage() + 10.0F);
            } else if (isUndead) {
                event.setNewDamage(event.getNewDamage() + 6.0F);
            } else if (isVampire) {
                event.setNewDamage(event.getNewDamage() + 8.0F);
            }
        }

        // Soul Reaper doubles damage to undead
        if (event.getEntity().getType().is(EntityTypeTags.UNDEAD)) {
            if (event.getSource().getEntity() instanceof Player attacker && attacker.getMainHandItem().is(SOUL_REAPER.get())) {
                event.setNewDamage(event.getNewDamage() * 2.0F);
            }
        }

        // Soulsteel Armor provides 15% damage reduction from undead attacks per piece
        if (event.getEntity() instanceof Player victim) {
            if (event.getSource().getEntity() != null && event.getSource().getEntity().getType().is(EntityTypeTags.UNDEAD)) {
                int soulsteelPieces = 0;
                if (victim.getItemBySlot(EquipmentSlot.HEAD).is(SOULSTEEL_HELMET.get())) soulsteelPieces++;
                if (victim.getItemBySlot(EquipmentSlot.CHEST).is(SOULSTEEL_CHESTPLATE.get())) soulsteelPieces++;
                if (victim.getItemBySlot(EquipmentSlot.LEGS).is(SOULSTEEL_LEGGINGS.get())) soulsteelPieces++;
                if (victim.getItemBySlot(EquipmentSlot.FEET).is(SOULSTEEL_BOOTS.get())) soulsteelPieces++;

                if (soulsteelPieces > 0) {
                    float multiplier = Math.max(0.2F, 1.0F - (0.15F * soulsteelPieces));
                    event.setNewDamage(event.getNewDamage() * multiplier);
                    if (soulsteelPieces >= 4 && victim instanceof ServerPlayer serverPlayer) {
                        grantAdvancement(serverPlayer, "soul_warden");
                    }
                }
            }
        }

        // Dracula's Bow arrow mechanics
        if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
            if (projectile.getTags().contains("draculas_arrow_empowered")) {
                event.setNewDamage(event.getNewDamage() + 3.0F);
                if (projectile.getOwner() instanceof LivingEntity shooter) {
                    shooter.heal(3.0F);
                    if (shooter instanceof Player player) {
                        feedOnEnemy(player, 3.0F);
                    }
                }
            } else if (projectile.getTags().contains("draculas_arrow_cursed")) {
                event.setNewDamage(event.getNewDamage() * 0.5F);
            }
        }

        // Dracula's Blade mechanics (weaker without pendant, empowered with pendant)
        if (event.getSource().getEntity() instanceof LivingEntity bladeAttacker && bladeAttacker.getMainHandItem().is(DRACULAS_BLADE.get())) {
            if (!hasDraculasPendant(bladeAttacker)) {
                event.setNewDamage(event.getNewDamage() * 0.6F);
            } else {
                event.setNewDamage(event.getNewDamage() + 2.0F);
            }
        }

        // Vampirism effect lifesteal on hit against living non-undead (or undead if wielding Dracula's Pendant)
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            boolean canLeech = !event.getEntity().getType().is(EntityTypeTags.UNDEAD) || hasDraculasPendant(attacker);
            if (attacker.hasEffect(VAMPIRISM_EFFECT) && canLeech) {
                int amplifier = attacker.getEffect(VAMPIRISM_EFFECT).getAmplifier();
                float lifesteal = event.getNewDamage() * 0.15F * (amplifier + 1);
                attacker.heal(lifesteal);
                if (attacker instanceof Player player) {
                    feedOnEnemy(player, lifesteal);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        // Naturally spawning vampire mobs: only living monsters with blood (Spiders, Illagers, Witches) can contract vampirism
        if (event.getEntity() instanceof Monster monster && !monster.getType().is(EntityTypeTags.UNDEAD)) {
            if (monster instanceof Spider || monster instanceof AbstractIllager || monster instanceof Witch) {
                var random = monster.getRandom();
                if (random.nextFloat() < 0.02F) {
                    monster.addEffect(new MobEffectInstance(VAMPIRISM_EFFECT, MobEffectInstance.INFINITE_DURATION, 0, false, true, true));
                    monster.addTag("soulless:vampire_mob");
                }
            }
        }

        if (event.getEntity() instanceof Skeleton || event.getEntity() instanceof Zombie) {
            var random = event.getEntity().getRandom();
            if (random.nextFloat() < 0.02F) { // 2% chance to spawn with soulsteel gear
                int choice = random.nextInt(5);
                ItemStack item = switch (choice) {
                    case 0 -> new ItemStack(SOUL_REAPER.get());
                    case 1 -> new ItemStack(BEHEADER.get());
                    case 2 -> new ItemStack(GRAVEDIGGER.get());
                    case 3 -> new ItemStack(SOULBREAKER.get());
                    default -> new ItemStack(SOULSTEEL_HELMET.get());
                };
                if (item.isDamageableItem()) {
                    item.setDamageValue(item.getMaxDamage() - random.nextInt(Math.max(1, item.getMaxDamage() / 2)));
                }

                if (choice == 4) {
                    event.getEntity().setItemSlot(EquipmentSlot.HEAD, item);
                    event.getEntity().setDropChance(EquipmentSlot.HEAD, 0.085F);
                } else {
                    event.getEntity().setItemSlot(EquipmentSlot.MAINHAND, item);
                    event.getEntity().setDropChance(EquipmentSlot.MAINHAND, 0.085F);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (event.getSource().getEntity() instanceof LivingEntity killer) {
            // Beheader decapitation mechanic
            if (killer.getMainHandItem().is(BEHEADER.get())) {
                Item headItem = null;
                if (victim instanceof Zombie) {
                    headItem = Items.ZOMBIE_HEAD;
                } else if (victim instanceof Skeleton) {
                    headItem = Items.SKELETON_SKULL;
                } else if (victim instanceof Creeper) {
                    headItem = Items.CREEPER_HEAD;
                } else if (victim instanceof WitherSkeleton) {
                    headItem = Items.WITHER_SKELETON_SKULL;
                } else if (victim instanceof Piglin) {
                    headItem = Items.PIGLIN_HEAD;
                } else if (victim instanceof Player) {
                    headItem = Items.PLAYER_HEAD;
                }

                if (headItem != null) {
                    float chance = victim instanceof Player ? 1.0F : 0.20F;
                    if (victim.getRandom().nextFloat() < chance) {
                        victim.spawnAtLocation(new ItemStack(headItem));
                        victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                            SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 0.8F);
                        if (killer instanceof ServerPlayer killerPlayer) {
                            grantAdvancement(killerPlayer, "off_with_their_heads");
                        }
                    }
                }
            }

            // Wooden Stake bonus vs vampire advancement
            if (killer.getMainHandItem().is(WOODEN_STAKE.get())) {
                boolean isVampire = victim.hasEffect(VAMPIRISM_EFFECT) || hasDraculasPendant(victim);
                if (isVampire && killer instanceof ServerPlayer killerPlayer) {
                    grantAdvancement(killerPlayer, "stake_through_the_heart");
                }
            }

            // Soul Reaper vs Undead advancement
            if (killer.getMainHandItem().is(SOUL_REAPER.get()) && victim.getType().is(EntityTypeTags.UNDEAD)) {
                if (killer instanceof ServerPlayer killerPlayer) {
                    grantAdvancement(killerPlayer, "dont_fear_the_reaper");
                }
            }
        }

        if (event.getEntity() instanceof ServerPlayer victimPlayer) {
            boolean killedByStake = false;
            if (event.getSource().getEntity() instanceof LivingEntity killer) {
                if (killer.getMainHandItem().is(WOODEN_STAKE.get())) {
                    killedByStake = true;
                }
                if (killer.hasEffect(VAMPIRISM_EFFECT) || killer.getMainHandItem().is(DRACULAS_BLADE.get())) {
                    victimPlayer.getPersistentData().putBoolean("SoullessContractedVampirism", true);
                }
            }

            int pendantCount = countDraculasPendants(victimPlayer);
            if (pendantCount > 0) {
                if (killedByStake) {
                    removeOneDraculasPendant(victimPlayer);
                    victimPlayer.getPersistentData().remove("SoullessKeptPendants");
                    victimPlayer.getPersistentData().remove("SoullessKeptBlades");
                    victimPlayer.getPersistentData().remove("SoullessKeptBows");
                    victimPlayer.getPersistentData().remove("SoullessContractedVampirism");
                    victimPlayer.getPersistentData().remove("SoullessBloodConsumed");
                    victimPlayer.level().playSound(null, victimPlayer.getX(), victimPlayer.getY(), victimPlayer.getZ(),
                        SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.5F, 0.8F);
                    if (event.getSource().getEntity() instanceof ServerPlayer killerPlayer) {
                        grantAdvancement(killerPlayer, "stake_through_the_heart");
                    }
                    sendVampireCuredLore(victimPlayer);
                } else {
                    victimPlayer.getPersistentData().putInt("SoullessKeptPendants", pendantCount);
                    int bladeCount = countItem(victimPlayer, DRACULAS_BLADE.get());
                    int bowCount = countItem(victimPlayer, DRACULAS_BOW.get());
                    victimPlayer.getPersistentData().putInt("SoullessKeptBlades", bladeCount);
                    victimPlayer.getPersistentData().putInt("SoullessKeptBows", bowCount);
                }
            } else {
                victimPlayer.getPersistentData().remove("SoullessKeptPendants");
                victimPlayer.getPersistentData().remove("SoullessKeptBlades");
                victimPlayer.getPersistentData().remove("SoullessKeptBows");
                if (killedByStake) {
                    victimPlayer.getPersistentData().remove("SoullessContractedVampirism");
                }
                if (victimPlayer.hasEffect(VAMPIRISM_EFFECT) && !victimPlayer.getPersistentData().getBoolean("SoullessContractedVampirism")) {
                    victimPlayer.getPersistentData().remove("SoullessBloodConsumed");
                    sendVampireCuredLore(victimPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getEntity().getPersistentData().remove("SoullessBloodConsumed");
            if (event.getOriginal().getPersistentData().getBoolean("SoullessContractedVampirism")) {
                event.getEntity().getPersistentData().putBoolean("SoullessContractedVampirism", true);
            }
            if (event.getOriginal().getPersistentData().getBoolean("SoullessReceivedVampireLore")) {
                event.getEntity().getPersistentData().putBoolean("SoullessReceivedVampireLore", true);
            }
            int keptPendants = event.getOriginal().getPersistentData().getInt("SoullessKeptPendants");
            if (keptPendants > 0) {
                event.getEntity().getPersistentData().putInt("SoullessKeptPendants", keptPendants);
                event.getEntity().getPersistentData().putInt("SoullessKeptBlades", event.getOriginal().getPersistentData().getInt("SoullessKeptBlades"));
                event.getEntity().getPersistentData().putInt("SoullessKeptBows", event.getOriginal().getPersistentData().getInt("SoullessKeptBows"));
            }
        } else {
            CompoundTag originalTag = event.getOriginal().getPersistentData();
            if (originalTag.contains("SoullessBloodConsumed")) {
                event.getEntity().getPersistentData().putInt("SoullessBloodConsumed", originalTag.getInt("SoullessBloodConsumed"));
            }
            if (originalTag.getBoolean("SoullessContractedVampirism")) {
                event.getEntity().getPersistentData().putBoolean("SoullessContractedVampirism", true);
            }
            if (originalTag.getBoolean("SoullessReceivedVampireLore")) {
                event.getEntity().getPersistentData().putBoolean("SoullessReceivedVampireLore", true);
            }
            int keptPendants = originalTag.getInt("SoullessKeptPendants");
            if (keptPendants > 0) {
                event.getEntity().getPersistentData().putInt("SoullessKeptPendants", keptPendants);
                event.getEntity().getPersistentData().putInt("SoullessKeptBlades", originalTag.getInt("SoullessKeptBlades"));
                event.getEntity().getPersistentData().putInt("SoullessKeptBows", originalTag.getInt("SoullessKeptBows"));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            if (player.getPersistentData().contains("SoullessKeptPendants")) {
                int kept = player.getPersistentData().getInt("SoullessKeptPendants");
                player.getPersistentData().remove("SoullessKeptPendants");
                if (kept > 0) {
                    player.getInventory().add(new ItemStack(DRACULAS_PENDANT.get(), kept));
                    player.addEffect(new MobEffectInstance(VAMPIRISM_EFFECT, MobEffectInstance.INFINITE_DURATION, 0, false, false, true));

                    int keptBlades = player.getPersistentData().getInt("SoullessKeptBlades");
                    player.getPersistentData().remove("SoullessKeptBlades");
                    for (int i = 0; i < keptBlades; i++) {
                        player.getInventory().add(new ItemStack(DRACULAS_BLADE.get()));
                    }

                    int keptBows = player.getPersistentData().getInt("SoullessKeptBows");
                    player.getPersistentData().remove("SoullessKeptBows");
                    for (int i = 0; i < keptBows; i++) {
                        player.getInventory().add(new ItemStack(DRACULAS_BOW.get()));
                    }
                }
            }

            if (player.getPersistentData().getBoolean("SoullessContractedVampirism")) {
                player.getPersistentData().remove("SoullessContractedVampirism");
                player.getPersistentData().putBoolean("SoullessAwakeningLoreHandled", true);
                player.addEffect(new MobEffectInstance(VAMPIRISM_EFFECT, MobEffectInstance.INFINITE_DURATION, 0, false, false, true));
                if (player instanceof ServerPlayer serverPlayer) {
                    grantAdvancement(serverPlayer, "draculas_curse");
                }
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.7F, 0.5F);
                sendVampireAwakeningLore(player);
            }
        }
    }

    public static void sendVampireAwakeningLore(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        CompoundTag data = player.getPersistentData();
        if (!data.getBoolean("SoullessReceivedVampireLore")) {
            data.putBoolean("SoullessReceivedVampireLore", true);

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.translatable("message.soulless.vampire_whisper"));
            player.sendSystemMessage(Component.literal(""));
        } else {
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.translatable("message.soulless.vampire_whisper_reawakened"));
            player.sendSystemMessage(Component.literal(""));
        }
    }

    public static void sendVampireCuredLore(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.translatable("message.soulless.vampire_whisper_cured"));
        player.sendSystemMessage(Component.literal(""));
    }

    public static void sendVampirePendantProtectedLore(Player player) {
        if (player.level().isClientSide()) {
            return;
        }
        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.translatable("message.soulless.vampire_whisper_pendant_protected"));
        player.sendSystemMessage(Component.literal(""));
    }

    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().is(Items.MILK_BUCKET) && event.getEntity() instanceof Player player) {
            if (player.hasEffect(VAMPIRISM_EFFECT)) {
                if (hasDraculasPendant(player)) {
                    sendVampirePendantProtectedLore(player);
                } else {
                    player.getPersistentData().remove("SoullessBloodConsumed");
                    player.getPersistentData().remove("SoullessContractedVampirism");
                    sendVampireCuredLore(player);
                }
            }
        }
    }

    public static void feedOnEnemy(Player player, float lifestealAmount) {
        if (player.level().isClientSide() || lifestealAmount <= 0.0F) {
            return;
        }
        int foodGain = Math.max(1, Math.round(lifestealAmount * 0.5F));
        float saturationGain = lifestealAmount * 0.6F;
        player.getFoodData().eat(foodGain, 0.6F);
        float newSaturation = Math.min((float) player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel() + saturationGain);
        player.getFoodData().setSaturation(newSaturation);

        if (player instanceof ServerPlayer serverPlayer) {
            grantAdvancement(serverPlayer, "blood_feast");
        }
    }

    @SubscribeEvent
    public void onEffectAdded(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Added event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().getEffect().is(VAMPIRISM_EFFECT)) {
            grantAdvancement(player, "draculas_curse");
            if (event.getOldEffectInstance() == null) {
                if (player.getPersistentData().getBoolean("SoullessAwakeningLoreHandled")) {
                    player.getPersistentData().remove("SoullessAwakeningLoreHandled");
                } else if (!hasDraculasPendant(player)) {
                    sendVampireAwakeningLore(player);
                } else if (!player.getPersistentData().getBoolean("SoullessReceivedVampireLore")) {
                    sendVampireAwakeningLore(player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEffectExpired(net.neoforged.neoforge.event.entity.living.MobEffectEvent.Expired event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getEffectInstance().getEffect().is(VAMPIRISM_EFFECT)) {
            if (!hasDraculasPendant(player) && player.getPersistentData().getInt("SoullessBloodConsumed") < 32) {
                player.getPersistentData().remove("SoullessBloodConsumed");
                player.getPersistentData().remove("SoullessContractedVampirism");
                sendVampireCuredLore(player);
            }
        }
    }

    public static void grantAdvancement(ServerPlayer player, String id) {
        if (player == null || player.server == null) {
            return;
        }
        var advancementId = ResourceLocation.fromNamespaceAndPath(MODID, id);
        var advancement = player.server.getAdvancements().get(advancementId);
        if (advancement == null) {
            return;
        }
        var progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (player.getMainHandItem().is(BAT.get())) {
                if (event.getTarget() instanceof Projectile) {
                    grantAdvancement(player, "home_run");
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(UNDEAD_CORE_ITEM.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.undead_core.line1").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.soulless.undead_core.line2").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        if (event.getItemStack().is(RETURN_TOKEN.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.return_token.line1").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(RIP_ITEM.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.rip.line1").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.soulless.rip.line2").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        if (event.getItemStack().is(GHOST_HEART.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.ghost_heart.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(SOUL_REAPER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.soul_reaper.line1").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(RITUAL_BLADE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.ritual_blade.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(BLOODTAKER_BLADE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.bloodtaker_blade.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(DRACULAS_BLADE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_blade.line1").withStyle(ChatFormatting.RED));
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_blade.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(DRACULAS_BOW.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_bow.line1").withStyle(ChatFormatting.DARK_RED));
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_bow.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(GRAVEDIGGER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.gravedigger.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(SOULBREAKER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.soulbreaker.line2").withStyle(ChatFormatting.AQUA));
            return;
        }

        if (event.getItemStack().is(BEHEADER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.beheader.line1").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.soulless.beheader.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(INJECTION_NEEDLE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.injection_needle.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(SOULPOWDER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.soulpowder.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(DRACULAS_PENDANT.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_pendant.line1").withStyle(ChatFormatting.DARK_RED));
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_pendant.line2").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.soulless.draculas_pendant.line3").withStyle(ChatFormatting.RED));
            return;
        }

        if (event.getItemStack().is(WOODEN_STAKE.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.wooden_stake.line1").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("tooltip.soulless.wooden_stake.line2").withStyle(ChatFormatting.DARK_RED));
            return;
        }

        if (event.getItemStack().is(HOLY_WATER.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.holy_water.line1").withStyle(ChatFormatting.AQUA));
            event.getToolTip().add(Component.translatable("tooltip.soulless.holy_water.line2").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (event.getItemStack().is(SOULSTEEL_HELMET.get()) || event.getItemStack().is(SOULSTEEL_CHESTPLATE.get()) ||
            event.getItemStack().is(SOULSTEEL_LEGGINGS.get()) || event.getItemStack().is(SOULSTEEL_BOOTS.get())) {
            event.getToolTip().add(Component.translatable("tooltip.soulless.soulsteel_armor.line2").withStyle(ChatFormatting.DARK_AQUA));
            return;
        }
    }

    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide()) {
            if (living.getTags().contains("soulless:vampire_mob") && living.tickCount % 10 == 0) {
                if (living.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                        new DustParticleOptions(new Vector3f(0.85F, 0.05F, 0.05F), 0.8F),
                        living.getX() + (living.getRandom().nextDouble() - 0.5) * 0.4,
                        living.getEyeY() + (living.getRandom().nextDouble() - 0.5) * 0.2,
                        living.getZ() + (living.getRandom().nextDouble() - 0.5) * 0.4,
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.0
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CLERIC) {
            // Tier 2 (Apprentice): 1 Emerald -> 1 Wooden Stake
            var tier2 = event.getTrades().get(2);
            if (tier2 != null) {
                tier2.add(new BasicItemListing(1, new ItemStack(WOODEN_STAKE.get()), 12, 2, 0.05F));
            }

            // Tier 3 (Journeyman): 1 Emerald + 1 Glass Bottle -> 2 Holy Water
            var tier3 = event.getTrades().get(3);
            if (tier3 != null) {
                tier3.add(new BasicItemListing(new ItemStack(Items.EMERALD, 1), new ItemStack(Items.GLASS_BOTTLE, 1), new ItemStack(HOLY_WATER.get(), 2), 12, 4, 0.05F));
            }

            // Tier 4 (Expert): 1 Emerald -> 1 Injection Needle
            var tier4 = event.getTrades().get(4);
            if (tier4 != null) {
                tier4.add(new BasicItemListing(1, new ItemStack(INJECTION_NEEDLE.get()), 8, 6, 0.05F));
            }
        }
    }
}
