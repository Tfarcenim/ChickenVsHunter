package tfar.chickenvshunter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import java.util.List;

public class RemoveSpecificBlockGoal extends MoveToBlockGoal {
    private final Mob removerMob;
    private int ticksSinceReachedGoal;
    private static final int WAIT_AFTER_BLOCK_FOUND = 20;

    public RemoveSpecificBlockGoal(PathfinderMob $$1, double $$2, int $$3) {
        super($$1, $$2, 24, $$3);
        this.removerMob = $$1;
    }

    @Override
    public boolean canUse() {
        if (!this.removerMob.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else if (this.findNearestBlock()) {
            this.nextStartTick = reducedTickDelay(20);
            return true;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return false;
        }
    }

    @Override
    public double acceptedDistance() {
        return 2;
    }

    @Override
    protected boolean findNearestBlock() {
        if (ChickVHunterSavedData.toBreak.isEmpty()) return false;
        blockPos = ChickVHunterSavedData.toBreak.get(0);
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        this.removerMob.fallDistance = 1.0F;
    }

    @Override
    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
    }

    public void playDestroyProgressSound(LevelAccessor $$0, BlockPos $$1) {
    }

    public void playBreakSound(Level $$0, BlockPos $$1) {

    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.removerMob.level();
        BlockPos mobPos = this.removerMob.blockPosition();
        BlockPos pos = this.getPosWithBlock(mobPos, level);
        if (this.isReachedTarget() && pos != null) {
            if (this.ticksSinceReachedGoal > 0) {
                giveLootAndRemoveBlock(level,pos);
                if (!ChickVHunterSavedData.toBreak.isEmpty()) {
                    ChickVHunterSavedData.toBreak.remove(0);
                }
                Vec3 $$4 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement($$4.x, 0.3, $$4.z);
            }
            ++this.ticksSinceReachedGoal;
        }
    }

    protected void giveLootAndRemoveBlock(Level level,BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        List<ItemStack> loot = Block.getDrops(state, (ServerLevel) level,pos,level.getBlockEntity(pos),null,ItemStack.EMPTY);

        ServerPlayer speedrunner = (ServerPlayer) ((ServerLevel) level).getEntity(ChickVHunterSavedData.speedrunner);

        if (speedrunner != null) {
            for (ItemStack stack : loot) {
                speedrunner.addItem(stack);
            }
        }

        level.removeBlock(pos, false);
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos pos, BlockGetter level) {
        if (!level.getBlockState(pos).isAir()) {
            return pos;
        }
        return null;
    }

    @Override
    protected boolean isValidTarget(LevelReader $$0, BlockPos pos) {
        ChunkAccess $$2 = $$0.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        if ($$2 == null) {
            return false;
        } else {
            return $$2.getBlockState(pos.above()).isAir() && $$2.getBlockState(pos.above(2)).isAir();
        }
    }
}
