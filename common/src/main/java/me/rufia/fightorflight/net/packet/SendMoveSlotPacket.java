package me.rufia.fightorflight.net.packet;

import io.netty.buffer.ByteBuf;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.net.NetworkPacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class SendMoveSlotPacket implements NetworkPacket, CustomPacketPayload {
    public static final ResourceLocation SEND_MOVE_SLOT_PACKET_ID = ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, "send_move_slot");
    protected int slot;
    protected int moveSlot;
    private final boolean isFromPokeStaff;
    public static final StreamCodec<ByteBuf, SendMoveSlotPacket> STREAM_CODEC;
    public static final Type<SendMoveSlotPacket> TYPE = new Type<>(SEND_MOVE_SLOT_PACKET_ID);

    public int getSlot() {
        return slot;
    }

    public int getMoveSlot() {
        return moveSlot;
    }

    public boolean isFromPokeStaff(){
        return isFromPokeStaff;
    }

    public SendMoveSlotPacket(int slot, int moveSlot,boolean isFromPokeStaff) {
        this.slot = slot;
        this.moveSlot = moveSlot;
        this.isFromPokeStaff=isFromPokeStaff;
    }

    public SendMoveSlotPacket(int slot, int moveSlot) {
        this.slot = slot;
        this.moveSlot = moveSlot;
        this.isFromPokeStaff=false;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, SendMoveSlotPacket::getSlot,
                ByteBufCodecs.INT, SendMoveSlotPacket::getMoveSlot,
                SendMoveSlotPacket::new);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
