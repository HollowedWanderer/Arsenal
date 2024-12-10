package dev.doctor4t.arsenal.client.render.feature;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.index.ArsenalTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BackWeaponFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public BackWeaponFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (BackWeaponComponent.isHoldingBackWeapon(abstractClientPlayerEntity)) return;
        ItemStack stack = BackWeaponComponent.getBackWeapon(abstractClientPlayerEntity);
        if (stack.isEmpty()) return;

        matrices.push();

        boolean hasCape = abstractClientPlayerEntity.canRenderCapeTexture() && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE) && abstractClientPlayerEntity.getCapeTexture() != null && !abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
        boolean hasChestPlate = !abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
        matrices.translate(0.0F, 0.0F, 0.05F + (hasCape ? 0.05f : 0f) + (hasChestPlate ? .05f : 0f));
        double d = MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevCapeX, abstractClientPlayerEntity.capeX)
                - MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevX, abstractClientPlayerEntity.getX());
        double e = MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevCapeY, abstractClientPlayerEntity.capeY)
                - MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevY, abstractClientPlayerEntity.getY());
        double m = MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevCapeZ, abstractClientPlayerEntity.capeZ)
                - MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevZ, abstractClientPlayerEntity.getZ());
        float n = MathHelper.lerpAngleDegrees(tickDelta, abstractClientPlayerEntity.prevBodyYaw, abstractClientPlayerEntity.bodyYaw);
        double o = MathHelper.sin(n * (float) (Math.PI / 180.0));
        double p = (-MathHelper.cos(n * (float) (Math.PI / 180.0)));
        float q = (float) e * 10.0F;
        q = MathHelper.clamp(q, -6.0F, 0f); // max from 32 (cape code) to 0
        float r = (float) (d * o + m * p) * 100.0F;
        r = MathHelper.clamp(r, 0.0F, 40.0F); // max from 150 (cape code) to 40
        float s = (float) (d * p - m * o) * 100.0F;
        s = MathHelper.clamp(s, -20.0F, 20.0F);
        if (r < 0.0F) {
            r = 0.0F;
        }

        float t = MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevStrideDistance, abstractClientPlayerEntity.strideDistance);
        q += MathHelper.sin(MathHelper.lerp(tickDelta, abstractClientPlayerEntity.prevHorizontalSpeed, abstractClientPlayerEntity.horizontalSpeed) * 6.0F) * 32.0F * t;
        if (abstractClientPlayerEntity.isInSneakingPose()) {
            q += 25.0F;
        }

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6f + r / 2.0F + q));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - s / 2.0F));

        float scale = .85f;
        if (stack.isIn(ArsenalTags.BIG_WEAPONS)) {
            scale = 1.6f;
            matrices.translate(0.0, 0.2, -0.15);
        } else {
            matrices.translate(0, 0.3, -0.1);
        }
        if (stack.isIn(ArsenalTags.SHIELDS)) {
            scale = 1.8f;
            matrices.translate(0.0, 0.2, 0.0);
        }

        matrices.scale(scale, scale, scale);

        MinecraftClient.getInstance().getItemRenderer().renderItem(abstractClientPlayerEntity, stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, abstractClientPlayerEntity.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }
}
