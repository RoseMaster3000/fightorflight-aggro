package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;


public class PokemonNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public int ticksUntilNewAngerParticle = 0;
    public float safeDistanceSqr = 36;

    public PokemonNearestAttackableTargetGoal(Mob mob, Class<T> targetType, float safeDistanceSqr, boolean mustSee, boolean mustReach) {
        super(mob, targetType, mustSee, mustReach);
        this.safeDistanceSqr = safeDistanceSqr;
    }

    public boolean canUse() {
        PokemonEntity pokemonEntity = (PokemonEntity) this.mob;
        if(!PokemonUtils.WildPokemonCanPerformUnprovokedAttack(pokemonEntity)){
            return false;
        }
        if (CobblemonFightOrFlight.getFightOrFlightCoefficient(pokemonEntity) <= CobblemonFightOrFlight.AUTO_AGGRO_THRESHOLD) {
            return false;
        } else {
            if (ticksUntilNewAngerParticle < 1) {
                CobblemonFightOrFlight.PokemonEmoteAngry(this.mob);
                ticksUntilNewAngerParticle = 25;
            } else {
                ticksUntilNewAngerParticle = ticksUntilNewAngerParticle - 1;
            }
        }

        return super.canUse();
    }

    protected void findTarget() {
        super.findTarget();
        if (this.target != null && this.target.distanceToSqr(this.mob) > safeDistanceSqr) {
            this.target = null;
        }
    }
}
