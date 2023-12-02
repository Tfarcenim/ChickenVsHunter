package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public abstract class LevelDeferredEvent {

    protected long timer;
    private final DeferredEventType<?> deferredEventType;

    public LevelDeferredEvent(DeferredEventType<?> deferredEventType) {
        this.deferredEventType = deferredEventType;
    }

    public final void tick() {
        timer--;
    }

    public boolean isReady() {
        return timer <= 0;
    }

    public final CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id",type().getId().toString());
        tag.putLong("timer",timer);
        writeWithoutMetaData(tag);
        return tag;
    }
    public abstract void writeWithoutMetaData(CompoundTag tag);

    public abstract boolean attemptRun(ServerLevel level);


    protected final DeferredEventType<?> type() {
        return deferredEventType;
    }

    public final void load(CompoundTag tag) {
        timer = tag.getLong("timer");
        loadAdditional(tag);
    }

    public abstract void loadAdditional(CompoundTag tag);

}
