package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.client.render.feature.BackWeaponFeatureRenderer;
import dev.doctor4t.arsenal.index.ArsenalItems;
import dev.doctor4t.arsenal.util.AnchorOwner;
import dev.doctor4t.arsenal.util.EnchantmentListener;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "getArmPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/util/Arm;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", at = @At("HEAD"), cancellable = true)
    private static void arsenal$swordPoses(AbstractClientPlayerEntity player, Arm arm, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack stack = player.getStackInArm(arm);
        if (stack.isOf(ArsenalItems.ANCHORBLADE)) {
            boolean reeling = EnchantmentListener.hasEnchantment(stack, "arsenal:reeling");
            if (player instanceof AnchorOwner owner && owner.arsenal$isAnchorActive(player.getMainArm() == arm ? Hand.MAIN_HAND : Hand.OFF_HAND, reeling)) {
                cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void arsenal$backBlade(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.addFeature(new BackWeaponFeatureRenderer(this));
    }
}
