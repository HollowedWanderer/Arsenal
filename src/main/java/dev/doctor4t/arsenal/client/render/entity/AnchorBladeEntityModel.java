package dev.doctor4t.arsenal.client.render.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;

public class AnchorBladeEntityModel extends EntityModel<ProjectileEntityRenderState> {
    private final ModelPart bone;
    private final ModelPart attachment;

    public AnchorBladeEntityModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.attachment = this.bone.getChild("attachment");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create()
                        .uv(0, 14).cuboid(-1.0F, -9.0F, -1.0F, 2.0F, 18.0F, 2.0F, new Dilation(0.0F))
                        .uv(0, 10).cuboid(-7.0F, -11.0F, -1.0F, 14.0F, 2.0F, 2.0F, new Dilation(0.0F))
                        .uv(0, 0).cuboid(-9.0F, 1.0F, 0.0F, 18.0F, 10.0F, 0.0F, new Dilation(0.001F)),
                ModelTransform.origin(0.0F, 13.0F, 0.0F));
        bone.addChild("bone2", ModelPartBuilder.create()
                        .uv(0, 54).cuboid(-9.0F, -3.0F, 7.0F, 5.0F, 0.0F, 5.0F, new Dilation(0.001F))
                        .uv(0, 48).cuboid(-12.0F, -6.0F, 6.0F, 5.0F, 0.0F, 6.0F, new Dilation(0.001F))
                        .uv(0, 59).cuboid(-12.0F, 0.0F, 4.0F, 5.0F, 0.0F, 5.0F, new Dilation(0.001F))
                        .uv(6, 40).cuboid(-12.0F, 1.0F, 6.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.001F))
                        .uv(6, 36).cuboid(-4.0F, 1.0F, 6.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.001F)),
                ModelTransform.origin(8.0F, 4.0F, -8.0F));

        bone.addChild("attachment", ModelPartBuilder.create()
                        .uv(8, 14).cuboid(-2.0F, -13.75F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.0F)),
                ModelTransform.origin(0.0F, -1.25F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
