package tfar.chickenvshunter.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import tfar.chickenvshunter.ChickenVsHunter;
import tfar.chickenvshunter.Init;
import tfar.chickenvshunter.ducks.ChickenDuck;
import tfar.chickenvshunter.world.deferredevent.DespawnLater;

public class ChickenArrowEntity extends AbstractArrow {
    private int duration = 200;

    public ChickenArrowEntity(EntityType<? extends ChickenArrowEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ChickenArrowEntity(Level $$0, LivingEntity $$1) {
        super(Init.CHICKEN_ARROW, $$1, $$0);
    }

    public ChickenArrowEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(Init.CHICKEN_ARROW, $$1, $$2, $$3, $$0);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide) {
            spawnAngryChickens();
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    protected void spawnAngryChickens() {
        int count = 4;
        for(int i = 0; i < count; ++i) {
            Chicken chicken = EntityType.CHICKEN.create(this.level());
            if (chicken != null) {
                chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                chicken.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("Rage Boost",.75, AttributeModifier.Operation.ADDITION));
                ((ChickenDuck)chicken).reassessGoals();
                ChickenVsHunter.addDeferredEvent((ServerLevel) level(),new DespawnLater(120,chicken,.05));
                this.level().addFreshEntity(chicken);
            }
        }
    }


    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide) {
            spawnAngryChickens();
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity $$0) {
        super.doPostHurtEffects($$0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("Duration")) {
            this.duration = $$0.getInt("Duration");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putInt("Duration", this.duration);
    }
}
