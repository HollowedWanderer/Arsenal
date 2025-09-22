package dev.doctor4t.arsenal.client.render.feature;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.util.BackStateResources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.joml.Quaternionf;

import java.util.List;

public class BackWeaponFeatureRenderer extends FeatureRenderer<PlayerEntityRenderer, PlayerEntityModel> {

    public BackWeaponFeatureRenderer(FeatureRendererContext<PlayerEntityRenderer, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer state, float limbAngle, float limbDistance) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (BackWeaponComponent.isHoldingBackWeapon(player)) return;
        ItemStack stack = BackWeaponComponent.getBackWeapon(player);
        if (stack.isEmpty() || player == null) return;

        BackStateResources.BackStateData data = BackStateResources.getTransform(Registries.ITEM.getId(stack.getItem()));
        List<Float> translation = data.translation();
        List<Float> rotation = data.rotation();
        List<Float> scales = data.scale();

        matrices.push();
        matrices.multiply((new Quaternionf())
                .rotateY(-3.1415927F)
                .rotateX(data.sway() * -(6.0F + state.field_53537 / 2.0F + state.field_53536) * 0.017453292F)
                .rotateZ(-(state.field_53538 / 2.0F * 0.017453292F))
                .rotateY((180.0F - state.field_53538 / 2.0F) * 0.017453292F)
        );

        this.getContextModel().body.applyTransform(matrices);
        matrices.translate(0.0, 0.25, 0.15);
        matrices.translate(translation.get(0), translation.get(1), translation.get(2));
        if (state.capeVisible && state.skinTextures.capeTexture() != null) {
            matrices.translate(0, 0, 0.05);
        }

        if (player.getEquippedStack(EquipmentSlot.CHEST) != ItemStack.EMPTY) {
            matrices.translate(0.0, 0.0, 0.05);
        }

        matrices.multiply(new Quaternionf()
                .rotateX((float) Math.toRadians(rotation.get(0)))
                .rotateY((float) Math.toRadians(rotation.get(1)))
                .rotateZ((float) Math.toRadians(rotation.get(2)))
        );

        float scale = .85f;
        matrices.scale(scale, scale, scale);
        matrices.scale(scales.get(0), scales.get(1), scales.get(2));

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, data.mode(), light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, MinecraftClient.getInstance().world, 0);
        matrices.pop();
    }
}
