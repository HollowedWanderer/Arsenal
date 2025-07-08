package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.ArsenalComponents;
import dev.doctor4t.arsenal.cca.RecallingPlayerAnimationComponent;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public record IsRecallingPayload() implements CustomPayload {
    public static Id<IsRecallingPayload> ID = new Id<>(Arsenal.id("is_recalling"));
    public static PacketCodec<RegistryByteBuf, IsRecallingPayload> CODEC = PacketCodec.unit(new IsRecallingPayload());


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<IsRecallingPayload> {
        @Override
        public void receive(IsRecallingPayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();
            RecallingPlayerAnimationComponent component = ArsenalComponents.RECALLING_PLAYER_ANIMATION_COMPONENT.get(player);
            Box area = new Box(player.getBlockPos()).expand(60);

            List<TridentEntity> nearbyEntities = player.getWorld().getEntitiesByClass(TridentEntity.class, area, TridentEntity::isAlive);
            List<PlayerEntity> nearbyPlayers = player.getWorld().getEntitiesByClass(PlayerEntity.class, area, PlayerEntity::isAlive);

            for (TridentEntity itemEntity : nearbyEntities) {
                if (itemEntity.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER) != null) {
                    if (Objects.equals(itemEntity.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER), player.getUuid().toString())) {
                        component.setIsRecalling(!component.getIsRecalling());
                        component.sync();
                    }
                }
            }
            if (nearbyEntities.isEmpty() && component.getIsRecalling()){
                component.setIsRecalling(!component.getIsRecalling());
                component.sync();
            }
            for (PlayerEntity playerEntity : nearbyPlayers) {
                for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                    if (playerEntity != player) {
                        if (playerEntity.getInventory().getStack(i).isOf(Items.TRIDENT) && Objects.equals(playerEntity.getInventory().getStack(i).get(ArsenalDataComponents.LOYAL_PLAYER), player.getUuid().toString())) {
                            component.setIsRecalling(!component.getIsRecalling());
                            component.sync();
                        }
                    }
                }
            }
            if (nearbyPlayers.isEmpty() && component.getIsRecalling()){
                component.setIsRecalling(!component.getIsRecalling());
                component.sync();
            }
        }
    }
}
