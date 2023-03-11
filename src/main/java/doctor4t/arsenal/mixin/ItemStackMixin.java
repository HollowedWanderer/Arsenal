package doctor4t.arsenal.mixin;

import doctor4t.arsenal.common.item.CustomColorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialeemisc.util.MialeeText;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(method = "getName", at = @At("RETURN"), cancellable = true)
	public void getName(CallbackInfoReturnable<Text> cir) {
		if (this instanceof CustomColorItem colorItem) {
			cir.setReturnValue(MialeeText.withColor(cir.getReturnValue(), colorItem.getStackColor()));
		}
	}
}
