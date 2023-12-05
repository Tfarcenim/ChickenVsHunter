package tfar.chickenvshunter;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tfar.chickenvshunter.platform.Services;
import tfar.chickenvshunter.world.deferredevent.ScaleLater;

import java.util.List;
import java.util.function.Consumer;

public class OpSeedItem extends Item {

    public static final Consumer<Player> IRON = player -> player.drop(new ItemStack(Init.CHICKEN_PICKAXE),false);
    public static final Consumer<Player> GOLD = player -> player.drop(new ItemStack(Init.CHICKEN_AXE),false);
    public static final Consumer<Player> DIAMOND = player -> player.drop(new ItemStack(Init.CHICKEN_BOW),false);
    public static final Consumer<Player> NETHERITE = player -> {
        player.drop(new ItemStack(Init.CHICKEN_HELMET),false);
        player.drop(new ItemStack(Init.CHICKEN_CHESTPLATE),false);
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
