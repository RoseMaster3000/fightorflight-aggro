package me.rufia.fightorflight.forge;

import net.minecraftforge.common.MinecraftForge;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
