package dev.doctor4t.arsenal.payload;

import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public record InventorySwapPayload(int slotId) implements CustomPayload {
    public static Id<InventorySwapPayload> ID = new Id<>(Arsenal.id("inventory_swap"));
    public static PacketCodec<RegistryByteBuf, InventorySwapPayload> CODEC = PacketCodec.of(InventorySwapPayload::write, InventorySwapPayload::new);

    public InventorySwapPayload(RegistryByteBuf buf) {
        this(buf.readInt());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(slotId);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<InventorySwapPayload> {

        @Override
        public void receive(InventorySwapPayload payload, ServerPlayNetworking.Context context) {
            ServerPlayerEntity player = context.player();
            int slotId = payload.slotId;
            if (!player.isSpectator() && !player.isCreative()) {
                if (!player.currentScreenHandler.isValid(slotId)) {
                    return;
                }
                Slot slot = player.currentScreenHandler.getSlot(slotId);
                ItemStack itemStack = BackWeaponComponent.getBackWeapon(player);
                boolean success = BackWeaponComponent.setBackWeapon(player, slot.getStack());
                if (success) {
                    slot.setStack(itemStack);
                }
            }
        }
    }
}
