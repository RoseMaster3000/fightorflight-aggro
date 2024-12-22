package me.rufia.fightorflight;


import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import me.rufia.fightorflight.config.FightOrFlightCommonConfigModel;
import me.rufia.fightorflight.config.FightOrFlightMoveConfigModel;
import me.rufia.fightorflight.config.FightOrFlightVisualEffectConfigModel;
import me.rufia.fightorflight.goals.*;
import me.rufia.fightorflight.utils.PokemonUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CobblemonFightOrFlight {
    public static final String MODID = "fightorflight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    //public static final DeferredRegister<PokemonTracingBullet>;
    public static final float AUTO_AGGRO_THRESHOLD = 50.0f;
    private static FightOrFlightCommonConfigModel commonConfig;
    private static FightOrFlightMoveConfigModel moveConfig;
    private static FightOrFlightVisualEffectConfigModel visualEffectConfig;
    private static TriConsumer<PokemonEntity, Integer, Goal> goalAdder;

    public static FightOrFlightCommonConfigModel commonConfig() {
        return commonConfig;
    }

    public static FightOrFlightMoveConfigModel moveConfig() {
        return moveConfig;
    }

    public static FightOrFlightVisualEffectConfigModel visualEffectConfig() {
        return visualEffectConfig;
    }

    public static void init(TriConsumer<PokemonEntity, Integer, Goal> goalAdder) {
        CobblemonFightOrFlight.goalAdder = goalAdder;
        AutoConfig.register(FightOrFlightCommonConfigModel.class, JanksonConfigSerializer::new);
        AutoConfig.register(FightOrFlightMoveConfigModel.class, JanksonConfigSerializer::new);
        AutoConfig.register(FightOrFlightVisualEffectConfigModel.class, JanksonConfigSerializer::new);
        commonConfig = AutoConfig.getConfigHolder(FightOrFlightCommonConfigModel.class).getConfig();
        moveConfig = AutoConfig.getConfigHolder(FightOrFlightMoveConfigModel.class).getConfig();
        visualEffectConfig = AutoConfig.getConfigHolder(FightOrFlightVisualEffectConfigModel.class).getConfig();
    }

    public static void addPokemonGoal(PokemonEntity pokemonEntity) {
        float minimum_movement_speed = CobblemonFightOrFlight.commonConfig().minimum_movement_speed;
        float maximum_movement_speed = CobblemonFightOrFlight.commonConfig().maximum_movement_speed;
        float speed_limit = CobblemonFightOrFlight.commonConfig().speed_stat_limit;
        float speed = pokemonEntity.getPokemon().getSpeed();
        float speedMultiplier = Mth.lerp(speed / speed_limit, minimum_movement_speed, maximum_movement_speed);

        float fleeSpeed = 1.3f * speedMultiplier;
        float pursuitSpeed = 1.2f * speedMultiplier;

        goalAdder.accept(pokemonEntity,3,new PokemonGoToPosGoal(pokemonEntity,pursuitSpeed));
        goalAdder.accept(pokemonEntity, 3, new PokemonMeleeAttackGoal(pokemonEntity, pursuitSpeed, true));
        goalAdder.accept(pokemonEntity, 3, new PokemonRangedAttackGoal(pokemonEntity, pursuitSpeed, PokemonUtils.getAttackRadius()));
        //goalAdder.accept(pokemonEntity,3,new PokemonPassiveAbilityGoal(pokemonEntity));
        goalAdder.accept(pokemonEntity, 3, new PokemonAvoidGoal(pokemonEntity, PokemonUtils.getAttackRadius()*3, 1.0f, fleeSpeed));
        goalAdder.accept(pokemonEntity, 4, new PokemonPanicGoal(pokemonEntity, fleeSpeed));

        goalAdder.accept(pokemonEntity, 1, new PokemonCommandedTargetGoal<>(pokemonEntity, LivingEntity.class, false));
        goalAdder.accept(pokemonEntity, 2, new PokemonOwnerHurtByTargetGoal(pokemonEntity));
        goalAdder.accept(pokemonEntity, 3, new PokemonOwnerHurtTargetGoal(pokemonEntity));
        goalAdder.accept(pokemonEntity, 3, new PokemonTauntedTargetGoal<>(pokemonEntity, PokemonEntity.class, false));
        goalAdder.accept(pokemonEntity, 4, new HurtByTargetGoal(pokemonEntity));
        goalAdder.accept(pokemonEntity, 5, new CaughtByTargetGoal(pokemonEntity));
        goalAdder.accept(pokemonEntity, 6, new PokemonNearestAttackableTargetGoal<>(pokemonEntity, Player.class, PokemonUtils.getAttackRadius()*3, true, true));
        goalAdder.accept(pokemonEntity, 6, new PokemonProactiveTargetGoal<>(pokemonEntity, Mob.class, 5, false, false, (arg) -> arg instanceof Enemy && !(arg instanceof Creeper)));

    }

    public static double getFightOrFlightCoefficient(PokemonEntity pokemonEntity) {
        if (!CobblemonFightOrFlight.commonConfig().do_pokemon_attack) {
            return -100;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();
        String speciesName = pokemon.getSpecies().getName().toLowerCase();
        if (SpeciesAlwaysAggro(speciesName)) {
            return 100;
        }
        if (SpeciesNeverAggro(speciesName) || SpeciesAlwaysFlee(speciesName)) {
            return -100;
        }
        float levelMultiplier = CobblemonFightOrFlight.commonConfig().aggression_level_multiplier;
        double pkmnLevel = levelMultiplier * pokemon.getLevel();
        double lowStatPenalty = (pkmnLevel * 1.5) + 30;
        double levelAggressionCoefficient = (pokemon.getAttack() + pokemon.getSpecialAttack()) - lowStatPenalty;
        double atkDefRatioCoefficient = (pokemon.getAttack() + pokemon.getSpecialAttack()) - (pokemon.getDefence() + pokemon.getSpecialDefence());
        double natureAggressionCoefficient = 0;
        double darknessAggressionCoefficient = 0;
        double intimidateCoefficient = 0;
        switch (pokemon.getNature().getDisplayName().toLowerCase()) {
            case "cobblemon.nature.docile":
            case "cobblemon.nature.timid":
            case "cobblemon.nature.gentle":
            case "cobblemon.nature.careful":
                natureAggressionCoefficient = -2;
                break;
            case "cobblemon.nature.relaxed":
            case "cobblemon.nature.lax":
            case "cobblemon.nature.quiet":
            case "cobblemon.nature.bashful":
            case "cobblemon.nature.calm":
                natureAggressionCoefficient = -1;
                break;
            case "cobblemon.nature.sassy":
            case "cobblemon.nature.hardy":
            case "cobblemon.nature.bold":
            case "cobblemon.nature.impish":
            case "cobblemon.nature.hasty":
                natureAggressionCoefficient = 1;
                break;
            case "cobblemon.nature.brave":
            case "cobblemon.nature.rash":
            case "cobblemon.nature.adamant":
            case "cobblemon.nature.naughty":
                natureAggressionCoefficient = 2;
                break;
            default:
                natureAggressionCoefficient = 0;
                break;
        }

        var pokemons = pokemonEntity.level().getEntitiesOfClass(PokemonEntity.class, AABB.ofSize(pokemonEntity.position(), 18, 18, 18), (pokemonEntity1) -> pokemonEntity1.getOwner() != null && Arrays.stream(CobblemonFightOrFlight.commonConfig().aggro_reducing_abilities).toList().contains(pokemonEntity1.getPokemon().getAbility().getName()));

        if (!pokemons.isEmpty()) {
            intimidateCoefficient = -30;
        }
        ElementalType typePrimary = pokemon.getPrimaryType();
        ElementalType typeSecondary = pokemon.getSecondaryType();
        if (typeSecondary == null) {
            typeSecondary = typePrimary;
        }

        boolean ghostLightLevelModifier = CobblemonFightOrFlight.commonConfig().ghost_light_level_aggro && (typePrimary.getName() == "ghost" || typeSecondary.getName() == "ghost");
        boolean darkLightLevelModifier = CobblemonFightOrFlight.commonConfig().dark_light_level_aggro && (typePrimary.getName() == "dark" || typeSecondary.getName() == "dark");

        if (ghostLightLevelModifier || darkLightLevelModifier) {
            int skyDarken = ((Entity) pokemonEntity).level().getSkyDarken();
            //LogUtils.getLogger().info(pokemon.getSpecies().getName() + " skyDarken: " + skyDarken);
            int lightLevel = ((Entity) pokemonEntity).level().getRawBrightness(pokemonEntity.blockPosition(), skyDarken);
            //LogUtils.getLogger().info(pokemon.getSpecies().getName() + " Raw Brightness: " + lightLevel);
            if (lightLevel <= 7) {
                darknessAggressionCoefficient = pkmnLevel;
            } else if (lightLevel >= 12) {
                darknessAggressionCoefficient -= pkmnLevel;
            }
        }

        //Weights and Clamps:
        levelAggressionCoefficient = Math.max(-(pkmnLevel + 5), Math.min(pkmnLevel, 1.5d * levelAggressionCoefficient));//5.0d * levelAggressionCoefficient;
        atkDefRatioCoefficient = Math.max(-pkmnLevel, 1.0d * atkDefRatioCoefficient);
        natureAggressionCoefficient = (pkmnLevel * 0.5) * natureAggressionCoefficient;//25.0d * natureAggressionCoefficient;
        double finalResult = levelAggressionCoefficient + atkDefRatioCoefficient + natureAggressionCoefficient + darknessAggressionCoefficient + intimidateCoefficient;

        return finalResult;
    }

    public static boolean SpeciesAlwaysAggro(String speciesName) {
        //LogUtils.getLogger().info("Are " + speciesName + " always aggro?");
        for (String aggroSpecies : CobblemonFightOrFlight.commonConfig().always_aggro) {
            if (aggroSpecies.equals(speciesName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean SpeciesNeverAggro(String speciesName) {
        for (String passiveSpecies : CobblemonFightOrFlight.commonConfig().never_aggro) {
            if (passiveSpecies.equals(speciesName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean SpeciesAlwaysFlee(String speciesName) {
        return Arrays.stream(commonConfig().always_flee).toList().contains(speciesName);
    }

    public static void PokemonEmoteAngry(Mob mob) {
        double particleSpeed = Math.random();
        double particleAngle = Math.random() * 2 * Math.PI;
        double particleXSpeed = Math.cos(particleAngle) * particleSpeed;
        double particleYSpeed = Math.sin(particleAngle) * particleSpeed;

        if (mob.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                    mob.position().x, mob.getBoundingBox().maxY, mob.position().z,
                    1,
                    particleXSpeed, 0.5d, particleYSpeed,
                    1.0f);
        } else {
            mob.level().addParticle(ParticleTypes.ANGRY_VILLAGER,
                    mob.position().x, mob.getBoundingBox().maxY, mob.position().z,
                    particleXSpeed, 0.5d, particleYSpeed);
        }
    }
}