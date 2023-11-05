package tfar.chickenvshunter;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

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
    }

    static RegisterEvent registerHelper;

    private void register(RegisterEvent event) {
        registerHelper = event;
        registerItem("chicken_helmet", Init.CHICKEN_HELMET);
        registerItem("chicken_chestplate", Init.CHICKEN_CHESTPLATE);
        registerItem("chicken_leggings", Init.CHICKEN_LEGGINGS);
        registerItem("chicken_boots", Init.CHICKEN_BOOTS);
    }

    private void registerItem(String name, Item item) {
        registerHelper.register(Registries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,name),() -> item);
    }
}