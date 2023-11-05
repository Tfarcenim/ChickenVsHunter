package tfar.chickenvshunter;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class Init {

    public static ArmorItem CHICKEN_HELMET;
    public static ArmorItem CHICKEN_CHESTPLATE;
    public static ArmorItem CHICKEN_LEGGINGS;
    public static ArmorItem CHICKEN_BOOTS;
    public static Block GOLDEN_EGG = new GoldenEggBlock(BlockBehaviour.Properties.copy(Blocks.DRAGON_EGG));
    public static BlockItem GOLDEN_EGG_I = new BlockItem(GOLDEN_EGG,new Item.Properties());

}
