package me.rufia.fightorflight.effects;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public interface FOFEffects {
    DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(CobblemonFightOrFlight.MODID, Registries.MOB_EFFECT);
    RegistrySupplier<MobEffect> RESISTANCE_WEAKENED = register("resistance_weakened", () -> new FOFStatusEffect(MobEffectCategory.HARMFUL, 0xB40C0C));

    static RegistrySupplier<MobEffect> register(String name, Supplier<MobEffect> effect) {
        return EFFECTS.register(ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, name), effect);
    }

    static void bootstrap() {
        EFFECTS.register();
    }
}
