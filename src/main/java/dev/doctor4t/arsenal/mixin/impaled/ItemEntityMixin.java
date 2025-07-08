package dev.doctor4t.arsenal.mixin.impaled;

import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void renderFirstPersonItem(CallbackInfo ci) {

        ItemEntity itemEntity = (ItemEntity) (Object) this;
        ItemStack stack = itemEntity.getStack();
        if (stack.get(ArsenalDataComponents.LOYAL_PLAYER)!=null && itemEntity.getWorld() instanceof ServerWorld serverWorld) {
            UUID playerUuid = UUID.fromString(stack.get(ArsenalDataComponents.LOYAL_PLAYER));
            PlayerEntity player = serverWorld.getPlayerByUuid(playerUuid);
            if (player!=null) {
                    if (stack.isOf(Items.TRIDENT) && !itemEntity.getWorld().isClient()) {
                        stack.set(ArsenalDataComponents.SOULD_RECALL, true);
                        TridentEntity tridentEntity = new TridentEntity(itemEntity.getWorld(), player, stack);
                        tridentEntity.setPos(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
                        itemEntity.getWorld().spawnEntity(tridentEntity);
                        itemEntity.discard();
                }
            }
        }
    }
}
