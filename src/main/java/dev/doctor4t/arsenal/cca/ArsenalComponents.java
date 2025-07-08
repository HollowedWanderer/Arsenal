package dev.doctor4t.arsenal.cca;

import dev.doctor4t.arsenal.Arsenal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ArsenalComponents implements EntityComponentInitializer {

    public static final ComponentKey<BackWeaponComponent> BACK_WEAPON_COMPONENT = ComponentRegistry.getOrCreate(Arsenal.id("back_weapon"), BackWeaponComponent.class);

    //IMPALED
    public static ComponentKey<TridentEntityComponent> TRIDENT_ENTITY_COMPONENT= ComponentRegistry.getOrCreate(Arsenal.id("trident_entity_component"), TridentEntityComponent.class);
    public static ComponentKey<RecallingPlayerAnimationComponent> RECALLING_PLAYER_ANIMATION_COMPONENT= ComponentRegistry.getOrCreate(Arsenal.id("recalling_player_animation_component"), RecallingPlayerAnimationComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, BACK_WEAPON_COMPONENT).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(BackWeaponComponent::new);

        //IMPALED
        registry.registerFor(TridentEntity.class,TRIDENT_ENTITY_COMPONENT, TridentEntityComponent::new);
        registry.registerForPlayers(RECALLING_PLAYER_ANIMATION_COMPONENT,RecallingPlayerAnimationComponent::new,RespawnCopyStrategy.NEVER_COPY);
    }
}
