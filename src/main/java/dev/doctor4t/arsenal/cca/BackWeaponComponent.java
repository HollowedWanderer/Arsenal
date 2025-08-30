package dev.doctor4t.arsenal.cca;

import dev.doctor4t.arsenal.payload.HoldWeaponPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class BackWeaponComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final SimpleInventory backWeapon = new SimpleInventory(1);
    private boolean holdingBackWeapon = false;

    public BackWeaponComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readData(ReadView readView) {
        this.backWeapon.setStack(0, readView.read("backWeapon", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.holdingBackWeapon = readView.getBoolean("holdingBackWeapon", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("backWeapon", ItemStack.CODEC, this.backWeapon.getStack(0));
        writeView.putBoolean("holdingBackWeapon", this.holdingBackWeapon);
    }

    public ItemStack getBackWeapon() {
        return this.backWeapon.getStack(0);
    }

    public static ItemStack getBackWeapon(PlayerEntity player) {
        return ArsenalComponents.BACK_WEAPON_COMPONENT.get(player).getBackWeapon();
    }

    public boolean setBackWeapon(ItemStack backWeapon) {
        this.backWeapon.setStack(0, backWeapon);
        ArsenalComponents.BACK_WEAPON_COMPONENT.sync(this.player);
        return true;
    }

    public static boolean setBackWeapon(PlayerEntity player, ItemStack backWeapon) {
        return ArsenalComponents.BACK_WEAPON_COMPONENT.get(player).setBackWeapon(backWeapon);
    }

    public SimpleInventory getBackWeaponInventory() {
        return this.backWeapon;
    }

    public static SimpleInventory getBackWeaponInventory(PlayerEntity player) {
        return ArsenalComponents.BACK_WEAPON_COMPONENT.get(player).getBackWeaponInventory();
    }

    public boolean isHoldingBackWeapon() {
        return this.holdingBackWeapon;
    }

    public static boolean isHoldingBackWeapon(PlayerEntity player) {
        return ArsenalComponents.BACK_WEAPON_COMPONENT.get(player).isHoldingBackWeapon();
    }

    public void setHoldingBackWeapon(boolean holdingBackWeapon) {
        this.holdingBackWeapon = holdingBackWeapon;
        ArsenalComponents.BACK_WEAPON_COMPONENT.sync(this.player);
    }

    public static void setHoldingBackWeapon(PlayerEntity player, boolean holdingBackWeapon) {
        if (player.getWorld().isClient()) {
            ClientPlayNetworking.send(new HoldWeaponPayload(holdingBackWeapon));
            return;
        }
        ArsenalComponents.BACK_WEAPON_COMPONENT.get(player).setHoldingBackWeapon(holdingBackWeapon);
    }
}
