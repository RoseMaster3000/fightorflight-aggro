package me.rufia.fightorflight.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.DyedItemColor;

public record PokeStaffCommandModeComponent(String mode, String cmdmode) {
    enum MODE {
        SEND, SETMOVE, SETCMDMODE
    }

    public enum CMDMODE {
        MOVE_ATTACK, MOVE, STAY, ATTACK, ATTACK_POSITION, NOCMD, CLEAR
    }

    public static final Codec<PokeStaffCommandModeComponent> CODEC;
    public static final StreamCodec<ByteBuf, PokeStaffCommandModeComponent> STREAM_CODEC;

    public String getCommandMode() {
        return cmdmode;
    }

    public String getMode() {
        return mode;
    }

    static {
        CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                                Codec.STRING.optionalFieldOf("command", CMDMODE.NOCMD.name()).forGetter(PokeStaffCommandModeComponent::getCommandMode),
                                Codec.STRING.optionalFieldOf("mode", MODE.SETMOVE.name()).forGetter(PokeStaffCommandModeComponent::getMode)
                        )
                        .apply(instance, PokeStaffCommandModeComponent::new));
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, PokeStaffCommandModeComponent::cmdmode,
                ByteBufCodecs.STRING_UTF8, PokeStaffCommandModeComponent::mode,
                PokeStaffCommandModeComponent::new);

    }
}
