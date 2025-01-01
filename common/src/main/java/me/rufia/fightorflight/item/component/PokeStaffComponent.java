package me.rufia.fightorflight.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record PokeStaffComponent(String mode, int moveSlot, String cmdmode) {
    public enum MODE {
        SEND, SETMOVE, SETCMDMODE
    }

    public enum CMDMODE {
        MOVE_ATTACK, MOVE, STAY, ATTACK, ATTACK_POSITION, NOCMD, CLEAR
    }

    public static final Codec<PokeStaffComponent> CODEC;
    public static final StreamCodec<ByteBuf, PokeStaffComponent> STREAM_CODEC;

    public void setMode(String val, ItemStack itemStack) {
        itemStack.set(ItemComponentFOF.POKE_STAFF_COMMAND_MODE_COMPONENT, new PokeStaffComponent(val, moveSlot, cmdmode));
    }

    public void setMoveSlot(int val, ItemStack itemStack) {
        itemStack.set(ItemComponentFOF.POKE_STAFF_COMMAND_MODE_COMPONENT, new PokeStaffComponent(mode, val, cmdmode));
    }

    public void setCmdmode(String val, ItemStack itemStack) {
        itemStack.set(ItemComponentFOF.POKE_STAFF_COMMAND_MODE_COMPONENT, new PokeStaffComponent(mode, moveSlot, val));
    }

    public static String getNextMode(String val) {
        String result;
        switch (PokeStaffComponent.MODE.valueOf(val)) {
            case SETMOVE -> result = MODE.SETCMDMODE.name();
            case SEND -> result = MODE.SETMOVE.name();
            case SETCMDMODE -> result = MODE.SEND.name();
            default -> result = MODE.SETCMDMODE.name();
        }
        return result;
    }

    static {
        CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                                Codec.STRING.optionalFieldOf("mode", MODE.SETMOVE.name()).forGetter(PokeStaffComponent::mode),
                                Codec.INT.optionalFieldOf("moveSlot", 0).forGetter(PokeStaffComponent::moveSlot),
                                Codec.STRING.optionalFieldOf("command", CMDMODE.NOCMD.name()).forGetter(PokeStaffComponent::cmdmode)
                        )
                        .apply(instance, PokeStaffComponent::new));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, PokeStaffComponent::mode,
                ByteBufCodecs.INT, PokeStaffComponent::moveSlot,
                ByteBufCodecs.STRING_UTF8, PokeStaffComponent::cmdmode,
                PokeStaffComponent::new);
    }
}
