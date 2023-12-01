package tfar.chickenvshunter.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chickenvshunter.ducks.ChickenDuck;

import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "collideBoundingBox",at = @At("RETURN"),cancellable = true)
    private static void noCollide(Entity entity, Vec3 $$1, AABB $$2, Level $$3, List<VoxelShape> $$4, CallbackInfoReturnable<Vec3> cir) {
        if (entity instanceof Chicken chicken) {
            if (((ChickenDuck)chicken).getBlocksLeft()> 0) {
                cir.setReturnValue($$1);
            }
        }
    }
}
