package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CaughtByTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private LivingEntity lastCaughtByMob;
    private int lastCaughtByMobTimestamp;

    public CaughtByTargetGoal(Mob mob) {
        super(mob, true, false);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        PokemonEntity pokemonEntity = (PokemonEntity) this.mob;
        //I guess AI is disabled if the pokemon entity has busy locks
        /*
        List<Object> busyLocks = pokemonEntity.getBusyLocks();
        for (Object busyLock : busyLocks) {
            CobblemonFightOrFlight.LOGGER.info("locked");
            if (busyLock instanceof EmptyPokeBallEntity pokeBallEntity) {
                CobblemonFightOrFlight.LOGGER.info("Pokemon in process of being caught");

                if (pokeBallEntity.getOwner() instanceof LivingEntity livingEntity) {
                    lastCaughtByMob = livingEntity;
                    lastCaughtByMobTimestamp = this.mob.tickCount;
                }
            }
        }
*/
        int mobID = ((PokemonInterface) pokemonEntity).getCapturedBy();
        if (mobID != 0) {
            Entity target = mob.level().getEntity(mobID);
            if (target instanceof LivingEntity livingEntity) {
                lastCaughtByMob = livingEntity;
                CobblemonFightOrFlight.LOGGER.info("Converted");
            } else {
                CobblemonFightOrFlight.LOGGER.info("Failed to convert");
            }
        }
        if (lastCaughtByMob != null) {
            if (lastCaughtByMob.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                return this.canAttack(lastCaughtByMob, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(lastCaughtByMob);
        this.targetMob = this.mob.getTarget();
        this.mob.setLastHurtByMob(this.mob.getTarget());
        if (this.mob.getTarget() instanceof Player) {
            this.mob.setLastHurtByPlayer((Player) this.mob.getTarget());
        }
//        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
//        if (this.alertSameType) {
//            this.alertOthers();
//        }

        super.start();
    }
}
