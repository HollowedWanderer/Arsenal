package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.index.ArsenalEnchantments;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.util.AnchorOwner;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
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
    private void arsenal$thrown(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.isOf(ArsenalItems.ANCHORBLADE)) {
            boolean reeling = EnchantmentHelper.getLevel(ArsenalEnchantments.REELING, stack) > 0;
            if (entity instanceof AnchorOwner owner && owner.arsenal$isAnchorActive(entity.getMainHandStack().equals(stack) ? Hand.MAIN_HAND : Hand.OFF_HAND, reeling)) {
                ci.cancel();
            }
        }
    }
}
