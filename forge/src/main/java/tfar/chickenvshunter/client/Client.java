package tfar.chickenvshunter.client;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import tfar.chickenvshunter.GhickenEntity;
import tfar.chickenvshunter.GhickenEntityRenderer;
import tfar.chickenvshunter.Init;

public class Client {

    public static void render(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer((EntityType<? extends GhickenEntity>) Init.GHICKEN, GhickenEntityRenderer::new);
    }

}
