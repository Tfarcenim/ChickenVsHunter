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

    }
}
