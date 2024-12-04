package dev.doctor4t.arsenal.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShockwaveParticle extends ExplosionLargeParticle {
    public ShockwaveParticle(ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, d, spriteProvider);
        this.maxAge = 8;
        this.scale = 8f;
        this.gravityStrength = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
        this.red = 1;
        this.green = 1;
        this.blue = 1;
        this.alpha = 0.5f;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public float getSize(float tickDelta) {
        float d = (this.age + tickDelta) / (this.maxAge);
        return this.scale * MathHelper.clamp(d, 0, 1);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternionf quaternion = camera.getRotation();
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1, -1, 0), new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0)};
        float size = this.getSize(tickDelta);
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vector3fs[i];
            vector3f.rotate(quaternion);
            vector3f.mul(size);
            vector3f.add(f, g, h);
        }
        int brightness = this.getBrightness(tickDelta);
        this.alpha = (float) MathHelper.lerp((float) this.age / this.getMaxAge(), 0.5, 0);
        this.vertex(vertexConsumer, vector3fs[0], this.getMaxU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, vector3fs[1], this.getMaxU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, vector3fs[2], this.getMinU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, vector3fs[3], this.getMinU(), this.getMaxV(), brightness);
    }

    private void vertex(VertexConsumer vertexConsumer, Vector3f pos, float u, float v, int light) {
        vertexConsumer.vertex(pos.x(), pos.y(), pos.z()).texture(u, v).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }

    @Override
    public int getBrightness(float tint) {
        return 240;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new ShockwaveParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}
