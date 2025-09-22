package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.util.ArmedEntityGetter;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderStateMixin.class)
public class ArmedEntityRenderStateMixin implements ArmedEntityGetter {

    @Unique
    private LivingEntity entity;

    @Override
    public void arsenal$setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public LivingEntity arsenal$getEntity() {
        return entity;
    }

    @Inject(method = "updateRenderState", at = @At("HEAD"))
    private static void updateRenderState(LivingEntity entity, ArmedEntityRenderStateMixin state, ItemModelManager itemModelManager, CallbackInfo ci) {
        if (state instanceof ArmedEntityGetter access) {
            access.arsenal$setEntity(entity);
        }
    }
}
