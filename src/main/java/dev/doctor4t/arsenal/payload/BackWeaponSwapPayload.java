package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public record BackWeaponSwapPayload() implements CustomPayload {
    public static Id<BackWeaponSwapPayload> ID = new Id<>(Arsenal.id("back_swap"));
    public static PacketCodec<RegistryByteBuf, BackWeaponSwapPayload> CODEC = PacketCodec.unit(new BackWeaponSwapPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<BackWeaponSwapPayload> {

        @Override
        public void receive(BackWeaponSwapPayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();

            if (!player.isSpectator()) {
                boolean toggled = BackWeaponComponent.isHoldingBackWeapon(player);
                BackWeaponComponent.setHoldingBackWeapon(player, false);
                ItemStack itemStack = BackWeaponComponent.getBackWeapon(player);
                boolean success = BackWeaponComponent.setBackWeapon(player, player.getStackInHand(Hand.MAIN_HAND));
                if (success) {
                    player.setStackInHand(Hand.MAIN_HAND, itemStack);
                }
                player.clearActiveItem();
                BackWeaponComponent.setHoldingBackWeapon(player, toggled);
            }
        }
    }
}
