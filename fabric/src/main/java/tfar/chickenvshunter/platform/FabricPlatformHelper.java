package tfar.chickenvshunter.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void scalePlayer(ServerPlayer player, float scale) {
        final ScaleData scaleData = ScaleTypes.BASE.getScaleData(player);
        scaleData.setScaleTickDelay(10);
        scaleData.setTargetScale(scaleData.getTargetScale() * scale);
    }
}
