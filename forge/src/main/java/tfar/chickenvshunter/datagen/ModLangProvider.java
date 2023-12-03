package tfar.chickenvshunter.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.Init;


public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, ChickenVsHunter.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Init.GOLDEN_EGG,"Golden Egg");
        add(Init.CHICKEN_HELMET,"Chicken Helmet");
        add(Init.CHICKEN_CHESTPLATE,"Chicken Chestplate");
        add(Init.CHICKEN_LEGGINGS,"Chicken Leggings");
        add(Init.CHICKEN_BOOTS,"Chicken Boots");

        add(Init.IRON_SEEDS,"Iron Seeds");
        add(Init.GOLD_SEEDS,"Gold Seeds");
        add(Init.DIAMOND_SEEDS,"Diamond Seeds");
        add(Init.NETHERITE_SEEDS,"Netherite Seeds");

        add(Init.GHICKEN,"Ghicken");
        add(Init.GHICKEN_FIREBALL,"Ghicken Fireball");
        add(Init.CHICKEN_CURSE,"Chicken Curse");

    }
}
