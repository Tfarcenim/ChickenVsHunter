package tfar.chickenvshunter;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ChickenCurseEffect extends MobEffect {
    public ChickenCurseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.getRandom().nextInt(100) == 0 && living instanceof ServerPlayer serverPlayer) {
            serverPlayer.drop(true);
        }
    }

    @Override
    public boolean isDurationEffectTick(int $$0, int amplifier) {
        return true;
    }
}
