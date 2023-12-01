package tfar.chickenvshunter.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.chickenvshunter.Init;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.entity.BlockBreakerEntity;
import tfar.chickenvshunter.world.ChickVHunterSavedData;

import java.util.ArrayList;
import java.util.List;

public class ChickenPickaxeItem extends PickaxeItem {
    public ChickenPickaxeItem(Tier $$0, int $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand $$2) {

        if (!level.isClientSide) {
            List<Chicken> getNearby = getNearbyChickens(player);
            HitResult pick = player.pick(20, 0, false);
            Vec3 location = pick.getLocation();

            for (Chicken chicken : getNearby) {
                Vec3 traj = location.subtract(chicken.getPosition(0)).normalize();
                ChickenDuck chickenDuck = (ChickenDuck)chicken;
                chickenDuck.setBlockBreaker();
                chicken.setNoGravity(true);
                chicken.setDiscardFriction(true);
                chicken.setDeltaMovement(traj);

                //BlockBreakerEntity blockBreakerEntity = Init.BLOCK_BREAKER.create(level);
                //blockBreakerEntity.setPos(chicken.getPosition(0));
         //       blockBreakerEntity.setOwner(player);

           //     level.addFreshEntity(blockBreakerEntity);
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand($$2), level.isClientSide);
    }

    public static List<Chicken> getNearbyChickens(Player player) {
        List<Chicken> chickens = new ArrayList<>();
        for (Entity entity : ((ServerPlayer) player).serverLevel().getAllEntities()) {
            if (entity instanceof Chicken chicken && !chicken.getUUID().equals(ChickVHunterSavedData.chicken)) {
                if (player.distanceToSqr(entity) < 24 * 24) {
                    chickens.add(chicken);
                }
            }
        }
        return chickens;
    }
}
