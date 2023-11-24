package tfar.chickenvshunter.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

public class ChickenPickaxeItem extends PickaxeItem {
    public ChickenPickaxeItem(Tier $$0, int $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand $$2) {

        if (!level.isClientSide) {
            ChickVHunterSavedData.createBreakablePos((ServerPlayer) player);
        }


        return InteractionResultHolder.sidedSuccess(player.getItemInHand($$2),level.isClientSide);
    }
}
