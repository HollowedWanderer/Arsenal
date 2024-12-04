package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(method = "getMainHandStack", at = @At("HEAD"), cancellable = true)
    private void arsenal$mainHandSlot(CallbackInfoReturnable<ItemStack> cir) {
        if (BackWeaponComponent.isHoldingBackWeapon(this.player)) {
            if (!BackWeaponComponent.getBackWeapon(this.player).isEmpty()) {
                cir.setReturnValue(BackWeaponComponent.getBackWeapon(this.player));
            } else {
                BackWeaponComponent.setHoldingBackWeapon(this.player, false);
            }
        }
    }

    @Inject(method = "updateItems", at = @At("TAIL"))
    private void arsenal$selectSlot(CallbackInfo ci) {
        if (!BackWeaponComponent.getBackWeapon(this.player).isEmpty()) {
            BackWeaponComponent.getBackWeapon(this.player).inventoryTick(this.player.getWorld(), this.player, 0, BackWeaponComponent.isHoldingBackWeapon(this.player));
        }
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
    private void arsenal$slotBreaking(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (BackWeaponComponent.isHoldingBackWeapon(this.player)) {
            if (!BackWeaponComponent.getBackWeapon(this.player).isEmpty()) {
                cir.setReturnValue(BackWeaponComponent.getBackWeapon(this.player).getMiningSpeedMultiplier(block));
            } else {
                BackWeaponComponent.setHoldingBackWeapon(this.player, false);
            }
        }
    }

    @Inject(method = "addPickBlock", at = @At("HEAD"))
    private void arsenal$nonPick(CallbackInfo ci) {
        BackWeaponComponent.setHoldingBackWeapon(this.player, false);
    }

    @Inject(method = "swapSlotWithHotbar", at = @At("HEAD"))
    private void arsenal$nonSwap(int slot, CallbackInfo ci) {
        BackWeaponComponent.setHoldingBackWeapon(this.player, false);
    }

    @Inject(method = "scrollInHotbar", at = @At("HEAD"))
    private void arsenal$nonScroll(double scrollAmount, CallbackInfo ci) {
        BackWeaponComponent.setHoldingBackWeapon(this.player, false);
    }
}
