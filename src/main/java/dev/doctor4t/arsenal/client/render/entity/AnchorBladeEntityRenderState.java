package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.entity.AnchorbladeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class AnchorBladeEntityRenderState extends ProjectileEntityRenderState {
    public float prevYaw;
    public float prevPitch;
    public ItemStack stack;
    public Entity owner;
    public AnchorbladeEntity entity;
}
