package dev.doctor4t.arsenal.client.render.feature;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.index.ArsenalTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.joml.Quaternionf;

public class BackWeaponFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    public BackWeaponFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().player;
        if (BackWeaponComponent.isHoldingBackWeapon(abstractClientPlayerEntity)) return;
        ItemStack stack = BackWeaponComponent.getBackWeapon(abstractClientPlayerEntity);
        if (stack.isEmpty() || abstractClientPlayerEntity == null) return;

        matrices.push();

        matrices.multiply((new Quaternionf())
                .rotateY(-3.1415927F)
                .rotateX(0.35F * -(6.0F + state.field_53537 / 2.0F + state.field_53536) * 0.017453292F)
                .rotateZ(-(state.field_53538 / 2.0F * 0.017453292F))
                .rotateY((180.0F - state.field_53538 / 2.0F) * 0.017453292F)
        );

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

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ItemDisplayContext.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, MinecraftClient.getInstance().world, 0);
        matrices.pop();
    }
}
