package tfar.chickenvshunter.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import tfar.chickenvshunter.Init;

public class GhickenFireballEntity extends Fireball {
    public GhickenFireballEntity(EntityType<? extends GhickenFireballEntity> entityType, Level level) {
        super(entityType, level);
    }

    private int explosionPower = 1;

    public GhickenFireballEntity(Level level, LivingEntity livingEntity, double d, double e, double f, int i) {
        super(Init.GHICKEN_FIREBALL, livingEntity, d, e, f, level);
        this.explosionPower = i;
        setItem(Items.EGG.getDefaultInstance());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            boolean bl = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, bl, Level.ExplosionInteraction.MOB);
            this.discard();
            if (this.random.nextInt(20) == 0) {
                Mob ghickenEntity = Init.GHICKEN.create(level());
                ghickenEntity.setPos(getX(),getY(),getZ());
                level().addFreshEntity(ghickenEntity);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level().isClientSide) {
            return;
        }
        Entity entity = result.getEntity();
        Entity entity2 = this.getOwner();
        entity.hurt(this.damageSources().fireball(this, entity2), 6.0f);
        if (entity2 instanceof LivingEntity) {
            this.doEnchantDamageEffects((LivingEntity)entity2, entity);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ExplosionPower", CompoundTag.TAG_ANY_NUMERIC)) {
            this.explosionPower = compound.getByte("ExplosionPower");
        }
    }
}
