package dev.doctor4t.arsenal.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.doctor4t.arsenal.index.ArsenalParticles;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;

public record SweepParticleEffect(int baseColor, int shadowColor) implements ParticleEffect {

    public static final MapCodec<SweepParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.RGB.fieldOf("base").forGetter(SweepParticleEffect::baseColor),
            Codecs.RGB.fieldOf("shadow").forGetter(SweepParticleEffect::shadowColor)
    ).apply(instance, SweepParticleEffect::new));

    public static final PacketCodec<RegistryByteBuf, SweepParticleEffect> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER,
            SweepParticleEffect::baseColor,
            PacketCodecs.INTEGER,
            SweepParticleEffect::shadowColor,
            SweepParticleEffect::new
    );

    @Override
    public ParticleType<?> getType() {
        return ArsenalParticles.SWEEP_PARTICLE;
    }
}
