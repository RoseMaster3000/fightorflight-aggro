package me.rufia.fightorflight.forge;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.client.keybinds.KeybindFightOrFlight;
import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.client.renderer.PokemonArrowRenderer;
import me.rufia.fightorflight.client.renderer.PokemonBulletRenderer;
import me.rufia.fightorflight.client.renderer.PokemonTracingBulletRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CobblemonFightOrFlight.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FOFForgeClient {
    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityFightOrFlight.TRACING_BULLET.get(), PokemonTracingBulletRenderer::new);
        EntityRenderers.register(EntityFightOrFlight.ARROW_PROJECTILE.get(), PokemonArrowRenderer::new);
        EntityRenderers.register(EntityFightOrFlight.BULLET.get(), PokemonBulletRenderer::new);
    }
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        KeybindFightOrFlight.bindings.forEach(event::register);
    }
}