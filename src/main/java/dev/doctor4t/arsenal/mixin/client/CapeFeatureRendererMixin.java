package dev.doctor4t.arsenal.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @ModifyConstant(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", constant = @Constant(floatValue = 32.0f, ordinal = 0))
    public float arsenal$clampCapeRotationR(float constant, @Local(argsOnly = true) AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return (!BackWeaponComponent.getBackWeapon(abstractClientPlayerEntity).isEmpty() && !BackWeaponComponent.isHoldingBackWeapon(abstractClientPlayerEntity)) ? 0f : constant;
    }

    @ModifyConstant(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V", constant = @Constant(floatValue = 150.0F, ordinal = 0))
    public float arsenal$clampCapeRotationQ(float constant, @Local(argsOnly = true) AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return (!BackWeaponComponent.getBackWeapon(abstractClientPlayerEntity).isEmpty() && !BackWeaponComponent.isHoldingBackWeapon(abstractClientPlayerEntity)) ? 40f : constant;
    }
}
