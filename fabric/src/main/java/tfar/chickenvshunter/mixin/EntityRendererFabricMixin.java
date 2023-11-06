package tfar.chickenvshunter.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererFabricMixin {

  //  @ModifyVariable(method = "renderNameTag",at = @At("HEAD"),argsOnly = true)
   // private Component setHealthName() {

  //  }

}
