package me.rufia.fightorflight;

import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.entity.PokemonTracingBulletRenderer;

import net.minecraft.client.renderer.entity.EntityRenderers;

public class CobblemonFightOrFlightClient  {

    public void onInitializeClient() {
        EntityRenderers.register(EntityFightOrFlight.TRACING_BULLET.get(), PokemonTracingBulletRenderer::new);



    }

}
