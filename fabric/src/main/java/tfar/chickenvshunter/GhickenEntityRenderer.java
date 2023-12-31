package tfar.chickenvshunter;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import tfar.chickenvshunter.entity.GhickenEntity;

public class GhickenEntityRenderer extends GeoEntityRenderer<GhickenEntity> {
    public GhickenEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(ChickenVsHunter.MOD_ID,"ghicken"),true));
        shadowRadius = 1;
    }
}