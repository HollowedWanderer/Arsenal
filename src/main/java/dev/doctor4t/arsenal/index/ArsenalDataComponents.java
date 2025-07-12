package dev.doctor4t.arsenal.index;

import com.mojang.serialization.Codec;
import dev.doctor4t.arsenal.Arsenal;
import dev.doctor4t.arsenal.item.skin.AnchorbladeSkin;
import dev.doctor4t.arsenal.item.skin.ScytheSkin;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public interface ArsenalDataComponents {

    ComponentType<AnchorbladeSkin> ANCHORBLADE_SKIN = register("anchorblade_skin", builder -> builder.codec(AnchorbladeSkin.CODEC).packetCodec(AnchorbladeSkin.PACKET_CODEC));
    ComponentType<ScytheSkin> SCYTHE_SKIN = register("scythe_skin", builder -> builder.codec(ScytheSkin.CODEC).packetCodec(ScytheSkin.PACKET_CODEC));
    ComponentType<String> SKIN_OWNER = register("skin_owner", builder -> builder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));

    // IMPALED
    ComponentType<Boolean> SOULD_RECALL = register("should_recall", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));
    ComponentType<Boolean> IS_DROPPED = register("is_dropped", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));
    ComponentType<String> LOYAL_PLAYER = register("loyal_player", builder -> builder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));
    ComponentType<String> LOYAL_PLAYER_NAME = register("loyal_player_name", builder -> builder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));
    ComponentType<Integer> SLOT_THROWN_FROM = register("slot_thrown_from", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));

    static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Arsenal.id(name), builderOperator.apply(ComponentType.builder()).build());
    }

    static void initialize() {}
}
