package dev.doctor4t.arsenal.mixin.impaled;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.arsenal.cca.TridentEntityComponent;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

    @Mutable
    @Shadow
    @Final
    private static TrackedData<Byte> LOYALTY;

    @Shadow
    public int returnTimer;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void customReturnLogic(CallbackInfo ci) {

        if (this.getWorld().isClient()) return;


        ItemStack stack = this.getItemStack();
        TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get((TridentEntity) (Object) this);
        if (tridentEntityComponent.getisDropped()) {
            this.inGroundTime = 0;
            stack.set(ArsenalDataComponents.SOULD_RECALL, false);
            this.getDataTracker().set(LOYALTY, (byte) 0);

        }
        if (stack.get(ArsenalDataComponents.SLOT_THROWN_FROM)!=null) {
            tridentEntityComponent.setslotDroppedFrom(stack.get(ArsenalDataComponents.SLOT_THROWN_FROM));
        }
        if (stack.get(ArsenalDataComponents.LOYAL_PLAYER) != null && stack.getOrDefault(ArsenalDataComponents.SOULD_RECALL, false)) {
            this.setNoClip(true);
            Vec3d vec3d = this.getOwner().getEyePos().subtract(this.getPos());
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double) 3, this.getZ());
            double d = 0.05 * (double) 3;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
            }

            ++this.returnTimer;
        }
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;isOwnerAlive()Z")
    )
    private boolean redirectIsOwnerAlive(boolean original) {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive() && !this.getWorld().isClient) {
            return (!(entity instanceof ServerPlayerEntity) || !entity.isSpectator()) || this.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER) != null;
        }
        return original;
    }
    @Inject(method = "tryPickup", at = @At(value = "HEAD"), cancellable = true)
    public void pickupSavedSlotFrom(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

        if (this.getWorld().isClient()) return;

        ItemStack stack = this.getItemStack();
        TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get((TridentEntity) (Object) this);
        if (this.getOwner()==player){
            if (tridentEntityComponent.getslotDroppedFrom()!=-1){
                if (player.getInventory().getStack(tridentEntityComponent.getslotDroppedFrom()).isEmpty()){
                    player.getInventory().setStack(tridentEntityComponent.getslotDroppedFrom(),stack);
                    cir.setReturnValue(true);
                } else if (player.isCreative()){
                        cir.setReturnValue(player.isCreative());
                    } else {
                        cir.setReturnValue(player.getInventory().insertStack(stack));
                    }

            }
        }
    }

    // PYROFAB'S CODE

    @ModifyVariable(
            method = "tick",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/entity/projectile/TridentEntity;LOYALTY:Lnet/minecraft/entity/data/TrackedData;"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;isOwnerAlive()Z")
            ),
            at = @At("STORE")
    )
    private int sit(int loyaltyLevel) {
        // If your owner told you to sit, you sit (fake no loyalty)
        if (this.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER) != null) {
            TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get((TridentEntity) (Object) this);
            if (tridentEntityComponent.getisDropped()) {
                return 0;
            }
        }
        return loyaltyLevel;
    }
}
