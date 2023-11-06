package tfar.chickenvshunter;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChickenCompassItem extends Item {
    public ChickenCompassItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack $$0, Player $$1, LivingEntity $$2, InteractionHand $$3) {
        return super.interactLivingEntity($$0, $$1, $$2, $$3);
    }
}
