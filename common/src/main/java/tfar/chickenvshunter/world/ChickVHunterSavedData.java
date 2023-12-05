package tfar.chickenvshunter.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChickVHunterSavedData extends SavedData {

    public static UUID speedrunner;
    public static UUID chicken;
    public static int REINFORCEMENT_DELAY = 20 * 40;

    public static boolean isHunter(Player player) {
        return !player.getUUID().equals(speedrunner);
    }

    @Override
    public CompoundTag save(CompoundTag var1) {
        return null;
    }
}
