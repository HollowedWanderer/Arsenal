package dev.doctor4t.arsenal.mixin.client;

import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Unique
    private static final Identifier HOTBAR_SELECTION_TEXTURE = Identifier.ofVanilla("hud/hotbar_selection");
    @Unique
    private static final Identifier HOTBAR_OFFHAND_LEFT_TEXTURE = Identifier.ofVanilla("hud/hotbar_offhand_left");
    @Unique
    private static final Identifier HOTBAR_OFFHAND_RIGHT_TEXTURE = Identifier.ofVanilla("hud/hotbar_offhand_right");

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void arsenal$renderWeaponSlot(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (player == null) return;
        ItemStack stack = BackWeaponComponent.getBackWeapon(player);
        if (!stack.isEmpty()) {
            int i = context.getScaledWindowWidth() / 2;
            if (BackWeaponComponent.isHoldingBackWeapon(player)) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_TEXTURE, i - 12, context.getScaledWindowHeight() - 23 - 70, 29, 24);
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_TEXTURE, i - 12 + 4, context.getScaledWindowHeight() - 23 - 70 + 4, 24, 23);

                int o = i - 90 + 4 * 20 + 2;
                int p = context.getScaledWindowHeight() - 19 - 70;
                this.renderHotbarItem(context, o, p, tickCounter, player, stack, 1);
            } else {
                Arm arm = player.getMainArm().getOpposite();
                if (arm == Arm.RIGHT) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_TEXTURE, i - 91 - 29, context.getScaledWindowHeight() - 23, 29, 24);
                } else {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_TEXTURE, i + 91, context.getScaledWindowHeight() - 23, 29, 24);
                }

                int n = context.getScaledWindowHeight() - 16 - 3;
                if (arm == Arm.RIGHT) {
                    this.renderHotbarItem(context, i - 91 - 26, n, tickCounter, player, stack, 0);
                } else {
                    this.renderHotbarItem(context, i + 91 + 10, n, tickCounter, player, stack, 0);
                }
            }
        }
    }

//    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1))
//    private void arsenal$selection(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
//        if (this.getCameraPlayer() != null && BackWeaponComponent.isHoldingBackWeapon(this.getCameraPlayer())) {
//            return;
//        }
//        original.call(instance, texture, x, y, u, v, width, height);
//    }
}
