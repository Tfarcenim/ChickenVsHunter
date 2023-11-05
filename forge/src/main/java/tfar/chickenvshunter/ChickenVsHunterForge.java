package tfar.chickenvshunter;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import tfar.chickenvshunter.datagen.ModDatagen;

@Mod(ChickenVsHunter.MOD_ID)
public class ChickenVsHunterForge {
    
    public ChickenVsHunterForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ChickenVsHunter.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::register);
        bus.addListener(ModDatagen::gather);
    }

    static RegisterEvent registerHelper;

    private void register(RegisterEvent event) {
        registerHelper = event;
        initializeItems();
        registerItem("chicken_helmet", Init.CHICKEN_HELMET);
        registerItem("chicken_chestplate", Init.CHICKEN_CHESTPLATE);
        registerItem("chicken_leggings", Init.CHICKEN_LEGGINGS);
        registerItem("chicken_boots", Init.CHICKEN_BOOTS);

        registerHelper.register(Registries.BLOCK,new ResourceLocation(ChickenVsHunter.MOD_ID,"golden_egg"),() -> Init.GOLDEN_EGG);
        registerItem("golden_egg",Init.GOLDEN_EGG_I);
    }

    public void initializeItems() {
        Init.CHICKEN_HELMET = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
        Init.CHICKEN_CHESTPLATE = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
        Init.CHICKEN_LEGGINGS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
        Init.CHICKEN_BOOTS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
    }

    private void registerItem(String name, Item item) {
        registerHelper.register(Registries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,name),() -> item);
    }
}