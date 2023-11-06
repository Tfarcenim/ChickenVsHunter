package tfar.chickenvshunter.mixin;

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
}
