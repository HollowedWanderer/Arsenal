package dev.doctor4t.arsenal.item.skin;

import com.mojang.serialization.Codec;
import dev.doctor4t.arsenal.Arsenal;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum AnchorbladeSkin implements StringIdentifiable {
    DEFAULT("default", new int[]{0xFF2B2632}, new int[]{0xFF1B1B1B}, null, null),
    LUXINTRUS("luxintrus", new int[]{0xFFE7761F, 0xFF37965B, 0xFFA51BB7}, new int[]{0xFFA84701, 0xFF115642, 0xFF671081}, "L'Ancre", "tooltip.arsenal.anchorblade_luxintrus"),
    CARRION("carrion", new int[]{0xFFE9DFB8}, new int[]{0xFF9D806E}, null, null),
    GILDED("gilded", new int[]{0xFFF1BC5A}, new int[]{0xFFE28634}, null, null),
    WINSWEEP("winsweep", new int[]{0xFFFFDC00, 0xFFC676F1}, new int[]{0xFFBE5F00, 0xFF7546A0}, "Wanchorblade", null),
    AMBESSA("ambessa", new int[]{0xFFA9A9A7}, new int[]{0xFF6A6D66}, "Crescent Blade", null);
    //MAINTAINER("maintainer", new int[]{0xFFA9A9A7}, new int[]{0xFF6A6D66}, "Maintainer", null);

    public static final Codec<AnchorbladeSkin> CODEC = StringIdentifiable.createCodec(AnchorbladeSkin::values);

    private static final IntFunction<AnchorbladeSkin> INDEX_MAPPER = ValueLists.createIndexToValueFunction(AnchorbladeSkin::ordinal, values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final PacketCodec<ByteBuf, AnchorbladeSkin> PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, AnchorbladeSkin::ordinal);

    public final String name;
    public final Identifier chainTexture;
    public final Identifier anchorbladeEntityModel;
    public final int[] colors;
    public final int[] shadowColors;
    public final @Nullable String tooltipName;
    public final @Nullable String lore;

    AnchorbladeSkin(String name, int[] colors, int[] shadowColors, @Nullable String tooltipName, @Nullable String lore) {
        this.name = name;
        this.chainTexture = Arsenal.id(this.asString().equals("default") ? "textures/entity/chain.png" : "textures/entity/chain_" + this.asString() + ".png");
        this.anchorbladeEntityModel = Arsenal.id(this.asString().equals("default") ? "item/anchorblade_in_hand" : "item/anchorblade_" + this.asString() + "_in_hand");
        this.colors = colors;
        this.shadowColors = shadowColors;
        this.tooltipName = tooltipName;
        this.lore = lore;
    }

    public int getFirstColor() {
        return this.colors[0];
    }

    public Pair<Integer, Integer> getRandomParticleColorPair(Random random) {
        int i = random.nextInt(this.colors.length);
        return new Pair<>(this.colors[i], this.shadowColors[i]);
    }

    @Nullable
    public static AnchorbladeSkin fromString(String name) {
        for (AnchorbladeSkin skin : AnchorbladeSkin.values()) if (skin.asString().equalsIgnoreCase(name)) return skin;
        return null;
    }

    public static AnchorbladeSkin getNext(AnchorbladeSkin skin) {
        AnchorbladeSkin[] values = AnchorbladeSkin.values();
        return values[(skin.ordinal() + 1) % values.length];
    }

    @Override
    public String asString() {
        return this.name;
    }
}