package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.world.entity.ai.goal.Goal;

public class PokemonAttackPosGoal extends Goal {
    private final PokemonEntity pokemonEntity;

    public PokemonAttackPosGoal(PokemonEntity pokemonEntity) {
        this.pokemonEntity = pokemonEntity;
    }

    @Override
    public boolean canUse() {
        return PokemonUtils.attackPositionAvailable(pokemonEntity) && PokemonUtils.shouldShoot(pokemonEntity);
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {

    }

}
