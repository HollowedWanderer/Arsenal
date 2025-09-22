package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerCapeModelMixin.class)
public class PlayerCapeModelMixin {

    @Shadow @Final private ModelPart cape;

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void injectSetAngles(PlayerEntityRenderer playerEntityRenderState, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            this.cape.resetTransform();

            float sway = 1;
            if ((!BackWeaponComponent.getBackWeapon(client.player).isEmpty() && !BackWeaponComponent.isHoldingBackWeapon(client.player))) {
                sway = 0.35F;
            }

            float adjustedXRotation = sway * (6.0F + playerEntityRenderState.field_53537 / 2.0F + playerEntityRenderState.field_53536)
                    * 0.017453292F;
            float zRotation = playerEntityRenderState.field_53538 / 2.0F * 0.017453292F;
            float yRotation = (180.0F - playerEntityRenderState.field_53538 / 2.0F) * 0.017453292F;

            Quaternionf capeRotation = new Quaternionf()
                    .rotateY(-3.1415927F)
                    .rotateX(adjustedXRotation)
                    .rotateZ(zRotation)
                    .rotateY(yRotation);
            this.cape.rotate(capeRotation);

            ci.cancel();
        }
    }
}
