package tfar.chickenvshunter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.chickenvshunter.entity.BlockBreakerEntity;
import tfar.chickenvshunter.entity.ChickenArrowEntity;
import tfar.chickenvshunter.entity.GhickenFireballEntity;
import tfar.chickenvshunter.item.ChickenAxeItem;
import tfar.chickenvshunter.item.ChickenBowItem;
import tfar.chickenvshunter.item.ChickenPickaxeItem;

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
    public static Item CHICKEN_AXE = new ChickenAxeItem(Tiers.DIAMOND,0,0,new Item.Properties());
    public static Item CHICKEN_BOW = new ChickenBowItem(new Item.Properties().durability(384 * 4));
    public static Item CHICKEN_PICKAXE = new ChickenPickaxeItem(Tiers.DIAMOND,0,0,new Item.Properties());
    public static Item IRON_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.IRON);
    public static Item GOLD_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.GOLD);
    public static Item DIAMOND_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.DIAMOND);
    public static Item NETHERITE_SEEDS = new OpSeedItem(new Item.Properties(),OpSeedItem.NETHERITE);
    public static ChickenCompassItem CHICKEN_COMPASS = new ChickenCompassItem(new Item.Properties());
    public static EntityType<? extends Mob> GHICKEN;
    public static EntityType<? extends Fireball> GHICKEN_FIREBALL = EntityType.Builder.<GhickenFireballEntity>of(GhickenFireballEntity::new, MobCategory.MISC).build("ghicken_fireball");
    public static EntityType<ChickenArrowEntity> CHICKEN_ARROW = EntityType.Builder.<ChickenArrowEntity>of(ChickenArrowEntity::new, MobCategory.MISC)
            .sized(.5f,.5f).clientTrackingRange(4).updateInterval(20).build("chicken_arrow");

    public static EntityType<BlockBreakerEntity> BLOCK_BREAKER = EntityType.Builder.of(BlockBreakerEntity::new, MobCategory.MISC)
            .sized(.5f,.5f).clientTrackingRange(4).updateInterval(20).build("block_breaker");

    public static BlockEntityType<GoldenEggBlockEntity> GOLDEN_EGG_E = BlockEntityType.Builder.of(GoldenEggBlockEntity::new,GOLDEN_EGG).build(null);

    public static MobEffect CHICKEN_CURSE = new ChickenCurseEffect(MobEffectCategory.HARMFUL,0);

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
