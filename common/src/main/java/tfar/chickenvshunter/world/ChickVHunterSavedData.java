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
    public static List<BlockPos> toBreak = new ArrayList<>();

    public static void createBreakablePos(ServerPlayer player) {
        HitResult pick = player.pick(20,0,false);
        Vec3 location = pick.getLocation();
        BlockPos start = ((BlockHitResult)pick).getBlockPos();
        toBreak.clear();
        toBreak.add(start);
        //for (int i = 0; i < 10; i++) {
      //      toBreak.add(start.offset(i,0,0));
      //  }
    }

    public static boolean isHunter(Player player) {
        return !player.getUUID().equals(speedrunner);
    }

    @Override
    public CompoundTag save(CompoundTag var1) {
        return null;
    }
}
