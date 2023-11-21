package tfar.chickenvshunter.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chickenvshunter.Init;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixinFabric {

    @Inject(method = "renderArmWithItem",at = @At("HEAD"),cancellable = true)
    private void renderArmEvent(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (player.getFirstPassenger() instanceof Chicken && !player.getItemBySlot(EquipmentSlot.HEAD).is(Init.CHICKEN_HELMET)) {
            ci.cancel();
        }
    }
}
