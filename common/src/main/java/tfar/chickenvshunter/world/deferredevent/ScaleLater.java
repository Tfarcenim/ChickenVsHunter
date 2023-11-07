package tfar.chickenvshunter.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.platform.Services;

import java.util.UUID;

public class ScaleLater extends DeferredEvent {

    private UUID uuid;
    private float scale;

    public ScaleLater() {
        super(DeferredEventTypes.SCALE_LATER);
    }
    public ScaleLater(long timer,ServerPlayer player,float scale) {
        this();
        this.timer = timer;
        this.uuid = player.getUUID();
        this.scale = scale;
    }

    @Override
    public boolean attemptRun(ServerLevel level) {
        ServerPlayer player = level.getServer().getPlayerList().getPlayer(uuid);
        if (player != null) {
            Services.PLATFORM.scalePlayer(player, scale);
            return true;
        } else {
            ChickenVsHunter.LOG.warn("Player was null?");
            return true;
        }
    }

    @Override
    public void writeWithoutMetaData(CompoundTag tag) {
        tag.putUUID("player",uuid);
        tag.putFloat("scale",scale);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        uuid = tag.getUUID("player");
        scale = tag.getFloat("scale");
    }
}
