package me.rufia.fightorflight.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.tags.CobblemonItemTags;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.evolution.requirements.LevelRequirement;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.config.FightOrFlightCommonConfigModel;
import me.rufia.fightorflight.item.ItemFightOrFlight;

public class FOFExpCalculator {
    public static int calculate(Pokemon battlePokemon,Pokemon opponentPokemon){
        /*
        * val baseExp = opponentPokemon.originalPokemon.form.baseExperienceYield
        val luckyEggMultiplier = if (battlePokemon.effectedPokemon.heldItemNoCopy().isIn(CobblemonItemTags.LUCKY_EGG)) Cobblemon.config.luckyEggMultiplier else 1.0
        val evolutionMultiplier = if (battlePokemon.effectedPokemon.evolutionProxy.server().any { evolution ->
            val requirements = evolution.requirements.asSequence()
            requirements.any { it is LevelRequirement } && requirements.all { it.check(battlePokemon.effectedPokemon) }
        }) 1.2 else 1.0
        val affectionMultiplier = if (battlePokemon.effectedPokemon.friendship >= 220) 1.2 else 1.0
        // that's us!
        val gimmickBoost = Cobblemon.config.experienceMultiplier
        val term4 = term1 * term2 * term3 + 1
        return (term4 * nonOtBonus * luckyEggMultiplier * evolutionMultiplier * affectionMultiplier * gimmickBoost).roundToInt()
        *
        * */
        float FOFExpMultiplier= CobblemonFightOrFlight.commonConfig().experience_multiplier;
        int baseExp=opponentPokemon.getForm().getBaseExperienceYield();
        int opponentLevel=opponentPokemon.getLevel();
        float term1 = (float) ((baseExp * opponentLevel) / 5.0);
        float victorLevel= battlePokemon.getLevel();
        float term2= (float) Math.pow ( ((2.0 * opponentLevel) + 10) / (opponentLevel + victorLevel + 10),2.5);
        boolean hasLuckyEgg=battlePokemon.heldItemNoCopy$common().is(CobblemonItemTags.LUCKY_EGG)||battlePokemon.heldItemNoCopy$common().is(ItemFightOrFlight.ORANLUCKYEGG.get());
        float luckyEggMultiplier= hasLuckyEgg? (float) Cobblemon.config.getLuckyEggMultiplier() :1.0f;
        float evolutionMultiplier=battlePokemon.getEvolutionProxy().server().stream().anyMatch(evolution -> {
            var requirements=evolution.getRequirements();
            return requirements.stream().anyMatch(evolutionRequirement -> evolutionRequirement instanceof LevelRequirement )&&requirements.stream().allMatch(evolutionRequirement -> evolutionRequirement.check(battlePokemon));
        })?1.2f:1.0f;
        float affectionMultiplier=battlePokemon.getFriendship()>=220?1.2f:1.0f;
        float gimmickBoost=Cobblemon.config.getExperienceMultiplier();
        float term3=term1*term2+1;
        return Math.round (term3* luckyEggMultiplier*evolutionMultiplier*affectionMultiplier*gimmickBoost*FOFExpMultiplier);
    }
}
