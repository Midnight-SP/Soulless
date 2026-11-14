package com.midnightsp.soulless;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import com.midnightsp.soulless.entity.SoullessEntities;

@Mod(value = SoullessMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SoullessMod.MODID, value = Dist.CLIENT)
public class SoullessModClient {
    public SoullessModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        SoullessMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        SoullessMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        EntityRenderers.register(SoullessEntities.GHOST_ORB.get(), context -> new ThrownItemRenderer(context, 1.0F, true));
        EntityRenderers.register(SoullessEntities.FIREBALL.get(), context -> new ThrownItemRenderer(context, 1.0F, true));
        EntityRenderers.register(SoullessEntities.RIP_PRIMED.get(), TntRenderer::new);
        EntityRenderers.register(SoullessEntities.HOLY_WATER.get(), ThrownItemRenderer::new);

        event.enqueueWork(() -> {
            ItemProperties.register(SoullessMod.DRACULAS_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            });
            ItemProperties.register(
                SoullessMod.DRACULAS_BOW.get(),
                ResourceLocation.withDefaultNamespace("pulling"),
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
            );
        });
    }
}
