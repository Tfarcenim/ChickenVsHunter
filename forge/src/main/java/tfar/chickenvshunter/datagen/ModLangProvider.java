package tfar.chickenvshunter.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.speedrunnervshuntersculk.Init;
import tfar.speedrunnervshuntersculk.SpeedrunnerVsHunterSculk;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, SpeedrunnerVsHunterSculk.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Init.SPEEDRUNNER_SCULK_SENSOR_BLANK,"Speedrunner Sculk Sensor");
        add(Init.SPEEDRUNNER_SCULK_SENSOR_HEALTH,"Speedrunner Sculk Sensor Health");
        add(Init.SPEEDRUNNER_SCULK_SENSOR_REACH,"Speedrunner Sculk Sensor Reach");
        add(Init.SPEEDRUNNER_SCULK_SENSOR_KNOCKBACK,"Speedrunner Sculk Sensor Knockback");
        add(Init.SPEEDRUNNER_SCULK_SENSOR_VOID,"Speedrunner Sculk Sensor Void");
    }
}
