package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DespawnLater extends LevelDeferredEvent {
    private double explode;
    private UUID uuid;
    public DespawnLater() {
        super(DeferredEventTypes.DESPAWN_LATER);
    }

    public DespawnLater(long timer, Entity entity,double explode) {
        this();
        this.explode = explode;
        this.timer = timer;
        this.uuid = entity.getUUID();
    }

    @Override
    public boolean attemptRun(ServerLevel level) {
        Entity entity = level.getEntity(uuid);
        if (entity != null) {
            entity.discard();
            if (Math.random() < explode) {
                entity.level().explode(entity,entity.getX(),entity.getY(),entity.getZ(),4, Level.ExplosionInteraction.TNT);
            }
        }
        return true;
    }

    @Override
    public void writeWithoutMetaData(CompoundTag tag) {
        tag.putUUID("entity",uuid);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        uuid = tag.getUUID("entity");
    }
}
