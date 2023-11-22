package tfar.chickenvshunter.mixin;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrownEgg.class)
public class ThrownEggMixin {

    @Inject(method = "onHit",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void hitEvent(HitResult $$0, CallbackInfo ci, int $$1, int $$2, Chicken chicken) {

    }
}
