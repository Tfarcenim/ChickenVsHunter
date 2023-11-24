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
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import javax.annotation.Nullable;

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
        ChickVHunterSavedData.toBreak.remove(0);
    }

    @Override
    public void tick() {
        super.tick();
        Level $$0 = this.removerMob.level();
        BlockPos $$1 = this.removerMob.blockPosition();
        BlockPos $$2 = this.getPosWithBlock($$1, $$0);
        RandomSource $$3 = this.removerMob.getRandom();
        if (this.isReachedTarget() && $$2 != null) {
            if (this.ticksSinceReachedGoal > 0) {
                Vec3 $$4 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement($$4.x, 0.3, $$4.z);
                if (!$$0.isClientSide) {
                    double $$5 = 0.08;
                    ((ServerLevel)$$0)
                            .sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)),
                                    (double)$$2.getX() + 0.5,
                                    (double)$$2.getY() + 0.7,
                                    (double)$$2.getZ() + 0.5,
                                    3,
                                    ((double)$$3.nextFloat() - 0.5) * 0.08,
                                    ((double)$$3.nextFloat() - 0.5) * 0.08,
                                    ((double)$$3.nextFloat() - 0.5) * 0.08,
                                    0.15F
                            );
                }
            }

            if (this.ticksSinceReachedGoal % 2 == 0) {
                Vec3 $$6 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement($$6.x, -0.3, $$6.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound($$0, this.blockPos);
                }
            }

            if (this.ticksSinceReachedGoal > 60) {
                $$0.removeBlock($$2, false);
                if (!$$0.isClientSide) {
                    for(int $$7 = 0; $$7 < 20; ++$$7) {
                        double $$8 = $$3.nextGaussian() * 0.02;
                        double $$9 = $$3.nextGaussian() * 0.02;
                        double $$10 = $$3.nextGaussian() * 0.02;
                        ((ServerLevel)$$0)
                                .sendParticles(
                                        ParticleTypes.POOF, (double)$$2.getX() + 0.5, $$2.getY(), (double)$$2.getZ() + 0.5, 1, $$8, $$9, $$10, 0.15F
                                );
                    }

                    this.playBreakSound($$0, $$2);
                }
            }

            ++this.ticksSinceReachedGoal;
        }
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos $$0, BlockGetter $$1) {
        return $$0;
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
