package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.util.ProjectileSlotHolder;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin implements ProjectileSlotHolder {
    private int arsenal$ownedSlot = -1;

    @Override
    public int arsenal$getOwnedSlot() {
        return this.arsenal$ownedSlot;
    }

    @Override
    public void arsenal$setOwnedSlot(int slot) {
        this.arsenal$ownedSlot = slot;
    }
}
