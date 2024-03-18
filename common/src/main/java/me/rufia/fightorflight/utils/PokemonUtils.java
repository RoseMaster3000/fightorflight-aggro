package me.rufia.fightorflight.utils;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class PokemonUtils {
    public static boolean shouldMelee(PokemonEntity pokemonEntity) {
        Move move = getMove(pokemonEntity, false);
        boolean b1 = pokemonEntity.getPokemon().getAttack() > pokemonEntity.getPokemon().getSpecialAttack();//The default setting.
        boolean b2 = pokemonEntity.getOwner() == null;//The pokemon has a trainer.
        boolean b3 = move != null && move.getDamageCategory() == DamageCategories.INSTANCE.getPHYSICAL();//The trainer selected a physical move.
        if (b2) {
            return b1 || !CobblemonFightOrFlight.commonConfig().wild_pokemon_ranged_attack;//wild pokemon choose the strongest way to attack
        } else {
            return b3;
        }
    }

    public static Move getMove(PokemonEntity pokemonEntity) {
        String moveName = !(((PokemonInterface) (Object) pokemonEntity).getCurrentMove() == null) ? (((PokemonInterface) (Object) pokemonEntity).getCurrentMove()) : pokemonEntity.getPokemon().getMoveSet().get(0).getName();
        Move move = null;
        boolean flag = false;
        for (Move m : pokemonEntity.getPokemon().getMoveSet().getMoves()) {
            move = m;
            if (m.getName().equals(moveName)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            move = pokemonEntity.getPokemon().getMoveSet().get(0);
        }
        if (move == null) {
            CobblemonFightOrFlight.LOGGER.info("Returning a null move for no reason");
            return null;
        }
        return move;
    }

    public static Move getMove(PokemonEntity pokemonEntity, boolean getSpecial) {
        Move move = getMove(pokemonEntity);
        if (move == null) {
            return null;
        }
        boolean isSpecial = move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL();
        boolean isPhysical = move.getDamageCategory() == DamageCategories.INSTANCE.getPHYSICAL();

        if ((isSpecial && getSpecial) || (isPhysical && !getSpecial)) {
            ((PokemonInterface) (Object) pokemonEntity).setCurrentMove(move);
            return move;
        }
        return null;
    }

    public static void makeParticle(int particleAmount, Entity entity, SimpleParticleType particleType) {
        Level level = entity.level();
        if (particleAmount > 0) {
            double d = 0;
            double e = 0;
            double f = 0;
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(particleType, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), particleAmount, d, e, f, 1f);
            } else {
                for (int j = 0; j < particleAmount; ++j) {
                    level.addParticle(particleType, entity.getRandomX(0.5), entity.getRandomY(), entity.getRandomZ(0.5), d, e, f);
                }
            }
        }
    }

    public static void setHurtByPlayer(PokemonEntity pokemonEntity, Entity target) {
        Entity owner = pokemonEntity.getOwner();
        if (owner instanceof Player player) {
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.setLastHurtByPlayer(player);
                CobblemonFightOrFlight.LOGGER.info("Hurt by player attack");
            }
        }
    }

    public static boolean isExplosiveMove(String moveName) {
        return Arrays.stream(CobblemonFightOrFlight.moveConfig().explosive_moves).toList().contains(moveName);
    }
}
