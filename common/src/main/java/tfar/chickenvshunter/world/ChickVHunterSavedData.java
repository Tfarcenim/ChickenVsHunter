package tfar.chickenvshunter.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ChickVHunterSavedData extends SavedData {

    public static UUID speedrunner;

    public static Vec3 chicken_location = Vec3.ZERO;

    @Override
    public CompoundTag save(CompoundTag var1) {
        return null;
    }
}
