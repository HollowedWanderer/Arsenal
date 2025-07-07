package dev.doctor4t.arsenal.client.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.doctor4t.arsenal.index.ArsenalDataComponents;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class AnchorbladeSkinProperty implements SelectProperty<AnchorbladeSkin> {
    public static final AnchorbladeSkinProperty INSTANCE = new AnchorbladeSkinProperty();
    public static final SelectProperty.Type<AnchorbladeSkinProperty, AnchorbladeSkin> TYPE = SelectProperty.Type.create(
            MapCodec.unit(INSTANCE),
            AnchorbladeSkin.CODEC
    );

    private AnchorbladeSkinProperty() {}

    @Nullable
    @Override
    public AnchorbladeSkin getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
        return stack.get(ArsenalDataComponents.ANCHORBLADE_SKIN);
    }

    @Override
    public Codec<AnchorbladeSkin> valueCodec() {
        return AnchorbladeSkin.CODEC;
    }

    @Override
    public Type<? extends SelectProperty<AnchorbladeSkin>, AnchorbladeSkin> getType() {
        return TYPE;
    }
}
