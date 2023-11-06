package tfar.chickenvshunter;

import net.minecraft.client.model.geom.ModelPart;

public class ChickenVsHunterClient {

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
