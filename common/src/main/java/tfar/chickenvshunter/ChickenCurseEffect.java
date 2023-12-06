package tfar.chickenvshunter;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ChickenCurseEffect extends MobEffect {
    public ChickenCurseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.getRandom().nextInt(1000) == 0 && living instanceof ServerPlayer serverPlayer) {
            serverPlayer.drop(true);
            serverPlayer.playSound(SoundEvents.CHICKEN_AMBIENT);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, serverPlayer.getInventory().selected, ItemStack.EMPTY));
        }
    }

    @Override
    public boolean isDurationEffectTick(int $$0, int amplifier) {
        return true;
    }
}
