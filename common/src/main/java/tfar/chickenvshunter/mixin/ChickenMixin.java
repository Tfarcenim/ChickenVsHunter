package tfar.chickenvshunter.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.CustomFollowOwnerGoal;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import java.util.UUID;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends PathfinderMob implements ChickenDuck, OwnableEntity {

    @Shadow public int eggTime;
    private final NearestAttackableTargetGoal targetHunters = new NearestAttackableTargetGoal<>((Chicken)(Object)this, Player.class,true, entity ->
            entity instanceof Player player && ChickVHunterSavedData.isHunter(player));

    private final NearestAttackableTargetGoal targetEverything = new NearestAttackableTargetGoal<>((Chicken)(Object)this, LivingEntity.class,true, ChickenMixin::isNotSpeedrunnerOrChicken);

    private final MeleeAttackGoal meleeAttack = new MeleeAttackGoal(this, 1.0D, true);


    private static boolean isNotSpeedrunnerOrChicken(LivingEntity entity) {
        if (entity.getUUID().equals(ChickVHunterSavedData.speedrunner)) {
            return false;
        }
        if (entity instanceof Chicken) {
            return false;
        }
        return true;
    }

    private int blocksLeft = 0;

    @Override
    public void setBlockBreaker() {
        blocksLeft = 200;
    }

    @Override
    public int getBlocksLeft() {
        return blocksLeft;
    }

    @Override
    public void setBlocksLeft(int blocksLeft) {
        this.blocksLeft = blocksLeft;
    }

    private boolean superHostile = true;

    private int reinforcementTime = ChickVHunterSavedData.REINFORCEMENT_DELAY;
    @Nullable
    private UUID ownerUUID;

    protected ChickenMixin(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At("RETURN"))
    private void addCustomGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(6,new CustomFollowOwnerGoal(this, 1, 10, 2, false));
    }

    private static final UUID rage_boost = UUID.fromString("04909418-c96c-48b3-a3ba-e9bcb4bbeafc");

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        if (!level().isClientSide && ChickenVsHunter.rageMode((ServerLevel) level())) {
            if (getAttribute(Attributes.MOVEMENT_SPEED).getModifier(rage_boost) == null) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(rage_boost, "Rage Boost", .5, AttributeModifier.Operation.ADDITION));
            } else {
                this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(rage_boost);
            }
        }
    }

    @Inject(method = "createAttributes",at = @At("RETURN"))
    private static void modifyAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(Attributes.ATTACK_DAMAGE,2);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        if (ownerUUID != null) {
            $$0.putUUID("owner",ownerUUID);
        }
        reassessGoals();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.hasUUID("owner")) {
            ownerUUID = $$0.getUUID("owner");
        }
        reassessGoals();
    }

    @Override
    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        reassessGoals();
    }

    @Override
    public int getReinforcementTime() {
        return reinforcementTime;
    }

    @Override
    public void setReinforcementTime(int time) {
        reinforcementTime = time;
    }

    @Override
    public void reassessGoals() {
        if (this.level() != null && !this.level().isClientSide) {
            this.targetSelector.removeGoal(targetHunters);
            this.goalSelector.removeGoal(meleeAttack);
            this.targetSelector.removeGoal(targetEverything);

            if (superHostile) {
                targetSelector.addGoal(2,targetEverything);
                this.goalSelector.addGoal(3,meleeAttack);
            } else if (ownerUUID != null) {
                this.targetSelector.addGoal(4, this.targetHunters);
                this.goalSelector.addGoal(5,meleeAttack);
            }
        }
    }


    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
}
