package dev.doctor4t.arsenal.util;

import net.minecraft.item.ItemStack;

public interface WeaponSlotHolder {
    int arsenal$getSlotHolding(ItemStack stack);

    boolean arsenal$tryInsertIntoSlot(int slot, ItemStack stack);
}
