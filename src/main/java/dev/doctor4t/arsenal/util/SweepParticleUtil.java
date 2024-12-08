package dev.doctor4t.arsenal.util;

import dev.doctor4t.arsenal.Arsenal;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;

public class SweepParticleUtil {
    public static void sendSweepPacketToClient(ServerWorld world, Pair<Integer, Integer> colorPair, double x, double y, double z) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(colorPair.getLeft());
        buf.writeInt(colorPair.getRight());
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);

        for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
            ServerPlayNetworking.send(serverPlayerEntity, Arsenal.CLIENTBOUND_SWEEP_PACKET, buf);
        }
    }
}
