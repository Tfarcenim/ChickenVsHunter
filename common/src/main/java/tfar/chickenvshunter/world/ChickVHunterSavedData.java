package tfar.chickenvshunter.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

public class ChickVHunterSavedData extends SavedData {

    public static UUID speedrunner;
    public static UUID chicken;
    public static int REINFORCEMENT_DELAY = 20 * 40;

    public static boolean isHunter(Player player) {
        return !isSpeedrunner(player);
    }

    public static boolean isSpeedrunner(Player player) {
        return player.getUUID().equals(speedrunner);
    }

    public static boolean isGameActive() {
        return speedrunner != null;
    }

    @Override
    public CompoundTag save(CompoundTag var1) {
        return null;
    }
}
