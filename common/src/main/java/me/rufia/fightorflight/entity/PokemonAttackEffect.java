package me.rufia.fightorflight.entity;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.awt.*;

public class PokemonAttackEffect {
    private static boolean shouldUseMeleeAttack(Pokemon pokemon) {
        return pokemon.getAttack() > pokemon.getSpecialAttack();
    }

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
        return calculatePokemonDamage(pokemonEntity, isSpecial, 60);
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, boolean isSpecial, float movePower) {
        float attack = isSpecial ? pokemonEntity.getPokemon().getSpecialAttack() : pokemonEntity.getPokemon().getAttack();
        attack = Math.min(attack, 255.0f) / 255.0f;
        float moveModifier =isSpecial? movePower / 100 * CobblemonFightOrFlight.moveConfig().move_power_multiplier:1;//TODO Currently the physical moves has no special effect.
        float minDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().minimum_attack_damage;
        float maxDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().maximum_attack_damage;

        return Math.min(Mth.lerp(attack * moveModifier, minDmg, maxDmg), maxDmg);
    }

    public static void applyTypeEffect(PokemonEntity pokemonEntity, Entity hurtTarget) {
        if (pokemonEntity == null) {
            return;
        }
        Pokemon pokemon = pokemonEntity.getPokemon();
        int pkmLevel = pokemon.getLevel();
        ElementalType primaryType = pokemon.getPrimaryType();
        if (hurtTarget instanceof
                LivingEntity livingHurtTarget) {
            int effectStrength = Math.max(pkmLevel / 10, 1);

            switch (primaryType.getName()) {
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

    public static boolean pokemonAttack(PokemonEntity pokemonEntity, Entity hurtTarget, Mob mob) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        int pkmLevel = pokemon.getLevel();
        //float maxAttack = Math.max(pokemonEntity.getPokemon().getAttack(), pokemonEntity.getPokemon().getSpecialAttack());


        //LogUtils.getLogger().info("target took " + primaryType.getName() + " damage");


        float hurtDamage = calculatePokemonDamage(pokemonEntity, true);
        float hurtKnockback = 1.0f;
        ElementalType primaryType = pokemon.getPrimaryType();
        if (hurtTarget instanceof LivingEntity livingHurtTarget) {
            int effectStrength = Math.max(pkmLevel / 10, 1);

            switch (primaryType.getName()) {
                case "fire":
                    livingHurtTarget.setSecondsOnFire(effectStrength);
                    break;
                case "ice":
                    livingHurtTarget.setTicksFrozen(livingHurtTarget.getTicksFrozen() + effectStrength * 30);
                    break;
                case "poison":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.POISON, effectStrength * 20, 0), mob);
                    break;
                case "psychic":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.LEVITATION, effectStrength * 20, 0), mob);
                    break;
                case "fairy":
                case "fighting":
                case "steel":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, effectStrength * 20, 0), mob);
                    break;
                case "ghost":
                case "dark":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, (effectStrength + 2) * 25, 0), mob);
                    break;
                case "ground":
                case "rock":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, (effectStrength + 2) * 25, 0), mob);
                    break;
                case "electric":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (effectStrength + 2) * 25, 0), mob);
                    break;
                case "bug":
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.HUNGER, (effectStrength + 2) * 25, 0), mob);
                    break;
                case "grass":
                    pokemonEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (effectStrength + 2) * 20, 0), mob);
                    break;
                case "dragon":
                    hurtDamage = hurtDamage + 3;
                    break;
                case "flying":
                    hurtKnockback = hurtKnockback * 2;
                    break;
                case "water":
                    hurtKnockback = hurtKnockback * 2;
                    livingHurtTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (effectStrength + 2) * 25, 0), mob);
                    break;

                default:
                    break;
            }
            boolean flag = hurtTarget.hurt(mob.level().damageSources().mobAttack(mob), hurtDamage);
            if (flag) {
                if (hurtTarget instanceof LivingEntity) {
                    ((LivingEntity) hurtTarget).knockback(hurtKnockback * 0.5F, Mth.sin(mob.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(mob.getYRot() * ((float) Math.PI / 180F)));
                    mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }

                mob.setLastHurtMob(hurtTarget);
            }
            return flag;
        }
        return false;
    }
}
