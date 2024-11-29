package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.item.PokeStaff;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokemonGoToPosGoal extends Goal {
    private final PokemonEntity pokemonEntity;
    private final double speedModifier;
    private boolean stuck;

    public PokemonGoToPosGoal(PokemonEntity entity, double speedModifier) {
        pokemonEntity = entity;
        this.speedModifier = speedModifier;

    }

    protected boolean checkCommand() {
        return PokemonUtils.moveCommandAvailable(pokemonEntity)
                || PokemonUtils.moveAttackCommandAvailable(pokemonEntity)
                || PokemonUtils.stayCommandAvailable(pokemonEntity);
    }

    public boolean canUse() {
        return checkCommand() && !isCloseEnough();
    }

    public boolean canContinueToUse() {
        return checkCommand() && !isCloseEnough();
    }

    public void start() {
        stuck = false;
    }


    public void tick() {
        //BlockPos
        BlockPos blockPos = getBlockPos();
        if (blockPos != BlockPos.ZERO) {
            //CobblemonFightOrFlight.LOGGER.info("Pathfinding");
            if (pokemonEntity.getNavigation().isDone()) {
                Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
                Vec3 vec32 = DefaultRandomPos.getPosTowards(pokemonEntity, 16, 3, vec3, 0.3141592741012573);
                if (vec32 == null) {
                    vec32 = DefaultRandomPos.getPosTowards(pokemonEntity, 8, 7, vec3, 1.5707963705062866);
                }

                if (vec32 != null) {
                    int i = Mth.floor(vec32.x);
                    int j = Mth.floor(vec32.z);
                    if (!((LivingEntity) pokemonEntity).level().hasChunksAt(i - 34, j - 34, i + 34, j + 34)) {
                        vec32 = null;
                    }
                }

                if (vec32 == null) {
                    this.stuck = true;
                    return;
                }

                pokemonEntity.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
            }
        }
    }

    public void stop() {
        PokemonUtils.clearCommand(pokemonEntity);
    }

    protected BlockPos getBlockPos() {
        return ((PokemonInterface) (Object) pokemonEntity).getTargetBlockPos();
    }

    protected boolean isCloseEnough() {
        return getBlockPos().closerToCenterThan(pokemonEntity.position(), 2);
    }
}
