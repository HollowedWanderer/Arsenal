package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.entity.AnchorbladeEntity;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.util.AnchorOwner;
import dev.doctor4t.arsenal.util.EnchantmentListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class AnchorbladeEntityRenderer extends EntityRenderer<AnchorbladeEntity> {
    private final ItemRenderer itemRenderer;

    public AnchorbladeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    }

    @Override
    public Identifier getTexture(AnchorbladeEntity entity) {
        return null;
    }

    @Override
    public void render(AnchorBladeEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
        float yawAngle = MathHelper.lerp(tickDelta, state.prevYaw, state.yaw);
        float pitchAngle = MathHelper.lerp(tickDelta, state.prevPitch, state.pitch);

        matrices.push();
        matrices.translate(0, .6, 0);

        float scale = 1.6f;
        matrices.scale(scale, scale, scale);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawAngle + 90));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-pitchAngle + 135));

        ItemStack stack = state.stack;
        AnchorbladeSkin skin = stack.getOrDefault(ArsenalDataComponents.ANCHORBLADE_SKIN, AnchorbladeSkin.DEFAULT);
        RenderLayer chainLayer = RenderLayer.getEntitySmoothCutout(skin.chainTexture);
        this.itemRenderer.renderItem(stack, ItemDisplayContext.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, MinecraftClient.getInstance().world, 0);

        matrices.pop();

        if (state.owner instanceof LivingEntity livingOwner) {
            matrices.push();
            Vec3d pos = state.entity.getLerpedPos(tickDelta);
            Vec3d ringPos = new Vec3d((skin == AnchorbladeSkin.AMBESSA ? 0f : 1f), 0, 0).rotateZ(pitchAngle * MathHelper.RADIANS_PER_DEGREE).rotateY((yawAngle + 90) * MathHelper.RADIANS_PER_DEGREE).add(0, state.entity.getHeight() / 2f, 0);
            Vec3d leashPos = livingOwner.getLeashPos(tickDelta);

            if (livingOwner instanceof AnchorOwner anchorOwner && livingOwner instanceof PlayerEntity player) {
                boolean reeling = EnchantmentListener.hasEnchantment(stack, "arsenal:reeling");
                Hand hand = Hand.OFF_HAND;
                if (anchorOwner.arsenal$isAnchorActive(hand, reeling) && anchorOwner.arsenal$getAnchor(hand, reeling).equals(state.entity)) {
                    player.setMainArm(player.getMainArm().getOpposite());
                    leashPos = player.getLeashPos(tickDelta);
                    player.setMainArm(player.getMainArm().getOpposite());
                }
            }

            Vec3d ownerPos = leashPos.subtract(pos);

            float length = (float) ringPos.distanceTo(ownerPos);
            MatrixStack.Entry matrixEntry = matrices.peek();
            Matrix4f modelMatrix = matrixEntry.getPositionMatrix();
            float minU = 0;
            float maxU = 1;
            float minV = 0;
            float maxV = length / 8f;
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(chainLayer);

            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
            Vec3d camPos = camera.getPos();
            Vec3d chainVec = ownerPos.subtract(ringPos).normalize();
            Vec3d rightVec = chainVec.crossProduct(camPos.subtract(pos)).normalize().multiply(0.25);

            Vec3d vert1 = ringPos.add(rightVec);
            Vec3d vert2 = ownerPos.add(rightVec);
            Vec3d vert3 = ownerPos.subtract(rightVec);
            Vec3d vert4 = ringPos.subtract(rightVec);
            int chainLight = LightmapTextureManager.pack(this.getBlockLight(state.entity, livingOwner.getBlockPos()), this.getSkyLight(state.entity, livingOwner.getBlockPos()));
            this.vertex(vert1, vertexConsumer, minU, minV, modelMatrix, light);
            this.vertex(vert2, vertexConsumer, minU, maxV, modelMatrix, chainLight);
            this.vertex(vert3, vertexConsumer, maxU, maxV, modelMatrix, chainLight);
            this.vertex(vert4, vertexConsumer, maxU, minV, modelMatrix, light);
            matrices.pop();
        }
    }

    @Override
    protected boolean canBeCulled(AnchorbladeEntity entity) {
        return false;
    }

    private void vertex(Vec3d vec, VertexConsumer vertexConsumer, float u, float v, Matrix4f modelMatrix, int light) {
        vertexConsumer.vertex(modelMatrix, (float) vec.x, (float) vec.y, (float) vec.z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(0, 1, 0);
    }

    @Override
    public AnchorBladeEntityRenderState createRenderState() {
        return new AnchorBladeEntityRenderState();
    }

    @Override
    public void updateRenderState(AnchorbladeEntity entity, AnchorBladeEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        state.owner = entity.getOwner();
        state.entity = entity;
        state.prevPitch = entity.lastPitch;
        state.prevYaw = entity.lastYaw;
        state.stack = entity.getStack();
        state.yaw = entity.getYaw();
        state.pitch = entity.getPitch();
    }
}
