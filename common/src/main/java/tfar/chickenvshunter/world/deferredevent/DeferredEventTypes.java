package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.resources.ResourceLocation;
import tfar.chickenvshunter.ChickenVsHunter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DeferredEventTypes {

    public static final Map<ResourceLocation,DeferredEventType<?>> REGISTRY = new HashMap<>();

    public static final DeferredEventType<ScaleLater> SCALE_LATER =
            register(ScaleLater::new, new ResourceLocation(ChickenVsHunter.MOD_ID,"scale_later"));
    public static final DeferredEventType<DespawnLater> DESPAWN_LATER =
            register(DespawnLater::new, new ResourceLocation(ChickenVsHunter.MOD_ID,"despawn_later"));

    public static <T extends DeferredEvent> DeferredEventType<T> register(Supplier<T> function, ResourceLocation resourceLocation) {
        DeferredEventType<T> deferredEventType = new DeferredEventType<>(function,resourceLocation);
        REGISTRY.put(resourceLocation,deferredEventType);
        return deferredEventType;
    }
}
