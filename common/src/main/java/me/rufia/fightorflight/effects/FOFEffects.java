package me.rufia.fightorflight.effects;

import dev.architectury.registry.registries.DeferredRegister;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public interface FOFEffects {
    Holder<MobEffect> RESISTANCE_WEAKENED = register("resistance_weakened", new FOFStatusEffect(MobEffectCategory.HARMFUL, 0xB40C0C));
    static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(CobblemonFightOrFlight.MODID, name), effect);
    }
    static void bootstrap(){
    }
}
