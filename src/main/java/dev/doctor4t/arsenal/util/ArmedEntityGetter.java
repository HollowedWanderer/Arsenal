package dev.doctor4t.arsenal.util;

import net.minecraft.entity.LivingEntity;

public interface ArmedEntityGetter {
    void arsenal$setEntity(LivingEntity entity);
    LivingEntity arsenal$getEntity();
}
