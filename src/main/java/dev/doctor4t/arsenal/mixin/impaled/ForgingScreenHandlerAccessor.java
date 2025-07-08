package dev.doctor4t.arsenal.mixin.impaled;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ForgingScreenHandler.class)
public interface ForgingScreenHandlerAccessor {
    @Accessor("input")
    Inventory getInput();
    @Accessor("player")
    PlayerEntity getPlayer();
}