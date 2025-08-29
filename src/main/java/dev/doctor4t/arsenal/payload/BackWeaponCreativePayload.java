package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record BackWeaponCreativePayload(ItemStack stack) implements CustomPayload {
    public static Id<BackWeaponCreativePayload> ID = new Id<>(Arsenal.id("creative_swap"));
    public static PacketCodec<RegistryByteBuf, BackWeaponCreativePayload> CODEC = PacketCodec.of(BackWeaponCreativePayload::write, BackWeaponCreativePayload::new);

    public BackWeaponCreativePayload(RegistryByteBuf buf) {
        this(buf.readBoolean() ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY);
    }

    public void write(RegistryByteBuf buf) {
        boolean hasItem = !stack.isEmpty();
        buf.writeBoolean(hasItem);
        if (hasItem) {
            ItemStack.PACKET_CODEC.encode(buf, stack);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BackWeaponCreativePayload> {

        @Override
        public void receive(BackWeaponCreativePayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();

            if (player.isCreative()) {
                BackWeaponComponent.setBackWeapon(player, payload.stack);
            }
        }
    }
}
