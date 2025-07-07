package dev.doctor4t.arsenal.client.particle;

import dev.doctor4t.arsenal.particle.SweepParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SweepAttackParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteWithAge;
    private final Vector3f baseColor;
    private final Vector3f shadowColor;

    private SweepAttackParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteWithAge, SweepParticleEffect particleEffect) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.spriteWithAge = spriteWithAge;
        this.maxAge = 4;
        this.scale = 1.0F;
        this.setSpriteForAge(spriteWithAge);
        this.baseColor = ColorHelper.toVector(particleEffect.baseColor());
        this.shadowColor = ColorHelper.toVector(particleEffect.shadowColor());
    }

    @Override
    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteWithAge);
        }
    }

    @Override
    protected void render(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float tickProgress) {
        this.setSprite(this.spriteWithAge.getSprite(this.age, this.maxAge * 2));
        this.setColor(this.baseColor.x, this.baseColor.y, this.baseColor.z);
        super.render(vertexConsumer, camera, quaternionf, tickProgress);
        this.setSprite(this.spriteWithAge.getSprite(this.age + this.maxAge, this.maxAge * 2));
        this.setColor(this.shadowColor.x, this.shadowColor.y, this.shadowColor.z);
        super.render(vertexConsumer, camera, quaternionf, tickProgress);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SweepParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SweepParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SweepAttackParticle(world, x, y, z, spriteProvider, parameters);
        }
    }
}
