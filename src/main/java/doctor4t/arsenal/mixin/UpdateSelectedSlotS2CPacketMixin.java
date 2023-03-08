package doctor4t.arsenal.mixin;

import doctor4t.arsenal.common.util.WeaponSlot;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(UpdateSelectedSlotC2SPacket.class)
public class UpdateSelectedSlotS2CPacketMixin implements WeaponSlot {
	@Unique private boolean selectedWeapon = false;

	@Override
	public void arsenal$setWeaponSlot(boolean weaponSlot) {
		this.selectedWeapon = weaponSlot;
	}

	@Override
	public boolean arsenal$getWeaponSlot() {
		return this.selectedWeapon;
	}
}
