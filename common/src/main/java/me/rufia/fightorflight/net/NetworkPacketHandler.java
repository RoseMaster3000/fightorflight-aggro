package me.rufia.fightorflight.net;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

public interface NetworkPacketHandler<T extends NetworkPacket> {
    void handle(T packet, NetworkManager.PacketContext context);
}
