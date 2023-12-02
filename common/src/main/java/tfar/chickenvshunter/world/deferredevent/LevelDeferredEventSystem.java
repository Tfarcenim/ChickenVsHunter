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

public class LevelDeferredEventSystem extends SavedData {

    public List<LevelDeferredEvent> futureEvents = new ArrayList<>();

    public List<LevelDeferredEvent> oldEvents = new ArrayList<>();

    public void addDeferredEvent(LevelDeferredEvent levelDeferredEvent) {
        futureEvents.add(levelDeferredEvent);
    }

    public void tickDeferredEvents(ServerLevel level) {
        for (LevelDeferredEvent levelDeferredEvent : futureEvents) {
            levelDeferredEvent.tick();
            if (levelDeferredEvent.isReady()) {
                boolean finished = levelDeferredEvent.attemptRun(level);
                if (finished) {
                    oldEvents.add(levelDeferredEvent);
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
        for (LevelDeferredEvent levelDeferredEvent : futureEvents) {
            listTag.add(levelDeferredEvent.write());
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
            LevelDeferredEvent levelDeferredEvent = deferredEventType.createFromTag(compoundTag);
            futureEvents.add(levelDeferredEvent);
        }
    }

    public static LevelDeferredEventSystem loadStatic(CompoundTag compoundTag) {
        LevelDeferredEventSystem id = new LevelDeferredEventSystem();
        id.load(compoundTag);
        return id;
    }


}
