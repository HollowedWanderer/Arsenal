package doctor4t.arsenal.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import doctor4t.arsenal.common.util.WeaponSlot;
import doctor4t.arsenal.mixin.accessors.CreativeSlotAccessor;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
	@Shadow
	private static int selectedTab;

	public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@WrapOperation(method = "setSelectedTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;add(Ljava/lang/Object;)Z", ordinal = 2))
	private boolean arsenal$moveWeaponSlot(DefaultedList<Slot> slots, Object object, Operation<Boolean> operation) {
		if (object instanceof CreativeInventoryScreen.CreativeSlot newSlot) {
			Slot slot = ((CreativeSlotAccessor) newSlot).getSlot();
			if (slot instanceof WeaponSlot) {
				return operation.call(slots, new CreativeInventoryScreen.CreativeSlot(slot, slot.id, 127, 20));
			}
		}
		return operation.call(slots, object);
	}

	@Inject(method = "drawBackground", at = @At(value = "TAIL"))
	private void arsenal$drawSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		ItemGroup itemGroup = ItemGroup.GROUPS[selectedTab];
		if (itemGroup == ItemGroup.INVENTORY) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE);
			int i = this.x + 126;
			int j = this.y + 19;
			this.drawTexture(matrices, i, j, 76, 61, 18, 18);
//			if (this.inventory.arsenal$getWeapon().isEmpty()) {					fixme: I don't know why, I don't want to know why, the texture won't render in the slot
//				RenderSystem.setShader(GameRenderer::getPositionTexShader);
//				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//				RenderSystem.setShaderTexture(0, SLOT_TEXTURE);
//				this.drawTexture(matrices, i + 1, j + 1, 0, 0, 16, 16);
//			}
		}
	}
}
