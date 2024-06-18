package me.rufia.fightorflight;

import me.rufia.fightorflight.client.keybinds.KeybindFightOrFlight;
import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.client.renderer.PokemonArrowRenderer;
import me.rufia.fightorflight.client.renderer.PokemonBulletRenderer;
import me.rufia.fightorflight.client.renderer.PokemonTracingBulletRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityFightOrFlight.TRACING_BULLET.get(), PokemonTracingBulletRenderer::new);
        EntityRendererRegistry.register(EntityFightOrFlight.ARROW_PROJECTILE.get(), PokemonArrowRenderer::new);
        EntityRendererRegistry.register(EntityFightOrFlight.BULLET.get(), PokemonBulletRenderer::new);
        for(KeyMapping keyMapping : KeybindFightOrFlight.bindings ){
            KeyBindingHelper.registerKeyBinding(keyMapping);
        }
    }
}
