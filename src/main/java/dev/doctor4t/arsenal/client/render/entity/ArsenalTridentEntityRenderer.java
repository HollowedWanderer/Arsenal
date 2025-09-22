package dev.doctor4t.arsenal.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ArsenalTridentEntityRenderer extends EntityRenderer<TridentEntity> {
    private final ItemRenderer itemRenderer;
    private final ItemStack tridentStack = new ItemStack(Items.TRIDENT); // TODO: Replace with a synced stack so skins work

    public ArsenalTridentEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    }

    @Override
    public Identifier getTexture(TridentEntity entity) {
        return null;
    }

    @Override
    public TridentEntityRenderer createRenderState() {
        return new TridentEntityRenderer();
    }

    @Override
    public void render(TridentEntityRenderer state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        float scale = 1.6f;
        matrices.scale(scale, scale, scale);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw + 90));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-state.pitch + 45));
        matrices.translate(.3, -.3, 0);

        this.itemRenderer.renderItem(tridentStack, ItemDisplayContext.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, MinecraftClient.getInstance().world, 1);

        matrices.pop();
    }
}
