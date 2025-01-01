package me.rufia.fightorflight.forge;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

//@Mod.EventBusSubscriber(modid = CobblemonFightOrFlight.MODID,bus= Mod.EventBusSubscriber.Bus.MOD)
public class ForgeBusEvent {
    @SubscribeEvent
    public static void onEntityJoined(EntityJoinLevelEvent event) {
        //LOGGER.info("onEntityJoined");

        if (event.getEntity() instanceof PokemonEntity pokemonEntity) {
            CobblemonFightOrFlight.addPokemonGoal(pokemonEntity);
        }
    }
}
