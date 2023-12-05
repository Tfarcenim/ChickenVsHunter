package tfar.chickenvshunter.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tfar.chickenvshunter.entity.EnderEggEntity;

public class EnderEggItem extends Item {
    public EnderEggItem(Properties $$0) {
        super($$0);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand $$2) {
        ItemStack $$3 = player.getItemInHand($$2);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENDER_PEARL_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        player.getCooldowns().addCooldown(this, 20);
        if (!level.isClientSide) {

            EnderEggEntity old = findLocalEntity((ServerPlayer) player);

            if (old == null) {
                EnderEggEntity enderEggEntity = new EnderEggEntity(level, player);
                enderEggEntity.setItem($$3);
                enderEggEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(enderEggEntity);
            } else {
                old.teleportPlayer((ServerPlayer) player);
                old.discard();
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
      /*  if (!player.getAbilities().instabuild) {
            $$3.shrink(1);
        }*/

        return InteractionResultHolder.sidedSuccess($$3, level.isClientSide());
    }

    static EnderEggEntity findLocalEntity(ServerPlayer serverPlayer) {
        ServerLevel serverLevel = serverPlayer.serverLevel();
        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity instanceof EnderEggEntity enderEggEntity && serverPlayer.equals(enderEggEntity.getOwner())) {
                return enderEggEntity;
            }
        }
        return null;
    }

}
