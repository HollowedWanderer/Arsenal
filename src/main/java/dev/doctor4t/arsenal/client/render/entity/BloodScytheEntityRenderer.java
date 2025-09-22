package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.entity.BloodScytheEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class BloodScytheEntityRenderer extends EntityRenderer<BloodScytheEntity> {

    public BloodScytheEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BloodScytheEntity entity) {
        return null;
    }

    @Override
    public EntityRenderer createRenderState() {
        return new EntityRenderer<>() {
            @Override
            public Identifier getTexture(Entity entity) {
                return null;
            }
        };
    }
}

