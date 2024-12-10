package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.index.ArsenalItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public class DrownedEntityMixin extends ZombieEntity {
    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/DrownedEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", ordinal = 0, shift = At.Shift.AFTER))
    protected void arsenal$guaranteeDrownedTridentDrop(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        this.updateDropChances(EquipmentSlot.MAINHAND);
    }

    @Inject(method = "initEquipment", at = @At(value = "TAIL"))
    protected void arsenal$equipAnchorbladeOnDrowned(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if ((double) random.nextFloat() > 0.9) {
            int i = random.nextInt(16);
            if (i < 10) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ArsenalItems.ANCHORBLADE));
                this.updateDropChances(EquipmentSlot.MAINHAND);
            }
        }
    }
}
