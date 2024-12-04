package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.entity.BloodScytheEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BloodScytheEntityRenderer<T extends BloodScytheEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE = new Identifier(Arsenal.MOD_ID, "textures/entity/blood_scythe.png");

    public BloodScytheEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(T bloodScythe, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, bloodScythe.prevYaw, bloodScythe.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, bloodScythe.prevPitch, bloodScythe.getPitch())));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
        matrixStack.scale(0.4f, 0.4f, 0.4f);
        matrixStack.translate(-4.0, 0.0, 0.0);
        for (int u = 0; u < 4; ++u) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
        }
        matrixStack.pop();
        super.render(bloodScythe, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }

    public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, normalX, normalY, normalZ).next();
    }
}

