package tfar.chickenvshunter.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.world.deferredevent.DespawnLater;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class GhickenEntity extends FlyingMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> ANIMATION = SynchedEntityData.defineId(GhickenEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHOULD_ANIMATION_CONTINUE = SynchedEntityData.defineId(GhickenEntity.class, EntityDataSerializers.BOOLEAN);

    private final int timer = 0;//server
    private boolean reset;//client



    public GhickenEntity(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new GhastMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (living) ->
                Math.abs(living.getY() - this.getY()) <= 4.0D));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericFlyController(this));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ANIMATION,0);
        entityData.define(SHOULD_ANIMATION_CONTINUE,false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public enum GeckoAnimation {
        NOTHING(Integer.MAX_VALUE), FIREBALL(20);

        private final int duration;

        GeckoAnimation(int duration) {

            this.duration = duration;
        }
    }

    public GeckoAnimation getAnimation() {
        return GeckoAnimation.values()[entityData.get(ANIMATION)];
    }

    public void setAnimation(GeckoAnimation animation) {
        entityData.set(ANIMATION,animation.ordinal());
    }

    private boolean shouldAnimationContinue() {
        return entityData.get(SHOULD_ANIMATION_CONTINUE);
    }

    private void setShouldAnimationContinue(boolean shouldAnimationContinue) {
        entityData.set(SHOULD_ANIMATION_CONTINUE,shouldAnimationContinue);
    }

    public boolean isAnimationDone() {
        if (getAnimation() == GeckoAnimation.NOTHING) return true;
        GeckoAnimation animation = getAnimation();
        return animation == null || timer >= animation.duration;
    }


    static class GhastMoveControl extends MoveControl {
        private final GhickenEntity ghast;
        private int floatDuration;

        public GhastMoveControl(GhickenEntity pGhast) {
            super(pGhast);
            this.ghast = pGhast;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.1D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }

            }
        }

        private boolean canReach(Vec3 pPos, int pLength) {
            AABB aabb = this.ghast.getBoundingBox();

            for(int i = 1; i < pLength; ++i) {
                aabb = aabb.move(pPos);
                if (!this.ghast.level().noCollision(this.ghast, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    int getExplosionPower() {
        return 1;
    }

    static class GhastShootFireballGoal extends Goal {
        private final GhickenEntity ghicken;
        public int chargeTime;

        public GhastShootFireballGoal(GhickenEntity ghicken) {
            this.ghicken = ghicken;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.ghicken.getTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.chargeTime = 0;
            ghicken.setAnimation(GeckoAnimation.FIREBALL);

        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            //this.ghast.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.ghicken.getTarget();
            if (livingentity != null) {
                double d0 = 64.0D;
                if (livingentity.distanceToSqr(this.ghicken) < d0 * d0 && this.ghicken.hasLineOfSight(livingentity)) {
                    Level level = this.ghicken.level();
                    ++this.chargeTime;
                    if (this.chargeTime == 10 && !this.ghicken.isSilent()) {
                        level.levelEvent(null, 1015, this.ghicken.blockPosition(), 0);
                        //ghicken.setAnimation();
                    }

                    if (this.chargeTime == 20) {
                        double d1 = 3D;
                        Vec3 vec3 = this.ghicken.getViewVector(1.0F);
                        double d2 = livingentity.getX() - (this.ghicken.getX() + vec3.x * d1);
                        double d3 = livingentity.getY(0.5D) - (0.5D + this.ghicken.getY(0.5D));
                        double d4 = livingentity.getZ() - (this.ghicken.getZ() + vec3.z * d1);
                        if (!this.ghicken.isSilent()) {
                            level.levelEvent(null, 1016, this.ghicken.blockPosition(), 0);
                        }

                        GhickenFireballEntity largefireball = new GhickenFireballEntity(level, this.ghicken, d2, d3, d4, this.ghicken.getExplosionPower());
                        largefireball.setPos(this.ghicken.getX() + vec3.x * d1, this.ghicken.getY(0.5D) + 0.5D, largefireball.getZ() + vec3.z * d1);
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }

                //this.ghicken.setCharging(this.chargeTime > 10);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide && level().getGameTime() % 1200 == 0) {

            ChunkPos $$2 = new ChunkPos(blockPosition());
            int $$4 = $$2.getMinBlockX();
            int $$5 = $$2.getMinBlockZ();

            BlockPos $$7 = findLightningTargetAround(level().getBlockRandomPos($$4, 0, $$5, 15));

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level());
            if (lightningBolt != null) {
                lightningBolt.moveTo(Vec3.atBottomCenterOf($$7));
                level().addFreshEntity(lightningBolt);

                Chicken chicken = EntityType.CHICKEN.create(this.level());
                if (chicken != null) {
                    chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    chicken.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("Rage Boost",.75, AttributeModifier.Operation.ADDITION));
                    ((ChickenDuck)chicken).reassessGoals();
                   // ChickenVsHunter.addDeferredEvent((ServerLevel) level(),new DespawnLater(120,chicken,.05));
                    this.level().addFreshEntity(chicken);
                }


            }
        }
    }

    protected BlockPos findLightningTargetAround(BlockPos pos) {
        BlockPos blockPos = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
      //  Optional<BlockPos> optional = ((ServerLevel)level()).findLightningRod(blockPos);
      //  if (optional.isPresent()) {
     //       return optional.get();
     //   }
        AABB aABB = new AABB(blockPos, new BlockPos(blockPos.getX(), level().getMaxBuildHeight(), blockPos.getZ())).inflate(3.0);
        List<LivingEntity> list = level().getEntitiesOfClass(LivingEntity.class, aABB, livingEntity -> livingEntity != null && livingEntity.isAlive() && level().canSeeSky(livingEntity.blockPosition()));
        if (!list.isEmpty()) {
            return list.get(level().random.nextInt(list.size())).blockPosition();
        }
        if (blockPos.getY() == level().getMinBuildHeight() - 1) {
            blockPos = blockPos.above(2);
        }
        return blockPos;
    }


    static class RandomFloatAroundGoal extends Goal {
        private final GhickenEntity ghicken;

        public RandomFloatAroundGoal(GhickenEntity pGhast) {
            this.ghicken = pGhast;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            MoveControl movecontrol = this.ghicken.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.ghicken.getX();
                double d1 = movecontrol.getWantedY() - this.ghicken.getY();
                double d2 = movecontrol.getWantedZ() - this.ghicken.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            RandomSource randomsource = this.ghicken.getRandom();
            double d0 = this.ghicken.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.ghicken.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.ghicken.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghicken.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
        }
    }

    static class GhastLookGoal extends Goal {
        private final GhickenEntity ghast;

        public GhastLookGoal(GhickenEntity pGhast) {
            this.ghast = pGhast;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 vec3 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI));
                this.ghast.yBodyRot = this.ghast.getYRot();
            } else {
                LivingEntity livingentity = this.ghast.getTarget();
                double d0 = 64.0D;
                if (livingentity.distanceToSqr(this.ghast) < 4096.0D) {
                    double d1 = livingentity.getX() - this.ghast.getX();
                    double d2 = livingentity.getZ() - this.ghast.getZ();
                    this.ghast.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
                    this.ghast.yBodyRot = this.ghast.getYRot();
                }
            }
        }
    }
}
