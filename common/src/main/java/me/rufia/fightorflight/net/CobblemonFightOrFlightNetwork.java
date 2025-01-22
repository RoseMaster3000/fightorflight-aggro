package me.rufia.fightorflight.net;

import dev.architectury.networking.NetworkManager;
import me.rufia.fightorflight.net.handler.SendCommandHandler;
import me.rufia.fightorflight.net.handler.SendMoveSlotHandler;
import me.rufia.fightorflight.net.packet.SendCommandPacket;
import me.rufia.fightorflight.net.packet.SendMoveSlotPacket;

public class CobblemonFightOrFlightNetwork {
    public static void init(){
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SendCommandPacket.TYPE, SendCommandPacket.STREAM_CODEC, ((packet, context) -> {
            SendCommandHandler handler = new SendCommandHandler();
            handler.handle(packet, context);
        }));
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SendMoveSlotPacket.TYPE, SendMoveSlotPacket.STREAM_CODEC, ((packet, context) -> {
            SendMoveSlotHandler handler = new SendMoveSlotHandler();
            handler.handle(packet, context);
        }));
    }
}
