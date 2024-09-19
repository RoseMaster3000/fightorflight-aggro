package me.rufia.fightorflight.entity;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.api.pokemon.evolution.progress.EvolutionProgress;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.evolution.progress.UseMoveEvolutionProgress;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.mixin.PokemonEntityMixin;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.Arrays;

public class PokemonAttackEffect {
    public static SimpleParticleType getParticleFromType(String name) {
        return switch (name) {
            case "fire" -> ParticleTypes.FLAME;
            case "ice" -> ParticleTypes.SNOWFLAKE;
            case "poison" -> ParticleTypes.MYCELIUM;
            case "psychic" -> ParticleTypes.PORTAL;
            case "fairy" -> ParticleTypes.CHERRY_LEAVES;
            case "fighting", "ground", "rock" -> ParticleTypes.POOF;
            case "steel" -> ParticleTypes.WAX_OFF;
            case "ghost" -> ParticleTypes.SOUL;
            case "dark" -> ParticleTypes.SMOKE;
            case "electric" -> ParticleTypes.ELECTRIC_SPARK;
            case "bug" -> ParticleTypes.SPORE_BLOSSOM_AIR;
            case "grass" -> ParticleTypes.COMPOSTER;
            case "dragon" -> ParticleTypes.DRAGON_BREATH;
            case "flying" -> ParticleTypes.SWEEP_ATTACK;
            case "water" -> ParticleTypes.SPLASH;
            case "normal" -> ParticleTypes.CRIT;
            default -> ParticleTypes.CRIT;
        };
    }

    public static Color getColorFromType(String typeName) {
        return switch (typeName) {
            case "fire" -> new Color(230, 40, 41);
            case "ice" -> new Color(63, 216, 255);
            case "poison" -> new Color(145, 65, 203);
            case "psychic" -> new Color(239, 65, 121);
            case "fairy" -> new Color(239, 112, 239);
            case "fighting" -> new Color(255, 128, 0);
            case "steel" -> new Color(96, 161, 184);
            case "ghost" -> new Color(112, 65, 112);
            case "dark" -> new Color(80, 65, 63);
            case "ground" -> new Color(145, 81, 33);
            case "rock" -> new Color(175, 169, 129);
            case "electric" -> new Color(250, 192, 0);
            case "bug" -> new Color(145, 161, 25);
            case "grass" -> new Color(63, 161, 41);
            case "dragon" -> new Color(80, 96, 225);
            case "flying" -> new Color(129, 185, 239);
            case "water" -> new Color(41, 128, 239);
            case "normal" -> new Color(159, 161, 159);
            default -> new Color(68, 104, 94);
        };
    }

    public static SimpleParticleType getParticleFromType(ElementalType type) {
        if (type == null) {
            return ParticleTypes.CRIT;
        }
        return switch (type.getName()) {
            case "fire" -> ParticleTypes.FLAME;
            case "ice" -> ParticleTypes.SNOWFLAKE;
            case "poison" -> ParticleTypes.MYCELIUM;
            case "psychic" -> ParticleTypes.PORTAL;
            case "fairy" -> ParticleTypes.CHERRY_LEAVES;
            case "fighting", "ground", "rock" -> ParticleTypes.POOF;
            case "steel" -> ParticleTypes.WAX_OFF;
            case "ghost" -> ParticleTypes.SOUL;
            case "dark" -> ParticleTypes.SMOKE;
            case "electric" -> ParticleTypes.ELECTRIC_SPARK;
            case "bug" -> ParticleTypes.SPORE_BLOSSOM_AIR;
            case "grass" -> ParticleTypes.COMPOSTER;
            case "dragon" -> ParticleTypes.DRAGON_BREATH;
            case "flying" -> ParticleTypes.SWEEP_ATTACK;
            case "water" -> ParticleTypes.SPLASH;
            case "normal" -> ParticleTypes.CRIT;
            default -> ParticleTypes.CRIT;
        };
    }

    public static Color getColorFromType(ElementalType type) {
        return getColorFromType(type.getName());
    }

    public static Color getColorFromType(Pokemon pokemon) {
        return getColorFromType(pokemon.getPrimaryType());
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, boolean isSpecial) {
        return calculatePokemonDamage(pokemonEntity, isSpecial, CobblemonFightOrFlight.moveConfig().base_power);
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, boolean isSpecial, float movePower) {
        float attack = isSpecial ? pokemonEntity.getPokemon().getSpecialAttack() : pokemonEntity.getPokemon().getAttack();
        attack = Math.min(attack, 255.0f) / 255.0f;
        float moveModifier = isSpecial ? movePower / 100 * CobblemonFightOrFlight.moveConfig().move_power_multiplier : 1;
        float minDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().minimum_attack_damage;
        float maxDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().maximum_attack_damage;
        float multiplier = 1f;

        if (((PokemonInterface) (Object) pokemonEntity).usingBeam()) {
            multiplier *= CobblemonFightOrFlight.moveConfig().beam_move_power_multiplier;
        }
        return Math.min(Mth.lerp(attack * moveModifier * multiplier, minDmg, maxDmg), maxDmg);
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, Move move) {
        //TODO Special effect for Photon Geyser
        boolean isSpecial = DamageCategories.INSTANCE.getSPECIAL().equals(move.getDamageCategory());
        float STAB;
        var primaryType = pokemonEntity.getPokemon().getPrimaryType();
        var secondaryType = pokemonEntity.getPokemon().getSecondaryType();
        if (secondaryType == null) {
            secondaryType = primaryType;
        }
        if (primaryType.equals(move.getType()) || secondaryType.equals(move.getType())) {
            STAB = 1.5f;
        } else {
            STAB = 1.0f;
        }
        return calculatePokemonDamage(pokemonEntity,isSpecial, (float) (move.getPower()*STAB));
    }

    protected static void calculateTypeEffect(PokemonEntity pokemonEntity, Entity hurtTarget, String typeName, int pkmLevel) {
        if (hurtTarget instanceof
                LivingEntity livingHurtTarget) {
            int effectStrength = Math.max(pkmLevel / 10, 1);

            switch (typeName) {
                case "fire":
                    livingHurtTarget.setSecondsOnFire(effectStrength);
                    break;
                case "ice":
                    livingHurtTarget.setTicksFrozen(livingHurtTarget.getTicksFrozen() + effectStrength * 30);
                    break;
                case "poison":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.POISON, effectStrength * 20, 0), pokemonEntity);
                    break;
                case "psychic":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.LEVITATION, effectStrength * 20, 0), pokemonEntity);
                    break;
                case "fairy":
                case "fighting":
                case "steel":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, effectStrength * 20, 0), pokemonEntity);
                    break;
                case "ghost":
                case "dark":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, (effectStrength + 2) * 25, 0), pokemonEntity);
                    break;
                case "ground":
                case "rock":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, (effectStrength + 2) * 25, 0), pokemonEntity);
                    break;
                case "electric":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (effectStrength + 2) * 25, 0), pokemonEntity);
                    break;
                case "bug":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.HUNGER, (effectStrength + 2) * 25, 0), pokemonEntity);
                    break;
                case "grass":
                    pokemonEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (effectStrength + 2) * 20, 0), pokemonEntity);
                    break;
                case "dragon":
                    break;
                case "flying":
                    break;
                case "water":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (effectStrength + 2) * 25, 0), pokemonEntity);
                    break;
                default:
                    break;
            }
        }
    }

    public static void applyTypeEffect(PokemonEntity pokemonEntity, Entity hurtTarget, String typeName) {
        if (pokemonEntity == null) {
            return;
        }
        Pokemon pokemon = pokemonEntity.getPokemon();
        int pkmLevel = pokemon.getLevel();

        calculateTypeEffect(pokemonEntity, hurtTarget, typeName, pkmLevel);
    }

    public static void applyTypeEffect(PokemonEntity pokemonEntity, Entity hurtTarget) {
        if (pokemonEntity == null) {
            return;
        }
        Pokemon pokemon = pokemonEntity.getPokemon();
        int pkmLevel = pokemon.getLevel();
        String primaryType = pokemon.getPrimaryType().getName();
        calculateTypeEffect(pokemonEntity, hurtTarget, primaryType, pkmLevel);
    }

    public static void applyOnHitEffect(PokemonEntity pokemonEntity, Entity hurtTarget, Move move) {
        if (move == null) {
            return;
        }
        int particleAmount = 4;
        boolean b1 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().self_angry_moves).toList().contains(move.getName());
        boolean b2 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().target_soul_fire_moves).toList().contains(move.getName());
        boolean b3 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().target_soul_moves).toList().contains(move.getName());
        boolean b4 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().slicing_moves).toList().contains(move.getName());
        if (b1) {
            PokemonUtils.makeParticle(particleAmount, pokemonEntity, ParticleTypes.ANGRY_VILLAGER);
        }
        if (b2) {
            PokemonUtils.makeParticle(particleAmount, hurtTarget, ParticleTypes.SOUL_FIRE_FLAME);
        }
        if (b3) {
            PokemonUtils.makeParticle(particleAmount, hurtTarget, ParticleTypes.SOUL);
        }
        if (b4) {
            PokemonUtils.makeParticle(particleAmount, hurtTarget, ParticleTypes.SWEEP_ATTACK);
        }
    }

    public static void makeTypeEffectParticle(int particleAmount, Entity entity, String typeName) {
        if (typeName == null) {
            return;
        }
        PokemonUtils.makeParticle(particleAmount, entity, getParticleFromType(typeName));
    }

    public static void applyPostEffect(PokemonEntity pokemonEntity, Entity hurtTarget, Move move) {
        Level level = hurtTarget.level();
        if (move == null) {
            return;
        }
        boolean b1 = Arrays.stream(CobblemonFightOrFlight.moveConfig().switch_moves).toList().contains(move.getName());
        boolean b2 = Arrays.stream(CobblemonFightOrFlight.moveConfig().explosive_moves).toList().contains(move.getName());
        boolean b3 = Arrays.stream(CobblemonFightOrFlight.moveConfig().recoil_moves_allHP).toList().contains(move.getName());
        if (b1) {
            if (pokemonEntity.getOwner() != null) {
                pokemonEntity.recallWithAnimation();
            }
        }
        if (b2) {
            pokemonExplode(pokemonEntity, level, move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL());
        }
        if (b3) {
            pokemonRecoilSelf(pokemonEntity, 1.0f);
        }
    }

    public static void pokemonExplode(PokemonEntity entity, Level level, boolean isSpecial) {
        if (!level.isClientSide) {
            Pokemon pokemon = entity.getPokemon();
            int power = isSpecial ? pokemon.getSpecialAttack() : pokemon.getAttack();
            level.explode(entity, level.damageSources().mobAttack(entity), null, entity.getX(), entity.getY(), entity.getZ(), getAoERadius(power), false, Level.ExplosionInteraction.MOB);
            //entity.die(level.damageSources().generic()); //well,it doesn't work
        }
    }

    public static void pokemonRecoilSelf(PokemonEntity pokemonEntity, float percent) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        float curHealth = pokemonEntity.getHealth();
        float maxHealth = pokemonEntity.getMaxHealth();
        float health = curHealth - maxHealth * percent;
        if (health > 0) {
            pokemonEntity.setHealth(curHealth);
        } else {
            pokemonEntity.setHealth(0);
        }

        if (pokemonEntity.getHealth() == 0f) {
            pokemon.setCurrentHealth(0);
        }
    }

    public static float getAoERadius(float power) {
        float min = CobblemonFightOrFlight.moveConfig().min_AoE_radius;
        float max = CobblemonFightOrFlight.moveConfig().max_AoE_radius;
        return Math.min(Mth.lerp(power / 180, min, max), max);

    }

    public static boolean pokemonAttack(PokemonEntity pokemonEntity, Entity hurtTarget) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        float hurtDamage;
        float hurtKnockback = 1f;
        Move move = PokemonUtils.getMove(pokemonEntity, false);

        if (move != null) {
            boolean b1 = PokemonUtils.isExplosiveMove(move.getName());
            if (b1) {
                hurtDamage = 0f;
            } else {
                //CobblemonFightOrFlight.LOGGER.info(move.getType().getName());
                applyTypeEffect(pokemonEntity, hurtTarget, move.getType().getName());
                makeTypeEffectParticle(10, hurtTarget, move.getType().getName());
                hurtDamage = calculatePokemonDamage(pokemonEntity, false, (float) move.getPower());
            }
        } else {
            applyTypeEffect(pokemonEntity, hurtTarget);
            makeTypeEffectParticle(6, hurtTarget, pokemonEntity.getPokemon().getPrimaryType().getName());
            hurtDamage = calculatePokemonDamage(pokemonEntity, false);
        }
        applyOnHitEffect(pokemonEntity, hurtTarget, move);
        PokemonUtils.setHurtByPlayer(pokemonEntity, hurtTarget);
        PokemonUtils.updateMoveEvolutionProgress(pokemon, move.getTemplate());
        boolean flag = hurtTarget.hurt(((Mob) pokemonEntity).level().damageSources().mobAttack(pokemonEntity), hurtDamage);
        if (flag) {
            if (hurtTarget instanceof LivingEntity) {
                ((LivingEntity) hurtTarget).knockback(hurtKnockback * 0.5F, Mth.sin(pokemonEntity.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(pokemonEntity.getYRot() * ((float) Math.PI / 180F)));
                pokemonEntity.setDeltaMovement(pokemonEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            pokemonEntity.setLastHurtMob(hurtTarget);
        }
        applyPostEffect(pokemonEntity, hurtTarget, move);
        return flag;
    }
}
