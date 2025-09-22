package dev.doctor4t.arsenal.client.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class ScytheSkinProperty implements SelectProperty<ScytheSkin> {
    public static final ScytheSkinProperty INSTANCE = new ScytheSkinProperty();
    public static final Type<ScytheSkinProperty, ScytheSkin> TYPE = Type.create(
            MapCodec.unit(INSTANCE),
            ScytheSkin.CODEC
    );

    private ScytheSkinProperty() {}

    @Nullable
    @Override
    public ScytheSkin getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        return stack.get(ArsenalDataComponents.SCYTHE_SKIN);
    }

    @Override
    public Codec<ScytheSkin> valueCodec() {
        return ScytheSkin.CODEC;
    }

    @Override
    public Type<? extends SelectProperty<ScytheSkin>, ScytheSkin> getType() {
        return TYPE;
    }
}
