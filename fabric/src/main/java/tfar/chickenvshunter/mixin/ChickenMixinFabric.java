package tfar.chickenvshunter.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chickenvshunter.ChickenVsHunter;

@Mixin(Chicken.class)
public class ChickenMixinFabric extends PathfinderMob {

    protected ChickenMixinFabric(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    private void addFireResist(EntityType entityType, Level level, CallbackInfo ci) {
        addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,-1,0,false,false));
    }

    @Inject(method = "aiStep",at = @At("HEAD"))
    private void chickenTickEvent(CallbackInfo ci) {
        ChickenVsHunter.chickenTick((Chicken) (Object) this);
    }
}
