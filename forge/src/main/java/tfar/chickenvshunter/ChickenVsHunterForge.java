package tfar.chickenvshunter;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import tfar.chickenvshunter.client.Client;
import tfar.chickenvshunter.datagen.ModDatagen;
import tfar.chickenvshunter.entity.GhickenFireballEntity;

import java.util.function.Supplier;

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
        bus.addListener(this::attribute);

        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(Client::render);
        }

    }

    static RegisterEvent registerHelper;

    private void register(RegisterEvent event) {
        registerHelper = event;
        registerItem("chicken_helmet", () -> Init.CHICKEN_HELMET = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties()));
        registerItem("chicken_chestplate",() -> Init.CHICKEN_CHESTPLATE = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
        registerItem("chicken_leggings",() -> Init.CHICKEN_LEGGINGS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties()));
        registerItem("chicken_boots",() -> Init.CHICKEN_BOOTS = new ChickenArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties()));

        registerHelper.register(Registries.BLOCK,new ResourceLocation(ChickenVsHunter.MOD_ID,"golden_egg"),() -> Init.GOLDEN_EGG);
        registerItem("golden_egg",() -> Init.GOLDEN_EGG_I);

        registerItem("chicken_axe",() -> Init.CHICKEN_AXE);
        registerItem("chicken_bow",() -> Init.CHICKEN_BOW);
        registerItem("chicken_pickaxe",() -> Init.CHICKEN_PICKAXE);

        registerItem("iron_seeds",() -> Init.IRON_SEEDS);
        registerItem("gold_seeds",() -> Init.GOLD_SEEDS);
        registerItem("diamond_seeds",() -> Init.DIAMOND_SEEDS);
        registerItem("netherite_seeds",() -> Init.NETHERITE_SEEDS);
        registerItem("chicken_compass",() -> Init.CHICKEN_COMPASS);

        event.register(Registries.CREATIVE_MODE_TAB,new ResourceLocation(ChickenVsHunter.MOD_ID,"creative_tab"),() -> Init.creativeModeTab);
        event.register(Registries.BLOCK_ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"golden_egg"),() -> Init.GOLDEN_EGG_E);
        event.register(Registries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"ghicken"),() -> Init.GHICKEN = EntityType.Builder.of(GhickenEntity::new, MobCategory.CREATURE).build("ghicken"));
        event.register(Registries.ENTITY_TYPE,new ResourceLocation(ChickenVsHunter.MOD_ID,"ghicken_fireball"),() -> Init.GHICKEN_FIREBALL);

        event.register(Registries.MOB_EFFECT,new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_curse"),() -> Init.CHICKEN_CURSE);

    }

    private void attribute(EntityAttributeCreationEvent event) {
        event.put(Init.GHICKEN,GhickenEntity.createAttributes().build());
    }

    private void registerItem(String name, Supplier<Item> item) {
        registerHelper.register(Registries.ITEM,new ResourceLocation(ChickenVsHunter.MOD_ID,name),item);
    }
}