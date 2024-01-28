package me.rufia.fightorflight;

import me.rufia.fightorflight.entity.EntityFightOrFlight;
import me.rufia.fightorflight.entity.PokemonTracingBullet;
import me.rufia.fightorflight.event.EntityLoadHandler;
import me.rufia.fightorflight.mixin.MobEntityAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class CobblemonFightOrFlightFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		CobblemonFightOrFlight.LOGGER.info("Hello Fabric world from Fight or Flight!");
		EntityFightOrFlight.bootstrap();
		CobblemonFightOrFlight.init((pokemonEntity, priority, goal) -> ((MobEntityAccessor) (Object) pokemonEntity).goalSelector().addGoal(priority, goal));

		ServerEntityEvents.ENTITY_LOAD.register(new EntityLoadHandler());
	}
}