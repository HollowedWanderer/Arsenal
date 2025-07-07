package dev.doctor4t.arsenal.mixin;

import dev.doctor4t.arsenal.util.DecorationEntityAttachmentSetter;
import net.minecraft.entity.decoration.BlockAttachedEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockAttachedEntity.class)
public class AbstractDecorationEntityMixin implements DecorationEntityAttachmentSetter {
    @Shadow protected BlockPos attachedBlockPos;

    @Override
    public void arsenal$setPos(BlockPos pos) {
        this.attachedBlockPos = pos;
    }
}
