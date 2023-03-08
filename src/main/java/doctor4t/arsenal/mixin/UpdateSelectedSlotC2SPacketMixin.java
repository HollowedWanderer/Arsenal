package doctor4t.arsenal.mixin;

import doctor4t.arsenal.common.util.WeaponSlot;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(UpdateSelectedSlotS2CPacket.class)
public class UpdateSelectedSlotC2SPacketMixin implements WeaponSlot {
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
