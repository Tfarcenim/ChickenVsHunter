package tfar.chickenvshunter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import tfar.chickenvshunter.client.BasicArrowRenderer;
import tfar.chickenvshunter.client.BlockBreakerRenderer;
import tfar.chickenvshunter.entity.GhickenEntity;
import tfar.chickenvshunter.network.ClientPacketHandler;
import tfar.chickenvshunter.network.PacketHandler;

public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ChickenVsHunterClient.setup();
        KeyBindingHelper.registerKeyBinding(ChickenVsHunterClient.PICKUP_CHICKEN);
        ClientTickEvents.START_CLIENT_TICK.register(this::keyPressed);

        EntityRendererRegistry.register((EntityType<? extends GhickenEntity>) Init.GHICKEN, GhickenEntityRenderer::new);
        EntityRendererRegistry.register(Init.GHICKEN_FIREBALL, context -> new ThrownItemRenderer<>(context,3,true));

        EntityRendererRegistry.register(Init.CHICKEN_ARROW, BasicArrowRenderer::new);
        EntityRendererRegistry.register(Init.BLOCK_BREAKER, BlockBreakerRenderer::new);
    }

    private void keyPressed(Minecraft minecraft) {
        if (ChickenVsHunterClient.PICKUP_CHICKEN.consumeClick()) {
            if (minecraft.player != null && minecraft.player.getFirstPassenger() instanceof Chicken chicken) {
                ClientPacketHandler.sendKeyBind(0);
                chicken.stopRiding();
            } else if (minecraft.hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Chicken chicken) {
                ClientPacketHandler.sendKeyBind(chicken.getId());
                chicken.startRiding(minecraft.player);

            }
        }
    }
}
