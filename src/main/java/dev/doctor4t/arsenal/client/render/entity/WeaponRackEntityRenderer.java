package dev.doctor4t.arsenal.client.render.entity;

import dev.doctor4t.arsenal.entity.WeaponRackEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WeaponRackEntityRenderer extends EntityRenderer<WeaponRackEntity> {

    public WeaponRackEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(WeaponRackEntity entity) {
        return null;
    }

    @Override
    public WeaponRackEntityRenderer() {
        return new ItemFrameEntityRenderer<>();
    }
}
