package tfar.chickenvshunter.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chickenvshunter.ChickenVsHunter;

@Mixin(Player.class)
public class PlayerMixinFabric {

    @Inject(method = "tick",at = @At("HEAD"))
    private void playerStartTick(CallbackInfo ci) {
        ChickenVsHunter.playerTick((Player) (Object)this);
    }

    @Inject(method = "actuallyHurt",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"),cancellable = true)
    private void onDamaged(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        if (ChickenVsHunter.onDamaged((Player)(Object)this,damageAmount,damageSource)) {
            ci.cancel();
        }
    }

}
