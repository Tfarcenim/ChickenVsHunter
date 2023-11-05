package tfar.chickenvshunter.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.Init;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static net.minecraft.data.models.BlockModelGenerators.createSimpleBlock;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        generateArmorTrims(Init.CHICKEN_HELMET);
        generateArmorTrims(Init.CHICKEN_CHESTPLATE);
        generateArmorTrims(Init.CHICKEN_LEGGINGS);
        generateArmorTrims(Init.CHICKEN_BOOTS);

  //      generateChickItem(Init.CHICKEN_PICKAXE,"",CHICK_PICK);
    }



 //   public static final ModelTemplate CHICK_PICK = create(new ResourceLocation(ChickenVsHunter.MOD_ID,"chicken_pickaxe"), TextureSlot.TEXTURE);

   // public void generateChickItem(Item pItem, String pModelLocationSuffix, ModelTemplate pModelTemplate) {
  //      pModelTemplate.create(ModelLocationUtils.getModelLocation(pItem, pModelLocationSuffix), TextureMapping.layer0(TextureMapping.getItemTexture(pItem, pModelLocationSuffix)), this.output);
  //  }


  //  private static ModelTemplate create(ResourceLocation pBlockModelLocation, TextureSlot... pRequiredSlots) {
  //      return new ModelTemplate(Optional.of(new ResourceLocation(pBlockModelLocation.getNamespace(), "block/" + pBlockModelLocation.getPath())), Optional.empty(), pRequiredSlots);
  //  }


}
