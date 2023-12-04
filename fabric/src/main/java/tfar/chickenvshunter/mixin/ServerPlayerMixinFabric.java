package tfar.chickenvshunter.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chickenvshunter.ChickenVsHunter;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixinFabric {

    @Inject(method = "restoreFrom",at = @At("RETURN"))
    private void fabricCloneEvent(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        ChickenVsHunter.playerClone((ServerPlayer)(Object) this,that,keepEverything);
    }
}
