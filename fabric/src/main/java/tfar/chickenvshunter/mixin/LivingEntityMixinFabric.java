package tfar.chickenvshunter.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chickenvshunter.ChickenVsHunter;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixinFabric extends Entity {

    @Shadow public abstract boolean hasEffect(MobEffect $$0);

    @Shadow public abstract void readAdditionalSaveData(CompoundTag $$0);

    public LivingEntityMixinFabric(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }



    @Inject(method = "causeFallDamage",at = @At("HEAD"),cancellable = true)
    private void onFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (ChickenVsHunter.onFallDamage(selfCast())) {
            cir.setReturnValue(false);
        }
    }

    /*@ModifyVariable(method = "knockback",at = @At(value = "HEAD"),argsOnly = true,ordinal = 0)
    private double adjustKnockback(double strength, double x, double z) {
        double newStrength = ArmorTrimsMod.onKnockback(strength, selfCast());
        return newStrength;
    }

        @Inject(method = "getVisibilityPercent",at = @At("RETURN"),cancellable = true)
    private void modifyVisibility(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
        if (hasEffect(MobEffects.INVISIBILITY)) {
            cir.setReturnValue(0d);
        }
    }

    @Inject(method = "hurt",at = @At("HEAD"),cancellable = true)
    private void onAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ArmorTrimsMod.attackEvent(selfCast(),source)) {
            cir.setReturnValue(false);
        }
    }*/

    @Inject(method = "heal",at = @At("HEAD"),cancellable = true)
    private void healEvent(float healAmount, CallbackInfo ci) {
        if (ChickenVsHunter.onHeal(selfCast(),healAmount)) {
            ci.cancel();
        }
    }

    @Inject(method = "actuallyHurt",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"),cancellable = true)
    private void onDamaged(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        if (ChickenVsHunter.onDamaged(selfCast(),damageAmount,damageSource)) {
            ci.cancel();
        }
    }



    @Unique
    private LivingEntity selfCast() {
        return (LivingEntity) (Object)this;
    }
}
