package tfar.chickenvshunter.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import tfar.chickenvshunter.ducks.PlayerDuck;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity implements PlayerDuck{

    protected PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public double getPassengersRidingOffset() {
        return getBbHeight();//fix bounding box clipping into player's face
    }
}
