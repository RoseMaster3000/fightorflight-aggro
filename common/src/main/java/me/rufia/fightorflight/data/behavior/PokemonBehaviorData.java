package me.rufia.fightorflight.data.behavior;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;
import java.util.Objects;

public abstract class PokemonBehaviorData {
    private final List<String> species;
    private final String form;
    private final String gender;
    private final List<String> biome;
    private final List<String> ability;
    private final List<String> move;
    private final List<String> nature;
    private final String levelRequirement;

    public PokemonBehaviorData(List<String> species, String form, String gender, List<String> ability, List<String> move, List<String> nature, List<String> biome, String levelRequirement) {
        this.species = species;
        this.form = form;
        this.ability = ability;
        this.gender = gender;
        this.move = move;
        this.nature = nature;
        this.biome = biome;
        this.levelRequirement = levelRequirement;
    }

    public boolean check(PokemonEntity pokemonEntity) {
        if (pokemonEntity != null) {
            return check(pokemonEntity.getPokemon());
            //&& checkItem(biome,pokemonEntity.level().getBiome(pokemonEntity.blockPosition()));
        }
        return false;
    }

    public boolean check(Pokemon pokemon) {
        if (pokemon == null) {
            return false;
        }
        return checkItem(species, pokemon.getSpecies().getName())
                && checkItem(form, pokemon.getForm().getName())
                && checkItem(gender, pokemon.getGender().toString())
                && checkItem(nature, pokemon.getNature().toString());
    }

    public boolean checkItem(String targetData, String pokemonData) {
        if (!targetData.isEmpty()) {
            return Objects.equals(targetData.toLowerCase(), pokemonData.toLowerCase());
        }
        return true;
    }

    public boolean checkItem(List<String> targetData, String pokemonData) {
        if (!targetData.isEmpty()) {
            return targetData.contains(pokemonData);
        }
        return true;
    }
}
