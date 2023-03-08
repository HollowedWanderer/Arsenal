package doctor4t.arsenal.mixin;

import doctor4t.arsenal.common.item.AnchorbladeItem;
import doctor4t.arsenal.common.util.WeaponSlot;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements WeaponSlot {
	@Shadow @Final public PlayerEntity player;
	@Unique private boolean selectedAnchor = false;

	@Inject(method = "getMainHandStack", at = @At("HEAD"), cancellable = true)
	private void arsenal$mainHandSlot(CallbackInfoReturnable<ItemStack> cir) {
		if (this.selectedAnchor) {
			ItemStack anchorStack = AnchorbladeItem.getWornAnchor(this.player);
			if (!anchorStack.isEmpty() && anchorStack.getItem() instanceof AnchorbladeItem) {
				cir.setReturnValue(anchorStack);
			} else {
				this.selectedAnchor = false;
			}
		}
	}

	@Inject(method = "updateItems", at = @At("TAIL"))
	private void arsenal$selectSlot(CallbackInfo ci) {
		ItemStack anchorStack = AnchorbladeItem.getWornAnchor(this.player);
		if (anchorStack.isEmpty() || !(anchorStack.getItem() instanceof AnchorbladeItem)) {
			anchorStack.inventoryTick(this.player.world, this.player, 0, this.selectedAnchor);
		}
	}

	@Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
	private void arsenal$slotBreaking(BlockState block, CallbackInfoReturnable<Float> cir) {
		if (this.selectedAnchor) {
			ItemStack anchorStack = AnchorbladeItem.getWornAnchor(this.player);
			if (!anchorStack.isEmpty() && anchorStack.getItem() instanceof AnchorbladeItem) {
				cir.setReturnValue(anchorStack.getMiningSpeedMultiplier(block));
			} else {
				this.selectedAnchor = false;
			}
		}
	}

	@Inject(method = "addPickBlock", at = @At("HEAD"))
	private void arsenal$nonPick(CallbackInfo ci) {
		this.selectedAnchor = false;
	}

	@Inject(method = "swapSlotWithHotbar", at = @At("HEAD"))
	private void arsenal$nonSwap(int slot, CallbackInfo ci) {
		this.selectedAnchor = false;
	}

	@Inject(method = "scrollInHotbar", at = @At("HEAD"))
	private void arsenal$nonScroll(double scrollAmount, CallbackInfo ci) {
		this.selectedAnchor = false;
	}

	@Inject(method = "clone", at = @At("HEAD"))
	private void arsenal$cloned(PlayerInventory playerInventory, CallbackInfo ci) {
		if (playerInventory instanceof WeaponSlot selection) {
			this.selectedAnchor = selection.arsenal$getWeaponSlot();
		}
	}

	@Override
	public void arsenal$setWeaponSlot(boolean weaponSlot) {
		this.selectedAnchor = weaponSlot;
	}

	@Override
	public boolean arsenal$getWeaponSlot() {
		return this.selectedAnchor;
	}
}
