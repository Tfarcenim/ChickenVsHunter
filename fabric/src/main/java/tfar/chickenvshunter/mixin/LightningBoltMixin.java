package tfar.chickenvshunter.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.chickenvshunter.entity.GhickenEntity;

import java.util.List;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @Inject(method = "tick",at = @At(value = "INVOKE",target = "Ljava/util/List;iterator()Ljava/util/Iterator;"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void modifyLightning(CallbackInfo ci, List<Entity> list) {
        onLightningHits((LightningBolt)(Object) this,list);
    }

    private static void onLightningHits(LightningBolt lightningBolt, List<Entity> list) {
        list.removeIf(entity -> entity instanceof Chicken || entity instanceof GhickenEntity);
    }
}
