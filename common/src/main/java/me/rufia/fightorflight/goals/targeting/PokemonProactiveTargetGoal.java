package me.rufia.fightorflight.goals.targeting;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.utils.TargetingWhitelist;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PokemonProactiveTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public float safeDistanceSqr = 36;
    //public PokemonProactiveTargetGoal(Mob mob, Class<T> targetType, float safeDistanceSqr, boolean mustSee, boolean mustReach) {
    //    super(mob, targetType, mustSee, mustReach);
    //    this.safeDistanceSqr = safeDistanceSqr;
    //}

    public PokemonProactiveTargetGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, targetType, randomInterval, mustSee, mustReach, targetPredicate);
        this.safeDistanceSqr = safeDistanceSqr;
    }

    public boolean canUse() {
        if (!CobblemonFightOrFlight.commonConfig().do_pokemon_defend_proactive) {
            return false;
        }
        PokemonEntity pokemonEntity = (PokemonEntity) this.mob;
        if (pokemonEntity.getPokemon().getState() instanceof ShoulderedState) {
            return false;
        }
        if (!pokemonEntity.getPokemon().isPlayerOwned()) {
            return false;
        }

        return super.canUse();
    }

    protected void findTarget() {
        super.findTarget();
        if (this.target != null) {
            if (this.target.distanceToSqr(this.mob) > safeDistanceSqr) {
                this.target = null;
            } else if (TargetingWhitelist.getWhitelist((PokemonEntity) this.mob).contains(target.getEncodeId())) {
                this.target = null;
            }
        }
    }
}
