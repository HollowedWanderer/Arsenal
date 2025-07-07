package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.particle.SweepParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ArsenalParticles {
    Map<ParticleType<?>, Identifier> PARTICLES = new LinkedHashMap<>();

    ParticleType<SweepParticleEffect> SWEEP_PARTICLE = create("sweep", FabricParticleTypes.complex(true, SweepParticleEffect.CODEC, SweepParticleEffect.PACKET_CODEC));
    SimpleParticleType BLOOD_BUBBLE = create("blood_bubble", FabricParticleTypes.simple(true));
    SimpleParticleType BLOOD_BUBBLE_SPLATTER = create("blood_bubble_splatter", FabricParticleTypes.simple(true));
    SimpleParticleType SHOCKWAVE = create("shockwave", FabricParticleTypes.simple(true));

    static void initialize() {
        PARTICLES.keySet().forEach(particle -> Registry.register(Registries.PARTICLE_TYPE, PARTICLES.get(particle), particle));
    }

    private static <T extends ParticleType<?>> T create(String name, T particle) {
        PARTICLES.put(particle, Arsenal.id(name));
        return particle;
    }
}
