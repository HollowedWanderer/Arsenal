package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.index.ArsenalStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryAttack", at = @At("HEAD"), cancellable = true)
    public void arsenal$preventStunnedMobsFromAttacking(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasStatusEffect(ArsenalStatusEffects.STUN)) {
            cir.setReturnValue(false);
        }
    }
}
