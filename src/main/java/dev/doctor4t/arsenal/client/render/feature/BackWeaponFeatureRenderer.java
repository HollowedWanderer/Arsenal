package dev.doctor4t.arsenal.client.render.feature;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.util.WeaponSlotCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class BackWeaponFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public BackWeaponFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (BackWeaponComponent.isHoldingBackWeapon(entity)) return;
        ItemStack stack = BackWeaponComponent.getBackWeapon(entity);
        if (stack.isEmpty()) return;
        ActionResult result = WeaponSlotCallback.EVENT.invoker().interact(entity, stack);
        if (result == ActionResult.FAIL) return;
        matrices.push();
        matrices.translate(0, 0.35, 0.25);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity, stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }
}
