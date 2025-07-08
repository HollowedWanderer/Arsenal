package dev.doctor4t.arsenal.mixin.impaled;

import dev.doctor4t.arsenal.cca.TridentEntityComponent;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "dropItem", at = @At(value = "HEAD"), cancellable = true)
    public void renderFirstPersonItem(ItemStack stack, boolean dropAtSelf, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {

        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity instanceof PlayerEntity playerEntity) {
            if (stack.isOf(Items.TRIDENT) && playerEntity.getWorld() instanceof ServerWorld serverWorld) {
                RegistryEntry<Enchantment> loyaltyEntry = livingEntity.getWorld().getRegistryManager().getEntryOrThrow(Enchantments.LOYALTY);
                if (EnchantmentHelper.getLevel(loyaltyEntry, stack) == 4) {
                    stack.set(ArsenalDataComponents.IS_DROPPED, true);
                    if (Objects.equals(stack.get(ArsenalDataComponents.LOYAL_PLAYER_NAME), playerEntity.getNameForScoreboard())){
                    stack.set(ArsenalDataComponents.LOYAL_PLAYER,playerEntity.getUuid().toString());
                    }
                    stack.set(ArsenalDataComponents.SLOT_THROWN_FROM,playerEntity.getInventory().getSelectedSlot());
                    TridentEntity tridentEntity = new TridentEntity(playerEntity.getWorld(), playerEntity, stack);
                    TridentEntityComponent tridentEntityComponent = TridentEntityComponent.get(tridentEntity);
                    tridentEntityComponent.setisDropped(true);
                    tridentEntityComponent.sync();
                    PlayerInventory inventory = playerEntity.getInventory();
                    for (int i = 0; i < inventory.size(); i++) {
                        ItemStack itemStack = inventory.getStack(i);
                        if (itemStack.equals(stack)) {
                            stack.set(ArsenalDataComponents.SLOT_THROWN_FROM, i);
                        }
                    }
                    tridentEntityComponent.setslotDroppedFrom(playerEntity.getInventory().getSelectedSlot());
                    tridentEntity.setPos(playerEntity.getX(), playerEntity.getY() + 1, playerEntity.getZ());
                    tridentEntity.setVelocity(playerEntity,playerEntity.getPitch(),playerEntity.getYaw(),0,0.35f,1.0f);
                    playerEntity.getWorld().spawnEntity(tridentEntity);
                    cir.setReturnValue(new ItemEntity(playerEntity.getWorld(), 0, 0, 0, ItemStack.EMPTY));
                }
            }
        }
    }
}
