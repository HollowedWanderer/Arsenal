package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record HoldWeaponPayload(boolean hold) implements CustomPayload {
    public static Id<HoldWeaponPayload> ID = new Id<>(Arsenal.id("hold_weapon"));
    public static PacketCodec<RegistryByteBuf, HoldWeaponPayload> CODEC = PacketCodec.of(HoldWeaponPayload::write, HoldWeaponPayload::new);

    public HoldWeaponPayload(RegistryByteBuf buf) {
        this(buf.readBoolean());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBoolean(hold);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<HoldWeaponPayload> {

        @Override
        public void receive(HoldWeaponPayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();
            BackWeaponComponent.setHoldingBackWeapon(player, payload.hold);
        }
    }
}
