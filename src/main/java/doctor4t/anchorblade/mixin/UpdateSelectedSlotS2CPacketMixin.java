package doctor4t.anchorblade.mixin;

import doctor4t.anchorblade.common.util.WeaponSlot;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(UpdateSelectedSlotC2SPacket.class)
public class UpdateSelectedSlotS2CPacketMixin implements WeaponSlot {
	@Unique private boolean selectedAnchor = false;

	@Override
	public void arsenal$setWeaponSlot(boolean weaponSlot) {
		this.selectedAnchor = weaponSlot;
	}

	@Override
	public boolean arsenal$getWeaponSlot() {
		return this.selectedAnchor;
	}
}
