package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.util.BackSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractCraftingScreenHandler {

    public PlayerScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, int width, int height) {
        super(type, syncId, width, height);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void arsenal$init(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo ci) {
        this.addSlot(new BackSlot(BackWeaponComponent.getBackWeaponInventory(inventory.player), 0, 77, 44));
    }
}
