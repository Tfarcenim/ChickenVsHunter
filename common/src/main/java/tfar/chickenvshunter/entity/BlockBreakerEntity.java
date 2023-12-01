package tfar.chickenvshunter.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakerEntity extends Projectile {

    public BlockBreakerEntity(EntityType<? extends BlockBreakerEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public int progress;

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }


        if (!level().isClientSide) {
            level().getProfiler().push("voidHole");
            BlockPos pos = blockPosition();
            BlockState state = level().getBlockState(pos);
            if (!state.isAir()) {
                progress++;
                LivingEntity owner = getOwner();
                List<ItemStack> loot = Block.getDrops(state, (ServerLevel) level(), pos, level().getBlockEntity(pos), owner, owner != null ? owner.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY);
                giveItemsToOwner(loot);
                level().removeBlock(pos, false);
                if (progress > 5) {
                    discard();
                }
            }
            level().getProfiler().pop();
        }
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        setPos(d0, d1, d2);
    }

    public void giveItemsToOwner(List<ItemStack> stacks) {

        LivingEntity owner = getOwner();

        if (owner instanceof ServerPlayer serverplayer) {
            for (ItemStack stack : stacks) {
                serverplayer.addItem(stack);
            }
        }
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag var1) {
        progress = var1.getInt("progress");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag var1) {
        var1.putInt("progress", progress);
    }


    @Nullable
    @Override
    public LivingEntity getOwner() {
        return (LivingEntity) super.getOwner();
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }
}
