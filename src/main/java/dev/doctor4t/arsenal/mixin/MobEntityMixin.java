package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.index.ArsenalStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void setTarget(@Nullable LivingEntity target);

    @Inject(method = "tick", at = @At("TAIL"))
    public void arsenal$removeTargetForStunnedMobs(CallbackInfo ci) {
        if (this.hasStatusEffect(ArsenalStatusEffects.STUN)) {
            this.setTarget(null);
            this.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
        }
    }
}
