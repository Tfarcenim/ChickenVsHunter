package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DeferredEventType<T extends LevelDeferredEvent> {

    private final Supplier<T> supplier;
    private final ResourceLocation id;

    public DeferredEventType(Supplier<T> supplier, ResourceLocation id) {
        this.supplier = supplier;
        this.id = id;
    }

    public T createFromTag(CompoundTag tag) {
        T deferredEvent = supplier.get();
        deferredEvent.load(tag);
        return deferredEvent;
    }

    public ResourceLocation getId() {
        return id;
    }
}
