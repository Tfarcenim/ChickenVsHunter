package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import tfar.chickenvshunter.ChickenVsHunter;

import java.util.ArrayList;
import java.util.List;

public class DeferredEventSystem extends SavedData {

    public List<DeferredEvent> futureEvents = new ArrayList<>();

    public List<DeferredEvent> oldEvents = new ArrayList<>();

    public void addDeferredEvent(DeferredEvent deferredEvent) {
        futureEvents.add(deferredEvent);
    }

    public void tickDeferredEvents(ServerLevel level) {
        for (DeferredEvent deferredEvent : futureEvents) {
            deferredEvent.tick();
            if (deferredEvent.isReady()) {
                boolean worked = deferredEvent.attemptRun(level);
                if (worked) {
                    oldEvents.add(deferredEvent);
                }
            }
        }
        futureEvents.removeAll(oldEvents);
        oldEvents.clear();
        if (!futureEvents.isEmpty()) {
            setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (DeferredEvent deferredEvent : futureEvents) {
            listTag.add(deferredEvent.write());
        }
        tag.put("deferredevents",listTag);
        return tag;
    }

    public void load(CompoundTag tag) {
        ListTag listTag = tag.getList("deferredevents", Tag.TAG_COMPOUND);
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            String id = compoundTag.getString("id");
            DeferredEventType<?> deferredEventType = DeferredEventTypes.REGISTRY.get(new ResourceLocation(id));
            if (deferredEventType == null) {
                ChickenVsHunter.LOG.warn("Unregistered deferred event: "+id);
                continue;
            }
            DeferredEvent deferredEvent = deferredEventType.createFromTag(compoundTag);
            futureEvents.add(deferredEvent);
        }
    }

    public static DeferredEventSystem loadStatic(CompoundTag compoundTag) {
        DeferredEventSystem id = new DeferredEventSystem();
        id.load(compoundTag);
        return id;
    }


}
