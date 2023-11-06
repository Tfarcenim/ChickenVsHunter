package tfar.chickenvshunter.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import tfar.chickenvshunter.Init;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
        createNonTemplateModelBlock(Init.GOLDEN_EGG);
    }

}
