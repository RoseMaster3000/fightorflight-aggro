package me.rufia.fightorflight;

import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.entity.PokemonArrowRenderer;
import me.rufia.fightorflight.entity.PokemonBulletRenderer;
import me.rufia.fightorflight.entity.PokemonTracingBulletRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityFightOrFlight.TRACING_BULLET.get(), PokemonTracingBulletRenderer::new);
        EntityRendererRegistry.register(EntityFightOrFlight.ARROW_PROJECTILE.get(), PokemonArrowRenderer::new);
        EntityRendererRegistry.register(EntityFightOrFlight.BULLET.get(), PokemonBulletRenderer::new);
    }
}
