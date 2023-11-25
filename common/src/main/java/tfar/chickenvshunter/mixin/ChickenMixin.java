package tfar.chickenvshunter.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chickenvshunter.RemoveSpecificBlockGoal;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.CustomFollowOwnerGoal;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import java.util.UUID;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends PathfinderMob implements ChickenDuck, OwnableEntity {

    private final NearestAttackableTargetGoal targetHunters = new NearestAttackableTargetGoal<>((Chicken)(Object)this, Player.class,true, entity ->
            entity instanceof Player player && ChickVHunterSavedData.isHunter(player));

    private final MeleeAttackGoal meleeAttack = new MeleeAttackGoal(this, 1.0D, true);


    private int reinforcementTime = ChickVHunterSavedData.REINFORCEMENT_DELAY;
    @Nullable
    private UUID ownerUUID;

    protected ChickenMixin(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "registerGoals",at = @At("RETURN"))
    private void addCustomGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(6,new CustomFollowOwnerGoal(this, 1, 10, 2, false));
        this.goalSelector.addGoal(4,new RemoveSpecificBlockGoal(this,1,3));
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

    public void reassessGoals() {
        if (this.level() != null && !this.level().isClientSide) {
            this.targetSelector.removeGoal(targetHunters);
            this.goalSelector.removeGoal(meleeAttack);
            if (ownerUUID != null) {
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
