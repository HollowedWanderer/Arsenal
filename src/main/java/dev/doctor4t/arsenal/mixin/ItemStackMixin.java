package dev.doctor4t.arsenal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.arsenal.item.CustomNameColorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isEmpty();

    @ModifyExpressionValue(
            method = "getTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 0)
    )
    private MutableText arsenal$tooltipChangeItemNameColor(MutableText mutableText) {
        if (this.getItem() instanceof CustomNameColorItem colorItem) {
            int nameColor = colorItem.getNameColor();
            if (nameColor == 0xFFFFFF) {
                return mutableText;
            } else {
                return mutableText.setStyle(mutableText.getStyle().withColor(nameColor));
            }
        }
        return mutableText;
    }

    @ModifyExpressionValue(
            method = "toHoverableText",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 1)
    )
    private MutableText arsenal$hoverTextChangeItemNameColor(MutableText mutableText) {
        if (this.getItem() instanceof CustomNameColorItem colorItem) {
            return mutableText.setStyle(mutableText.getStyle().withColor(colorItem.getNameColor()));
        }
        return mutableText;
    }
}
