package me.rufia.fightorflight.item.component;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ItemComponentFOF {
    public static final DataComponentType<PokeStaffComponent> POKE_STAFF_COMMAND_MODE_COMPONENT = register("poke_staff_setting", (builder) -> builder.persistent(PokeStaffComponent.CODEC).networkSynchronized(PokeStaffComponent.STREAM_CODEC));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, name), builder.apply(DataComponentType.builder()).build());
    }
}
