package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.Objects;

//Some status moves are designed to be used passively,which means you just need to have it in the pokemon's moveset
public class PokemonPassiveAbilityGoal extends Goal {
    protected PokemonEntity pokemonEntity;
    protected float radius;

    public PokemonPassiveAbilityGoal(PokemonEntity entity) {
        this.pokemonEntity = entity;
        this.radius = CobblemonFightOrFlight.moveConfig().status_move_radius;
    }

    public boolean canUse() {
        //return PokemonUtils.canTaunt(pokemonEntity);
        return false;
    }

    public void tick() {
        /*
        if (PokemonUtils.canTaunt(pokemonEntity)) {
            for (LivingEntity livingEntity : pokemonEntity.level().getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pokemonEntity.position(), radius, radius, radius), (livingEntity -> {
                if (livingEntity instanceof PokemonEntity pokemonEntity2) {
                    return Objects.equals(pokemonEntity.getOwner(), pokemonEntity2.getOwner());
                }
                return false;
            }))) {
                if (livingEntity instanceof PokemonEntity pokemonEntity2) {
                    if (Objects.equals(pokemonEntity2.getTarget(), pokemonEntity.getOwner())) {
                        pokemonEntity2.setTarget(pokemonEntity);
                        pokemonEntity2.setLastHurtByMob(pokemonEntity);
                        pokemonEntity.setTarget(pokemonEntity);
                        CobblemonFightOrFlight.LOGGER.info("TARGET CHANGED");
                    }
                }
            }
        }
        */


    }
}
