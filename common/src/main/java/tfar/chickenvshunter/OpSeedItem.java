package tfar.chickenvshunter;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OpSeedItem extends Item {

    public static final Consumer<Player> IRON = player -> {
        player.drop(new ItemStack(Init.CHICKEN_PICKAXE),true);
    };
    public static final Consumer<Player> GOLD = player -> {
        player.drop(new ItemStack(Init.CHICKEN_AXE),true);
    };
    public static final Consumer<Player> DIAMOND = player -> {};
    public static final Consumer<Player> NETHERITE = player -> {};

    public final Consumer<Player> rightClick;
    public OpSeedItem(Properties $$0,Consumer<Player> onRightClick) {
        super($$0);
        rightClick = onRightClick;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (interactionTarget instanceof Chicken chicken && interactionTarget.isAlive()) {
            chicken.level().playSound(player, chicken, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!player.level().isClientSide) {
                rightClick.accept(player);
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
