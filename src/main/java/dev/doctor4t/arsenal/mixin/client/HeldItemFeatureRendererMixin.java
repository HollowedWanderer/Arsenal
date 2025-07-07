package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.util.AnchorOwner;
import dev.doctor4t.arsenal.util.ArmedEntityGetter;
import dev.doctor4t.arsenal.util.EnchantmentListener;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin {
    @Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
    private void arsenal$thrown(ArmedEntityRenderState entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entityState instanceof ArmedEntityGetter access) {
            ItemStack stack = access.arsenal$getEntity().getStackInArm(arm);
            if (stack.isOf(ArsenalItems.ANCHORBLADE)) {
                boolean reeling = EnchantmentListener.hasEnchantment(stack, "arsenal:reeling");
                if (access.arsenal$getEntity() instanceof AnchorOwner owner && owner.arsenal$isAnchorActive(access.arsenal$getEntity().getMainHandStack().equals(stack) ? Hand.MAIN_HAND : Hand.OFF_HAND, reeling)) {
                    ci.cancel();
                }
            }
        }
    }
}
