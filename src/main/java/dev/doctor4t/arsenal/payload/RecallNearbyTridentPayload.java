package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.TridentEntityComponent;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public record RecallNearbyTridentPayload() implements CustomPayload {
    public static Id<RecallNearbyTridentPayload> ID = new Id<>(Arsenal.id("recall_nearby_trident"));
    public static PacketCodec<RegistryByteBuf, RecallNearbyTridentPayload> CODEC = PacketCodec.unit(new RecallNearbyTridentPayload());


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<RecallNearbyTridentPayload> {
        @Override
        public void receive(RecallNearbyTridentPayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();
            Box area = new Box(player.getBlockPos()).expand(60);

            List<TridentEntity> nearbyEntities = player.getWorld().getEntitiesByClass(TridentEntity.class, area, TridentEntity::isAlive);
            List<PlayerEntity> nearbyPlayers = player.getWorld().getEntitiesByClass(PlayerEntity.class, area, PlayerEntity::isAlive);

            for (TridentEntity itemEntity : nearbyEntities) {
                if (itemEntity.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER) != null) {
                    if (Objects.equals(itemEntity.getItemStack().get(ArsenalDataComponents.LOYAL_PLAYER), player.getUuid().toString())) {
                        itemEntity.getItemStack().set(ArsenalDataComponents.SOULD_RECALL,true);
                        itemEntity.getItemStack().set(ArsenalDataComponents.IS_DROPPED,false);
                        TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get(itemEntity);
                        itemEntity.getItemStack().set(ArsenalDataComponents.SLOT_THROWN_FROM,player.getInventory().getSelectedSlot());
                        tridentEntityComponent.setisDropped(false);
                        tridentEntityComponent.setslotDroppedFrom(player.getInventory().getSelectedSlot());
                    }
                }
            }
            for (PlayerEntity playerEntity : nearbyPlayers) {
                for (int i = 0; i < playerEntity.getInventory().size(); i++) {
                    if (playerEntity != player) {
                        if (playerEntity.getInventory().getStack(i).isOf(Items.TRIDENT) && Objects.equals(playerEntity.getInventory().getStack(i).get(ArsenalDataComponents.LOYAL_PLAYER), player.getUuid().toString())) {
                            TridentEntity tridentEntity = new TridentEntity(playerEntity.getWorld(), player, playerEntity.getInventory().getStack(i));
                            tridentEntity.setPos(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ());
                            playerEntity.getInventory().setStack(i, ItemStack.EMPTY);
                            tridentEntity.getItemStack().set(ArsenalDataComponents.SOULD_RECALL,true);
                            playerEntity.getWorld().spawnEntity(tridentEntity);
                        }
                    }
                }
            }
        }
    }
}
