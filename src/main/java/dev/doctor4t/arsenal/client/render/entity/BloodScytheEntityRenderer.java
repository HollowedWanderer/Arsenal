package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.entity.BloodScytheEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class BloodScytheEntityRenderer extends EntityRenderer<BloodScytheEntity, EntityRenderState> {

    public BloodScytheEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}

