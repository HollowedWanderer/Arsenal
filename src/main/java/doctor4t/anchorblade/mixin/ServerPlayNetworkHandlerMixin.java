package doctor4t.anchorblade.mixin;

import doctor4t.anchorblade.common.util.WeaponSlot;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Shadow public ServerPlayerEntity player;

	@Inject(method = "onUpdateSelectedSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;", ordinal = 1))
	private void arsenal$updateSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
		if (packet instanceof WeaponSlot selectPacket && this.player.getInventory() instanceof WeaponSlot selection) {
			selection.arsenal$setWeaponSlot(selectPacket.arsenal$getWeaponSlot());
		}
	}
}
