package tfar.chickenvshunter;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OpSeedItem extends Item {

    //- swap a couple of the seed drops around
    //	- netherite seed gives chicken bow, 4 golden ghicken eggs, chicken pants, chicken boots
    //	- diamond seed gives chicken helmet, and chicken chest piece

    public static final Consumer<Player> IRON = player -> player.drop(new ItemStack(Init.CHICKEN_PICKAXE),false);
    public static final Consumer<Player> GOLD = player -> player.drop(new ItemStack(Init.CHICKEN_AXE),false);
    public static final Consumer<Player> DIAMOND = player -> {
        player.drop(new ItemStack(Init.CHICKEN_HELMET),false);
        player.drop(new ItemStack(Init.CHICKEN_CHESTPLATE),false);
    };
    public static final Consumer<Player> NETHERITE = player -> {
        player.drop(new ItemStack(Init.CHICKEN_BOW), false);
        player.drop(new ItemStack(Init.GHICKEN_SPAWN_EGG,4),false);

        player.drop(new ItemStack(Init.CHICKEN_LEGGINGS),false);
        player.drop(new ItemStack(Init.CHICKEN_BOOTS),false);

    };

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
