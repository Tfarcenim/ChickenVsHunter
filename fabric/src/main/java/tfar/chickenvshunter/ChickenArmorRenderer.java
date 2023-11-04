package tfar.chickenvshunter;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChickenArmorRenderer extends GeoArmorRenderer<ChickenArmorItem> {
    public ChickenArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(ChickenVsHunter.MOD_ID, "armor/chicken_armor")));
    }
}
