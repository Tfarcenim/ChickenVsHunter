package tfar.chickenvshunter.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tfar.chickenvshunter.Init;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        makeSeed(Init.IRON_SEEDS,Items.IRON_INGOT,pWriter);
        makeSeed(Init.GOLD_SEEDS,Items.GOLD_INGOT,pWriter);
        makeSeed(Init.DIAMOND_SEEDS,Items.DIAMOND,pWriter);
        makeSeed(Init.NETHERITE_SEEDS,Items.NETHERITE_INGOT,pWriter);
    }

    protected void makeSeed(Item seed,Item input,Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD,seed).define('#', Items.WHEAT_SEEDS).define('X', input)
                .pattern("###").pattern("#X#").pattern("###")
                .unlockedBy("has_"+ BuiltInRegistries.ITEM.getKey(input).getPath(), has(input)).save(pWriter);
    }
}
