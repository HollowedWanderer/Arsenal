package dev.doctor4t.arsenal.index;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.entity.AnchorbladeEntity;
import dev.doctor4t.arsenal.entity.BloodScytheEntity;
import dev.doctor4t.arsenal.entity.WeaponRackEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ArsenalEntities {

    EntityType<BloodScytheEntity> BLOOD_SCYTHE = register(
            "blood_scythe",
            EntityType.Builder.create(BloodScytheEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .dimensions(5.0F, 0.2F)
    );

    EntityType<AnchorbladeEntity> ANCHORBLADE = register(
            "anchorblade",
            EntityType.Builder.create(AnchorbladeEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .dimensions(1.2F, 1.2F)
                    .maxTrackingRange(128)
    );

    EntityType<WeaponRackEntity> WEAPON_RACK = register(
            "weapon_rack",
            EntityType.Builder.create(WeaponRackEntity::new, SpawnGroup.MISC)
                    .disableSaving()
                    .dimensions(0.4F, 0.4F)
                    .maxTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
    );

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Arsenal.MOD_ID, id));
    }

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    static void initialize() {}
}
