package dev.doctor4t.arsenal.mixin.impaled;

import dev.doctor4t.arsenal.cca.TridentEntityComponent;
import dev.doctor4t.arsenal.mixin.ProjectileEntityMixin;
import dev.doctor4t.arsenal.util.WeaponSlotHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntityMixin {

    @Inject(method = "age", at = @At("HEAD"), cancellable = true)
    private void stopDespawing(CallbackInfo ci) {
        PersistentProjectileEntity persistentProjectileEntity = (PersistentProjectileEntity) (Object) this;
        if (persistentProjectileEntity instanceof TridentEntity tridentEntity) {
            TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get(tridentEntity);
            if (tridentEntityComponent.getisDropped()){
                ci.cancel();
            }
        }
    }
}
