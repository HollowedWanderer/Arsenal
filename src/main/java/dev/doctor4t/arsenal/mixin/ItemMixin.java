package dev.doctor4t.arsenal.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void arsenal$throw(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Item item = (Item) (Object) this;
        if (item == Items.FIRE_CHARGE) {
            ItemStack stack = user.getStackInHand(hand);
            if (!world.isClient()) {
                world.syncWorldEvent(null, 1018, user.getBlockPos(), 0);
                Vec3d vec3d = user.getRotationVec(1.0F).normalize().multiply(2);
                SmallFireballEntity smallFireballEntity = new SmallFireballEntity(world, user, vec3d);
                smallFireballEntity.setPosition(smallFireballEntity.getX(), user.getEyeY(), smallFireballEntity.getZ());
                world.spawnEntity(smallFireballEntity);
                stack.decrement(1);
                user.getItemCooldownManager().set(Items.FIRE_CHARGE.getDefaultStack(), 6);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

//    @Inject(method = "inventoryTick", at = @At("HEAD"))
//    private void arsenal$setTridentOwner(ItemStack stack, ServerWorld world, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
//        if (stack.isOf(Items.TRIDENT) && entity instanceof PlayerEntity player) {
//            WeaponOwnerComponent weaponOwnerComponent = ArsenalComponents.WEAPON_OWNER_COMPONENT.get(stack);
//            weaponOwnerComponent.setOwner(player.getUuid());
//        }
//    }
}
