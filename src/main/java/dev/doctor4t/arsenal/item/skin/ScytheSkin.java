package dev.doctor4t.arsenal.item.skin;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum ScytheSkin implements StringIdentifiable {
    DEFAULT("default", 0xFFD9D9D9, 0xFF7F8885, null, null),
    CLOWN("clown", 0xFFD90420, 0xFF8C0420, null, "tooltip.arsenal.scythe_clown"),
    CARRION("carrion", 0xFFE9DFB8, 0xFF9D806E, null, null),
    GILDED("gilded", 0xFFF1BC5A, 0xFFE28634, null, null),
    ROZE("roze", 0xFFB70066, 0xFF710949, null, null),
    FOLLY("folly", 0xFFFF005A, 0xFFBC0045, "Folly Tree Branch", null),
    SCISSORS("scissors", 0xFFB9B1AF, 0xFF6F686F, null, null);

    public static final Codec<ScytheSkin> CODEC = StringIdentifiable.createCodec(ScytheSkin::values);

    private static final IntFunction<ScytheSkin> INDEX_MAPPER = ValueLists.createIdToValueFunction(ScytheSkin::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final PacketCodec<ByteBuf, ScytheSkin> PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, ScytheSkin::ordinal);

    public final String name;
    public final int color;
    public final int shadowColor;
    public final @Nullable String lore;
    public final @Nullable String tooltipName;

    ScytheSkin(String name, int color, int shadowColor, @Nullable String tooltipName, @Nullable String lore) {
        this.name = name;
        this.color = color;
        this.shadowColor = shadowColor;
        this.lore = lore;
        this.tooltipName = tooltipName;
    }

    @Nullable
    public static ScytheSkin fromString(String name) {
        for (ScytheSkin skin : ScytheSkin.values()) if (skin.asString().equalsIgnoreCase(name)) return skin;
        return null;
    }

    public static ScytheSkin getNext(ScytheSkin skin) {
        ScytheSkin[] values = ScytheSkin.values();
        return values[(skin.ordinal() + 1) % values.length];
    }

    @Override
    public String asString() {
        return this.name;
    }
}