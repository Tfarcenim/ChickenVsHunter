package tfar.chickenvshunter;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class ChickenVsHunterClient {

    public static KeyMapping PICKUP_CHICKEN = new KeyMapping("pickup_chicken", GLFW.GLFW_KEY_I,"key.categories.chickenvshunter");

    public static void setup() {
        ItemProperties.register(Init.CHICKEN_COMPASS, new ResourceLocation("angle"), new CompassItemPropertyFunction((p_234983_, p_234984_, entity) -> {
            return Init.CHICKEN_COMPASS.pos;
        }));

        ItemProperties.register(
                Init.CHICKEN_BOW, new ResourceLocation("pulling"), ($$0x, $$1, $$2, $$3) -> $$2 != null && $$2.isUsingItem() && $$2.getUseItem() == $$0x ? 1.0F : 0.0F
        );

        ItemProperties.register(Init.CHICKEN_BOW, new ResourceLocation("pull"), ($$0x, $$1, $$2, $$3) -> {
            if ($$2 == null) {
                return 0.0F;
            } else {
                return $$2.getUseItem() != $$0x ? 0.0F : (float)($$0x.getUseDuration() - $$2.getUseItemRemainingTicks()) / 20.0F;
            }
        });
    }

    public static void adjustArms(ModelPart leftArm,ModelPart rightArm) {
        float x = (float) Math.PI;
        float z = 0.05f;

        changeRotation(rightArm, -x, 0, -z);
        changeRotation(leftArm, -x, 0, z);
    }

    private static void changeRotation(ModelPart part, float x, float y, float z) {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }



}
