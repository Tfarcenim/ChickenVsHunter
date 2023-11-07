package tfar.chickenvshunter;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GoldenEggBlockEntity extends BlockEntity {
    public GoldenEggBlockEntity(BlockPos $$1, BlockState $$2) {
        super(Init.GOLDEN_EGG_E, $$1, $$2);
    }


    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void serverTick() {
        timer--;
        setChanged();
    }

    private int timer;

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("timer",timer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        timer = tag.getInt("timer");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GoldenEggBlockEntity e) {
        e.serverTick();
    }
}
