package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.item.PokeStaff;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokemonGoToPosGoal extends Goal {
    private final PokemonEntity pokemonEntity;
    private final double speedModifier;
    private BlockPos blockPos;

    PokemonGoToPosGoal(PokemonEntity entity, double speedModifier) {
        pokemonEntity = entity;
        this.speedModifier = speedModifier;
    }

    protected boolean checkCommand() {
        return PokemonUtils.moveCommandAvailable(pokemonEntity)
                || PokemonUtils.moveAttackCommandAvailable(pokemonEntity)
                || PokemonUtils.stayCommandAvailable(pokemonEntity);
    }

    public boolean canUse() {
        return checkCommand();
    }

    public void tick() {
        String data = ((PokemonInterface) (Object) pokemonEntity).getCommandData();
        if (data.startsWith("VEC3_")) {
            Pattern p = Pattern.compile("VEC3_([-\\d]*)_([-\\d]*)_([-\\d]*)");//I know it's not safe, but who will send other data?
            Matcher m = p.matcher(data);
            if (m.find()) {
                try {
                    int x = Integer.parseInt(m.group(1));
                    int y = Integer.parseInt(m.group(2));
                    int z = Integer.parseInt(m.group(3));
                    BlockPos targetBlockPos = new BlockPos(x, y, z);
                } catch (NumberFormatException e) {
                    CobblemonFightOrFlight.LOGGER.info("Failed to get the target position");
                }
            }
        }
    }


    public void stop() {
        ((PokemonInterface) (Object) pokemonEntity).setCommand(PokeStaff.CMDMODE.NOCMD.name());
    }
}
