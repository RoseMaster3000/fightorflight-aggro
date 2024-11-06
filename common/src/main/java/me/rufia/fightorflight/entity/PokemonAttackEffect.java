package me.rufia.fightorflight.entity;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.categories.DamageCategories;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, Entity target, boolean isSpecial) {
        return calculatePokemonDamage(pokemonEntity, target, isSpecial, (float) CobblemonFightOrFlight.moveConfig().base_power,null);
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, Entity target, boolean isSpecial, float movePower,ElementalType type) {
        int attack = isSpecial ? pokemonEntity.getPokemon().getSpecialAttack() : pokemonEntity.getPokemon().getAttack();
        int maxStat = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_special_attack_stat : CobblemonFightOrFlight.commonConfig().maximum_attack_stat;
        float attackModifier = (float) Math.min(attack, maxStat) / maxStat;
        float moveModifier = movePower / 100 * CobblemonFightOrFlight.moveConfig().move_power_multiplier;
        float minDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().minimum_attack_damage;
        float maxDmg = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_damage : CobblemonFightOrFlight.commonConfig().maximum_attack_damage;
        float multiplier = extraDamageFromEntityFeature(pokemonEntity,target,type);
        //CobblemonFightOrFlight.LOGGER.info(Float.toString(multiplier));
        PokemonInterface pokemonInterface = ((PokemonInterface) pokemonEntity);
        if (pokemonInterface.usingBeam() || pokemonInterface.usingSound() || pokemonInterface.usingMagic()) {
            multiplier *= CobblemonFightOrFlight.moveConfig().indirect_attack_move_power_multiplier;
        }
        float value = Math.min(Mth.lerp(attackModifier * moveModifier * multiplier, minDmg, maxDmg), maxDmg);
        //CobblemonFightOrFlight.LOGGER.info("value:{} minDmg:{} maxDmg:{} attack:{} attackModifier:{} moveModifier:{} multiplier:{}", value, minDmg, maxDmg, attack, attackModifier, moveModifier, multiplier);
        return value;
    }

    public static float calculatePokemonDamage(PokemonEntity pokemonEntity, Entity target, Move move) {
        //TODO Special effect for Photon Geyser
        if (move == null) {
            CobblemonFightOrFlight.LOGGER.info("Null move detected");
            return CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_damage;
        }
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
        return calculatePokemonDamage(pokemonEntity, target, isSpecial, (float) (move.getPower() * STAB),move.getType());
    }

    protected static float extraDamageFromEntityFeature(PokemonEntity pokemonEntity, Entity target, ElementalType moveType) {
        //TODO W.I.P.
        if(target.level().isClientSide || !(target instanceof LivingEntity livingEntity)){
            return 1.0f;
        }
        ElementalType type = moveType == null ? pokemonEntity.getPokemon().getPrimaryType() : moveType;
        if (!(livingEntity instanceof PokemonEntity targetPokemon)) {
            if (ElementalTypes.INSTANCE.getWATER().equals(type)) {
                if (livingEntity.isSensitiveToWater()) {
                    return CobblemonFightOrFlight.commonConfig().water_type_super_effective_dmg_multiplier;
                }
            }
            if (ElementalTypes.INSTANCE.getFIRE().equals(type)) {
                if (livingEntity.fireImmune()) {
                    return CobblemonFightOrFlight.commonConfig().fire_type_no_effect_dmg_multiplier;
                }
            }
            if (ElementalTypes.INSTANCE.getICE().equals(type)) {
                if (!livingEntity.canFreeze()) {
                    return CobblemonFightOrFlight.commonConfig().ice_type_no_effect_dmg_multiplier;
                }
                if(livingEntity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)){
                    return CobblemonFightOrFlight.commonConfig().ice_type_super_effective_dmg_multiplier;
                }
            }
            if(ElementalTypes.INSTANCE.getPOISON().equals(type)){
                if(livingEntity.getMobType()== MobType.UNDEAD){
                    return CobblemonFightOrFlight.commonConfig().poison_type_no_effect_dmg_multiplier;
                }
            }
        } else {
            //TODO type effectiveness here
            //I checked MoveInstruction, DamageInstruction and SuperEffectiveInstruction and found nothing.
            // I doubt that Cobblemon doesn't have the type effectiveness logic,it might be sent to showdown to process.I need to write it myself.
        }
        return 1.0f;
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
        String moveName = move.getName();
        int particleAmount = 4;
        boolean b1 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().self_angry_moves).toList().contains(moveName);
        boolean b2 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().target_soul_fire_moves).toList().contains(moveName);
        boolean b3 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().target_soul_moves).toList().contains(moveName);
        boolean b4 = Arrays.stream(CobblemonFightOrFlight.visualEffectConfig().slicing_moves).toList().contains(moveName);
        boolean b5 = Arrays.stream(CobblemonFightOrFlight.moveConfig().magic_attack_moves).toList().contains(moveName);
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
        if (b5) {
            makeMagicAttackParticle(pokemonEntity, hurtTarget);
        }
    }

    public static void makeMagicAttackParticle(PokemonEntity pokemonEntity, Entity target) {
        int particleAmount = 8;
        Move move = PokemonUtils.getRangeAttackMove(pokemonEntity);
        if (move == null) {
            return;
        }
        makeTypeEffectParticle(particleAmount, pokemonEntity, move.getType().getName());
        makeTypeEffectParticle(particleAmount, target, move.getType().getName());
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
        //These effects might stack, so a chain of ifs might be needed.
        boolean b1 = Arrays.stream(CobblemonFightOrFlight.moveConfig().switch_moves).toList().contains(move.getName());
        boolean b2 = Arrays.stream(CobblemonFightOrFlight.moveConfig().explosive_moves).toList().contains(move.getName());
        boolean b3 = Arrays.stream(CobblemonFightOrFlight.moveConfig().recoil_moves_allHP).toList().contains(move.getName());
        boolean b4 = Arrays.stream(CobblemonFightOrFlight.moveConfig().hp_draining_moves_50).toList().contains(move.getName());
        boolean b5 = Arrays.stream(CobblemonFightOrFlight.moveConfig().hp_draining_moves_75).toList().contains(move.getName());
        if (b1) {
            pokemonRecallWithAnimation(pokemonEntity);
        }
        if (b2) {
            pokemonExplode(pokemonEntity, level, move.getDamageCategory() == DamageCategories.INSTANCE.getSPECIAL());
        }
        if (b3) {
            pokemonRecoilSelf(pokemonEntity, 1.0f);
        }
        if (b4 || b5) {
            float dmg = calculatePokemonDamage(pokemonEntity, hurtTarget, move);
            boolean hasBigRoot = pokemonEntity.getPokemon().heldItem().is(CobblemonItems.BIG_ROOT);
            float percent = (b4 ? 0.5f : 0.75f) * (hasBigRoot ? 1.3f : 1.0f);
            pokemonEntity.heal(dmg * percent);
        }
    }

    public static void pokemonRecallWithAnimation(PokemonEntity pokemonEntity) {
        if (pokemonEntity.getOwner() != null) {
            pokemonEntity.recallWithAnimation();
        }
    }

    public static void pokemonExplode(PokemonEntity entity, Level level, boolean isSpecial) {
        if (!level.isClientSide) {
            Pokemon pokemon = entity.getPokemon();
            int power = isSpecial ? pokemon.getSpecialAttack() : pokemon.getAttack();
            level.explode(entity, level.damageSources().mobAttack(entity), null, entity.getX(), entity.getY(), entity.getZ(), getAoERadius(power) + 1, false, Level.ExplosionInteraction.MOB);
        }
    }

    public static void dealAoEDamage(PokemonEntity pokemonEntity, LivingEntity centerEntity, boolean isSpecial, boolean shouldHurtAlly) {
        Move move = isSpecial ? PokemonUtils.getRangeAttackMove(pokemonEntity) : PokemonUtils.getMeleeMove(pokemonEntity);
        if (move == null) {
            return;
        }
        double radius = getAoERadius(pokemonEntity, move);
        //CobblemonFightOrFlight.LOGGER.info(Double.toString(radius));
        List<LivingEntity> list = centerEntity.level().getEntitiesOfClass(LivingEntity.class, centerEntity.getBoundingBox().inflate(radius - centerEntity.getBbWidth() / 2));
        Iterator<LivingEntity> it = list.iterator();
        while (true) {
            LivingEntity livingEntity;
            do {
                if (!it.hasNext()) {
                    return;
                }
                livingEntity = it.next();
            } while (centerEntity.distanceToSqr(livingEntity) > 25.0);
            if (livingEntity == pokemonEntity || !shouldHurtAlly && livingEntity instanceof TamableAnimal animal && animal.getOwner().equals(pokemonEntity.getOwner())) {
                continue;
            }
            float dmgMultiplier = CobblemonFightOrFlight.moveConfig().min_AoE_damage_multiplier;

            //CobblemonFightOrFlight.LOGGER.info(livingEntity.getDisplayName().getString());
            boolean bl = livingEntity.hurt(centerEntity.damageSources().mobAttack(pokemonEntity), calculatePokemonDamage(pokemonEntity, livingEntity, move) * dmgMultiplier);
            if (bl) {
                applyTypeEffect(pokemonEntity, livingEntity);
                PokemonUtils.setHurtByPlayer(pokemonEntity, livingEntity);
                applyOnHitEffect(pokemonEntity, livingEntity, move);
                makeTypeEffectParticle(10, livingEntity, move.getType().getName());
            }
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

    public static double getAoERadius(PokemonEntity entity, Move move) {
        Pokemon pokemon = entity.getPokemon();
        boolean isSpecial = move.getDamageCategory().equals(DamageCategories.INSTANCE.getSPECIAL());
        int stat = isSpecial ? pokemon.getSpecialAttack() : pokemon.getAttack();
        int requiredStat = isSpecial ? CobblemonFightOrFlight.commonConfig().maximum_special_attack_stat : CobblemonFightOrFlight.commonConfig().maximum_attack_stat;
        return Math.min(Mth.lerp((float) stat / requiredStat, CobblemonFightOrFlight.moveConfig().min_AoE_radius, CobblemonFightOrFlight.moveConfig().max_AoE_radius), CobblemonFightOrFlight.moveConfig().max_AoE_radius);
    }

    public static boolean pokemonAttack(PokemonEntity pokemonEntity, Entity hurtTarget) {
        Pokemon pokemon = pokemonEntity.getPokemon();
        float hurtDamage;
        float hurtKnockback = 1f;
        Move move = PokemonUtils.getMeleeMove(pokemonEntity);

        if (move != null) {
            //CobblemonFightOrFlight.LOGGER.info(move.getName());
            boolean b1 = PokemonUtils.isExplosiveMove(move.getName());
            if (b1) {
                hurtDamage = 0f;

            } else {
                hurtDamage = calculatePokemonDamage(pokemonEntity, hurtTarget, move);
            }
            applyTypeEffect(pokemonEntity, hurtTarget, move.getType().getName());
            makeTypeEffectParticle(10, hurtTarget, move.getType().getName());
            PokemonUtils.updateMoveEvolutionProgress(pokemon, move.getTemplate());
            applyPostEffect(pokemonEntity, hurtTarget, move);
        } else {
            applyTypeEffect(pokemonEntity, hurtTarget);
            makeTypeEffectParticle(6, hurtTarget, pokemonEntity.getPokemon().getPrimaryType().getName());
            hurtDamage = calculatePokemonDamage(pokemonEntity, hurtTarget, false);
        }
        applyOnHitEffect(pokemonEntity, hurtTarget, move);
        PokemonUtils.setHurtByPlayer(pokemonEntity, hurtTarget);
        boolean flag = hurtTarget.hurt(((Mob) pokemonEntity).level().damageSources().mobAttack(pokemonEntity), hurtDamage);
        if (flag) {
            if (hurtTarget instanceof LivingEntity) {
                ((LivingEntity) hurtTarget).knockback(hurtKnockback * 0.5F, Mth.sin(pokemonEntity.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(pokemonEntity.getYRot() * ((float) Math.PI / 180F)));
                pokemonEntity.setDeltaMovement(pokemonEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }

            pokemonEntity.setLastHurtMob(hurtTarget);
        }
        return flag;
    }
}
