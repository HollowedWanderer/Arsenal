package dev.doctor4t.arsenal.util;

import dev.doctor4t.arsenal.payload.BackWeaponCreativePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BackSlot extends Slot {
    public BackSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        super.setStack(stack, previousStack);
        ClientPlayNetworking.send(new BackWeaponCreativePayload(stack));
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        super.setStackNoCallbacks(stack);
        ClientPlayNetworking.send(new BackWeaponCreativePayload(stack));
    }
}
