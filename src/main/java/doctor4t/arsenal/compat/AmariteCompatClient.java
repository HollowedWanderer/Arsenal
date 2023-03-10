package doctor4t.arsenal.compat;

import com.winsweep.amarite.registry.AmariteItems;
import doctor4t.arsenal.common.util.WeaponSlotCallback;
import net.minecraft.util.ActionResult;

public class AmariteCompatClient {
	public static void init() {
		WeaponSlotCallback.EVENT.register((player, holder, stack) -> {
			if (stack.getItem() == AmariteItems.AMARITE_LONGSWORD) {
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		});
	}
}
