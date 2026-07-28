package com.midnightsp.soulless;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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
    }
}
