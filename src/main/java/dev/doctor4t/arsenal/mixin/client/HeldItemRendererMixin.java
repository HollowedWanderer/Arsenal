package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.index.ArsenalEnchantments;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.util.AnchorOwner;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    public void arsenal$renderAnchor(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(ArsenalItems.ANCHORBLADE)) {
            boolean reeling = EnchantmentHelper.getLevel(ArsenalEnchantments.REELING, stack) > 0;
            if (player instanceof AnchorOwner owner && owner.arsenal$isAnchorActive(hand, reeling)) {
                ci.cancel();
            }
        }
    }
}
