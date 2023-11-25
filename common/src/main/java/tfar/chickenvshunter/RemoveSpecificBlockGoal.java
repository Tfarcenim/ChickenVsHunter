package tfar.chickenvshunter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

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
        BlockPos $$2 = this.getPosWithBlock(mobPos, level);
        RandomSource $$3 = this.removerMob.getRandom();
        if (this.isReachedTarget() && $$2 != null) {
            if (this.ticksSinceReachedGoal > 0) {
                Vec3 $$4 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement($$4.x, 0.3, $$4.z);
                if (!level.isClientSide) {
                    double d0 = 0.08;
                    ((ServerLevel)level)
                            .sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)),
                                    (double)$$2.getX() + 0.5,
                                    (double)$$2.getY() + 0.7,
                                    (double)$$2.getZ() + 0.5,
                                    3,
                                    ((double)$$3.nextFloat() - 0.5) * d0,
                                    ((double)$$3.nextFloat() - 0.5) * d0,
                                    ((double)$$3.nextFloat() - 0.5) * d0,
                                    0.15F
                            );
                }
            }

            if (this.ticksSinceReachedGoal % 2 == 0) {
                Vec3 $$6 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement($$6.x, -0.3, $$6.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound(level, this.blockPos);
                }
            }

            if (this.ticksSinceReachedGoal > 15) {
                level.removeBlock($$2, false);
                if (!level.isClientSide) {
                    for(int $$7 = 0; $$7 < 20; ++$$7) {
                        double $$8 = $$3.nextGaussian() * 0.02;
                        double $$9 = $$3.nextGaussian() * 0.02;
                        double $$10 = $$3.nextGaussian() * 0.02;
                        ((ServerLevel)level)
                                .sendParticles(
                                        ParticleTypes.POOF, (double)$$2.getX() + 0.5, $$2.getY(), (double)$$2.getZ() + 0.5, 1, $$8, $$9, $$10, 0.15F
                                );
                    }

                    if (!ChickVHunterSavedData.toBreak.isEmpty()) {
                        ChickVHunterSavedData.toBreak.remove(0);
                    }

                    this.playBreakSound(level, $$2);
                }
            }

            ++this.ticksSinceReachedGoal;
        }
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos pos, BlockGetter level) {
        if (!level.getBlockState(pos).isAir()) {
            return pos;
        }
        BlockPos[] blockPoss =new BlockPos[]{pos.below(), pos.west(), pos.east(), pos.north(), pos.south(), pos.below().below()};
        for (BlockPos blockPos : blockPoss) {
            if (level.getBlockState(blockPos).isAir()) continue;
            return blockPos;
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
