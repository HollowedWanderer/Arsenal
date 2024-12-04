package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public interface ArsenalDamageTypes {
    RegistryKey<DamageType> ANCHOR = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Arsenal.id("anchor"));
    RegistryKey<DamageType> BLOOD_SCYTHE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Arsenal.id("blood_scythe"));
    RegistryKey<DamageType> SPEWING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Arsenal.id("spewing"));
}
