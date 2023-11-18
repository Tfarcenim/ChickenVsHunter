package tfar.chickenvshunter;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ChickenVsHunterClient.setup();
    }
}
