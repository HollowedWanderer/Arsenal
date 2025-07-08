package dev.doctor4t.arsenal.mixin.impaled;

import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Inject(method = "canTakeOutput", at = @At("RETURN"), cancellable = true)
    private void canTakeResult(PlayerEntity playerEntity, boolean resultNonEmpty, CallbackInfoReturnable<Boolean> cir) {
        Inventory inputInventory = ((ForgingScreenHandlerAccessor) this).getInput();
        if (resultNonEmpty && !cir.getReturnValueZ()) {
            ItemStack item = inputInventory.getStack(0);
            ItemStack upgradeItem = inputInventory.getStack(1);
            cir.setReturnValue(item.isOf(Items.TRIDENT) && upgradeItem.isOf(Items.HEART_OF_THE_SEA));
        }
    }

    @ModifyArg(
            method = "updateResult",
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;EMPTY:Lnet/minecraft/item/ItemStack;")),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"
            )
    )
    private ItemStack updateResult(ItemStack initialResult) {
        PlayerEntity player = ((ForgingScreenHandlerAccessor) this).getPlayer();
        if (initialResult.isEmpty()) {
            Inventory inputInventory = ((ForgingScreenHandlerAccessor) this).getInput();
            ItemStack item = inputInventory.getStack(0);
            ItemStack upgradeItem = inputInventory.getStack(1);
            if (item.isOf(Items.TRIDENT) && upgradeItem.isOf(Items.HEART_OF_THE_SEA)) {
                RegistryEntry<Enchantment> loyaltyEntry = player.getWorld().getRegistryManager().getEntryOrThrow(Enchantments.LOYALTY);
                if (EnchantmentHelper.getLevel(loyaltyEntry,item)==3) {
                    ItemStack result = item.copy();

                    result.addEnchantment(loyaltyEntry,EnchantmentHelper.getLevel(loyaltyEntry,item)+1);
                    result.set(ArsenalDataComponents.LOYAL_PLAYER,player.getUuid().toString());
                    result.set(ArsenalDataComponents.LOYAL_PLAYER_NAME,player.getNameForScoreboard());

                    return result;
                }
            }
        }
        return initialResult;
    }
}
