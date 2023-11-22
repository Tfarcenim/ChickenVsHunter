package tfar.chickenvshunter.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.platform.Services;
import tfar.chickenvshunter.world.ChickVHunterSavedData;
import tfar.chickenvshunter.world.deferredevent.ScaleLater;

public class ChickenAxeItem extends AxeItem {
    public ChickenAxeItem(Tier $$0, float $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity livingentity, InteractionHand hand) {
        if (player.getUUID().equals(ChickVHunterSavedData.speedrunner) && livingentity instanceof Player hunter) {
            if (!player.level().isClientSide) {
                Services.PLATFORM.scalePlayer((ServerPlayer) hunter, .5f);
                ScaleLater scaleLater = new ScaleLater(300, (ServerPlayer) hunter,2);
                ChickenVsHunter.addDeferredEvent((ServerLevel) hunter.level(),scaleLater);
            }
            return InteractionResult.sidedSuccess(!player.level().isClientSide);
        }
        return super.interactLivingEntity(stack, player, livingentity, hand);
    }
}
