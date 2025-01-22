package me.rufia.fightorflight.net.packet;

import io.netty.buffer.ByteBuf;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.net.NetworkPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class SendCommandPacket implements NetworkPacket, CustomPacketPayload {
    public static final ResourceLocation SEND_COMMAND_PACKET_ID = ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, "send_command");
    protected int slot;
    protected String command;
    protected String commandData;
    private final boolean isFromPokeStaff;
    public static final StreamCodec<ByteBuf, SendCommandPacket> STREAM_CODEC;
    public static final Type<SendCommandPacket> TYPE = new Type<>(SEND_COMMAND_PACKET_ID);

    public int getSlot() {
        return slot;
    }

    public String getCommand() {
        return command;
    }

    public String getCommandData() {
        return commandData;
    }

    public boolean isFromPokeStaff(){
        return isFromPokeStaff;
    }

    public SendCommandPacket(int slot, String command, String commandData, boolean isFromPokeStaff) {
        this.slot = slot;
        this.command = command;
        this.commandData = commandData;
        this.isFromPokeStaff = isFromPokeStaff;
    }

    public SendCommandPacket(int slot, String command, String commandData) {
        this.slot = slot;
        this.command = command;
        this.commandData = commandData;
        this.isFromPokeStaff = false;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, SendCommandPacket::getSlot,
                ByteBufCodecs.STRING_UTF8, SendCommandPacket::getCommand,
                ByteBufCodecs.STRING_UTF8, SendCommandPacket::getCommandData,
                SendCommandPacket::new);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
