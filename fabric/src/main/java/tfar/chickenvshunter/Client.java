package tfar.chickenvshunter;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ItemProperties.register(
                Init.CHICKEN_BOW, new ResourceLocation("pulling"), ($$0x, $$1, $$2, $$3) -> $$2 != null && $$2.isUsingItem() && $$2.getUseItem() == $$0x ? 1.0F : 0.0F
        );

        ItemProperties.register(Init.CHICKEN_BOW, new ResourceLocation("pull"), ($$0x, $$1, $$2, $$3) -> {
            if ($$2 == null) {
                return 0.0F;
            } else {
                return $$2.getUseItem() != $$0x ? 0.0F : (float)($$0x.getUseDuration() - $$2.getUseItemRemainingTicks()) / 20.0F;
            }
        });

    }
}
