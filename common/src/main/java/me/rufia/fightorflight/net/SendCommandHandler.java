package me.rufia.fightorflight.net;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.activestate.ActivePokemonState;
import com.cobblemon.mod.common.pokemon.activestate.PokemonState;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import dev.architectury.networking.NetworkManager;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.item.ItemFightOrFlight;
import me.rufia.fightorflight.item.PokeStaff;
import me.rufia.fightorflight.item.component.PokeStaffComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SendCommandHandler implements NetworkPacketHandler<SendCommandPacket> {
    @Override
    public void handle(SendCommandPacket packet, NetworkManager.PacketContext context) {
        CobblemonFightOrFlight.LOGGER.info("Handling packet");
        Player player = context.getPlayer();
        int slot = packet.slot;
        if (player instanceof ServerPlayer serverPlayer) {
            Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer).get(slot);
            if (pokemon != null) {
                PokemonState state = pokemon.getState();
                if (state instanceof ShoulderedState || !(state instanceof ActivePokemonState activePokemonState)) {
                    //nothing to do
                } else {
                    PokemonEntity pokemonEntity = activePokemonState.getEntity();
                    if (pokemonEntity != null) {
                        ItemStack stack = getStack(player);
                        if (stack == null) {
                            return;
                        }
                        //CobblemonFightOrFlight.LOGGER.info("ITEM ACQUIRED");
                        String cmdData = packet.getCommandData();
                        String cmdMode = packet.command;
                        CobblemonFightOrFlight.LOGGER.info("MODE:%s DATA:%s".formatted(cmdMode, cmdData));
                        ((PokemonInterface) pokemonEntity).setCommand(cmdMode);
                        ((PokemonInterface) pokemonEntity).setCommandData(cmdData);
                    }
                }
            }
        }
    }

    private ItemStack getStack(Player player) {
        if (player.getMainHandItem().is(ItemFightOrFlight.POKESTAFF.get())) {
            return player.getMainHandItem();
        } else if (player.getOffhandItem().is(ItemFightOrFlight.POKESTAFF.get())) {
            return player.getOffhandItem();
        } else {
            return null;
        }
    }
}
