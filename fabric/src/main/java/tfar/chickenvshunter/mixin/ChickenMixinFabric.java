package tfar.chickenvshunter.mixin;

import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chickenvshunter.ChickenVsHunter;

@Mixin(Chicken.class)
public class ChickenMixinFabric {

    @Inject(method = "aiStep",at = @At("HEAD"))
    private void chickenTickEvent(CallbackInfo ci) {
        ChickenVsHunter.chickenTick((Chicken) (Object) this);
    }
}
