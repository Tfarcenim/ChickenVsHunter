package tfar.chickenvshunter;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Init {

    public static ArmorItem CHICKEN_HELMET;
    public static ArmorItem CHICKEN_CHESTPLATE;
    public static ArmorItem CHICKEN_LEGGINGS;
    public static ArmorItem CHICKEN_BOOTS;
    public static Block GOLDEN_EGG = new GoldenEggBlock(BlockBehaviour.Properties.copy(Blocks.DRAGON_EGG));
    public static BlockItem GOLDEN_EGG_I = new BlockItem(GOLDEN_EGG,new Item.Properties());
    public static Item CHICKEN_AXE = new AxeItem(Tiers.DIAMOND,0,0,new Item.Properties());
    public static Item CHICKEN_BOW = new BowItem(new Item.Properties().durability(384 * 4));
    public static Item CHICKEN_PICKAXE = new PickaxeItem(Tiers.DIAMOND,0,0,new Item.Properties());
    public static Item IRON_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.IRON);
    public static Item GOLD_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.GOLD);
    public static Item DIAMOND_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.DIAMOND);
    public static Item NETHERITE_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.NETHERITE);
    public static ChickenCompassItem CHICKEN_COMPASS = new ChickenCompassItem(new Item.Properties());
    public static EntityType<? extends Mob> GHICKEN;

    public static BlockEntityType<GoldenEggBlockEntity> GOLDEN_EGG_E = BlockEntityType.Builder.of(GoldenEggBlockEntity::new,GOLDEN_EGG).build(null);

    public static CreativeModeTab creativeModeTab = CreativeModeTab.builder(null,-1)
            .title(Component.translatable(ChickenVsHunter.MOD_ID))
            .icon(() -> new ItemStack(GOLDEN_EGG))
            .displayItems((itemDisplayParameters, output) -> getAllItems().forEach(output::accept)
            ).build();

    public static final List<Item> items = new ArrayList<>();
    public static List<Item> getAllItems() {
        if (items.isEmpty()) {
            Field[] fields = Init.class.getFields();

            for (Field field : fields) {
                try {
                    if (field.get(null) instanceof Item item) {
                        items.add(item);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return items;
    }

}
