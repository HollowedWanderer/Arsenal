package dev.doctor4t.arsenal.cca;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.item.ArsenalWeaponItem;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;

public class ArsenalComponents implements EntityComponentInitializer, ItemComponentInitializer {
    public static final ComponentKey<BackWeaponComponent> BACK_WEAPON_COMPONENT = ComponentRegistry.getOrCreate(Arsenal.id("back_weapon"), BackWeaponComponent.class);
    public static final ComponentKey<WeaponSkinComponent> WEAPON_SKIN_COMPONENT = ComponentRegistry.getOrCreate(Arsenal.id("weapon_skin"), WeaponSkinComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, BACK_WEAPON_COMPONENT).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(BackWeaponComponent::new);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(item -> item instanceof ArsenalWeaponItem, WEAPON_SKIN_COMPONENT, WeaponSkinComponent::new);
    }
}
