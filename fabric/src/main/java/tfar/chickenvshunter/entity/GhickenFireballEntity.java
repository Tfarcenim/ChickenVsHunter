package tfar.chickenvshunter.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

public class GhickenFireballEntity extends AbstractHurtingProjectile {
    public GhickenFireballEntity(EntityType<? extends GhickenFireballEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GhickenFireballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, double d, double e, double f, double g, double h, double i, Level level) {
        super(entityType, d, e, f, g, h, i, level);
    }

    public GhickenFireballEntity(EntityType<? extends AbstractHurtingProjectile> entityType, LivingEntity livingEntity, double d, double e, double f, Level level) {
        super(entityType, livingEntity, d, e, f, level);
    }
}
