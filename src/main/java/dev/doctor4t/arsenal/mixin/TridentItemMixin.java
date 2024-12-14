package dev.doctor4t.arsenal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.arsenal.cca.ArsenalComponents;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.util.ProjectileSlotHolder;
import dev.doctor4t.arsenal.util.WeaponSlotHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean arsenal$spawnEntity(World world, Entity entity, Operation<Boolean> operation, @Local(ordinal = 0) ItemStack stack, @Local(ordinal = 0) LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            BackWeaponComponent backWeaponComponent = ArsenalComponents.BACK_WEAPON_COMPONENT.get(player);
            if (backWeaponComponent.getBackWeapon().isOf(Items.TRIDENT)) { // clear trident
                backWeaponComponent.getBackWeapon().decrement(1);
            }

            if (player.getInventory() instanceof WeaponSlotHolder holder && entity instanceof ProjectileSlotHolder slotHolder) {


                int index = holder.arsenal$getSlotHolding(stack);
                if (index != -1) {
                    slotHolder.arsenal$setOwnedSlot(index);
                }
            }
        }
        return operation.call(world, entity);
    }
}
