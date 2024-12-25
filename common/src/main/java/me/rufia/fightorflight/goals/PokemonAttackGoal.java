package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class PokemonAttackGoal extends Goal {
    protected PokemonEntity getPokemonEntity() {
        return null;
    }

    protected void setAttackTime(int i) {
        PokemonEntity pokemonEntity = getPokemonEntity();
        if (pokemonEntity == null) {
            return;
        }
        ((PokemonInterface) (Object) pokemonEntity).setAttackTime(i);
    }

    protected int getAttackTime() {
        PokemonEntity pokemonEntity = getPokemonEntity();
        if (pokemonEntity == null) {
            return -1;
        }
        return ((PokemonInterface) (Object) pokemonEntity).getAttackTime();
    }

    protected void resetAttackTime(double d) {
        PokemonEntity pokemonEntity = getPokemonEntity();
        float attackSpeedModifier = Math.max(0.1f, 1 - pokemonEntity.getSpeed() / CobblemonFightOrFlight.commonConfig().speed_stat_limit);
        float f = (float) Math.sqrt(d) / PokemonUtils.getAttackRadius() * attackSpeedModifier;
        //this.attackTime = Mth.floor(20 * Mth.lerp(f, CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_interval, CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_interval));
        setAttackTime(Mth.floor(20 * Mth.lerp(f, CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_interval, CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_interval)));
    }
}
