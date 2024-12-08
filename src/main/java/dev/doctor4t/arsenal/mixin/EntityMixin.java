package dev.doctor4t.arsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.arsenal.index.ArsenalStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyReturnValue(method = "isTeammate", at = @At("RETURN"))
    public boolean arsenal$preventStunnedMobsFromTargeting(boolean original) {
        if ((Object) this instanceof LivingEntity livingEntity) {
            return livingEntity.hasStatusEffect(ArsenalStatusEffects.STUN);
        }
        return original;
    }
}
