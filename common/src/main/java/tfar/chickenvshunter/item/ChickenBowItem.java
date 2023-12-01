package tfar.chickenvshunter.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import tfar.chickenvshunter.entity.ChickenArrowEntity;

public class ChickenBowItem extends BowItem {
    public ChickenBowItem(Properties $$0) {
        super($$0);
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player) {
            Player $$4 = (Player)$$2;
            boolean $$5 = $$4.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack $$6 = $$4.getProjectile(stack);
            if (!$$6.isEmpty() || $$5) {
                if ($$6.isEmpty()) {
                    $$6 = new ItemStack(Items.ARROW);
                }

                int $$7 = this.getUseDuration(stack) - $$3;
                float power = getPowerForTime($$7);
                if (!((double)power < 0.1)) {
                    boolean $$9 = $$5 && $$6.is(Items.ARROW);
                    if (!level.isClientSide) {
                        ArrowItem $$10 = (ArrowItem)($$6.getItem() instanceof ArrowItem ? $$6.getItem() : Items.ARROW);
                        AbstractArrow $$11 = createChickenArrow(level, $$4);//the change
                        $$11.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, power * 3.0F, 1.0F);
                        if (power == 1.0F) {
                            $$11.setCritArrow(true);
                        }

                        int $$12 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if ($$12 > 0) {
                            $$11.setBaseDamage($$11.getBaseDamage() + (double)$$12 * 0.5 + 0.5);
                        }

                        int $$13 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if ($$13 > 0) {
                            $$11.setKnockback($$13);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            $$11.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$4.getUsedItemHand()));
                        if ($$9 || $$4.getAbilities().instabuild && ($$6.is(Items.SPECTRAL_ARROW) || $$6.is(Items.TIPPED_ARROW))) {
                            $$11.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity($$11);
                    }

                    level.playSound(
                            null,
                            $$4.getX(),
                            $$4.getY(),
                            $$4.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F
                    );
                    if (!$$9 && !$$4.getAbilities().instabuild) {
                        $$6.shrink(1);
                        if ($$6.isEmpty()) {
                            $$4.getInventory().removeItem($$6);
                        }
                    }

                    $$4.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public AbstractArrow createChickenArrow(Level level,LivingEntity living) {
        return new ChickenArrowEntity(level,living);
    }


}
