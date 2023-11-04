package tfar.chickenvshunter;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;

public class ChickenVsHunterFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        ChickenVsHunter.init();
        initializeItems();
        register();
    }

    public void register() {
        Registry.register(BuiltInRegistries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_helmet"),Init.CHICKEN_HELMET);
        Registry.register(BuiltInRegistries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_chestplate"),Init.CHICKEN_CHESTPLATE);
        Registry.register(BuiltInRegistries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_leggings"),Init.CHICKEN_LEGGINGS);
        Registry.register(BuiltInRegistries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_boots"),Init.CHICKEN_BOOTS);
    }

    public void initializeItems() {
        Init.CHICKEN_HELMET = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET,new Item.Properties());
        Init.CHICKEN_CHESTPLATE = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET,new Item.Properties());
        Init.CHICKEN_LEGGINGS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET,new Item.Properties());
        Init.CHICKEN_BOOTS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET,new Item.Properties());
    }
}
