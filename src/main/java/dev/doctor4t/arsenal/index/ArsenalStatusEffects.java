package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.effect.StunStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArsenalStatusEffects {
    private static final Map<StatusEffect, Identifier> EFFECTS = new LinkedHashMap<>();

    public static final StatusEffect STUN = create("stun", new StunStatusEffect());

    public static void initialize() {
        EFFECTS.keySet().forEach(effect -> Registry.register(Registries.STATUS_EFFECT, EFFECTS.get(effect), effect));
    }

    private static <T extends StatusEffect> T create(String name, T effect) {
        EFFECTS.put(effect, Arsenal.id(name));
        return effect;
    }
}
