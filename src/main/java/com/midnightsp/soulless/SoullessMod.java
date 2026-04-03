package com.midnightsp.soulless;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.midnightsp.soulless.datagen.SoullessRecipeProvider;
import com.midnightsp.soulless.entity.SoullessEntities;
import com.midnightsp.soulless.item.GhostOrbItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SoullessMod.MODID)
public class SoullessMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "soulless";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "soulless" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "soulless" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "soulless" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Lost Souls item with the id "soulless:lost_souls"
    public static final DeferredItem<Item> LOST_SOULS = ITEMS.registerSimpleItem("lost_souls", new Item.Properties());
    public static final DeferredItem<Item> GHOSTPOWDER = ITEMS.registerSimpleItem("ghostpowder", new Item.Properties());
    public static final DeferredItem<Item> SOULSTEEL_INGOT = ITEMS.registerSimpleItem("soulsteel_ingot", new Item.Properties());
    public static final DeferredItem<Item> SOUL_REAPER = ITEMS.register("soul_reaper", () ->
        new SwordItem(Tiers.DIAMOND, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F)))
    );
    public static final DeferredItem<Item> GHOST_ORB = ITEMS.register("ghost_orb", () ->
        new GhostOrbItem(new Item.Properties().stacksTo(16))
    );
    public static final DeferredBlock<Block> SOULSTEEL_BLOCK = BLOCKS.registerSimpleBlock("soulsteel_block", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final DeferredItem<BlockItem> SOULSTEEL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("soulsteel_block", SOULSTEEL_BLOCK);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public SoullessMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register entity types
        SoullessEntities.ENTITY_TYPES.register(modEventBus);

        // Register datagen event listener
        modEventBus.addListener(this::gatherData);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (SoullessMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the Lost Souls item to the materials tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(LOST_SOULS);
            event.accept(GHOSTPOWDER);
            event.accept(SOULSTEEL_INGOT);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(SOULSTEEL_BLOCK_ITEM);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(SOUL_REAPER);
            event.accept(GHOST_ORB);
        }
    }

    // Register datagen providers
    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            generator.addProvider(event.includeServer(), new SoullessRecipeProvider(packOutput, lookupProvider));
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!event.getEntity().getType().is(EntityTypeTags.UNDEAD)) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player)) {
            return;
        }

        Player killer = (Player) event.getSource().getEntity();
        int dropCount = killer.getMainHandItem().is(SOUL_REAPER.get()) ? 2 : 1;
        ItemStack lostSoulDrop = new ItemStack(LOST_SOULS.get(), dropCount);
        event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), lostSoulDrop));
    }

    @SubscribeEvent
    public void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (!event.getEntity().getType().is(EntityTypeTags.UNDEAD)) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player attacker)) {
            return;
        }

        if (!attacker.getMainHandItem().is(SOUL_REAPER.get())) {
            return;
        }

        event.setNewDamage(event.getNewDamage() * 2.0F);
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (!event.getItemStack().is(GHOST_ORB.get())) {
            return;
        }

        var advancementId = ResourceLocation.fromNamespaceAndPath(MODID, "the_orb_of_dreamers");
        var advancement = serverPlayer.server.getAdvancements().get(advancementId);

        if (advancement == null) {
            return;
        }

        var progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
        for (String criterion : progress.getRemainingCriteria()) {
            serverPlayer.getAdvancements().award(advancement, criterion);
        }
    }
}
