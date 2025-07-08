package dev.doctor4t.arsenal.mixin.impaled;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(TridentItem.class)
public class TridentItemMixin extends Item {
    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (stack.get(ArsenalDataComponents.SOULD_RECALL)!=null && Boolean.TRUE.equals(stack.get(ArsenalDataComponents.SOULD_RECALL))) {
            stack.set(ArsenalDataComponents.SOULD_RECALL, false);
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.get(ArsenalDataComponents.LOYAL_PLAYER_NAME)!=null){
            textConsumer.accept(Text.of("Bound To: "+stack.get(ArsenalDataComponents.LOYAL_PLAYER_NAME)));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
    @ModifyVariable(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;splitUnlessCreative(ILnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack addComponent(ItemStack value) {
        value.set(ArsenalDataComponents.IS_DROPPED,false);
        return value;
    }
    @Inject(method = "onStoppedUsing", at = @At(value = "HEAD"))
    public void pickupSavedSlotFrom(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {

        if (world.isClient()) return;
        if (user instanceof PlayerEntity player) {
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack itemStack = inventory.getStack(i);
                if (itemStack.equals(stack)) {
                    stack.set(ArsenalDataComponents.SLOT_THROWN_FROM, i);
                }
            }
        }
    }
}
