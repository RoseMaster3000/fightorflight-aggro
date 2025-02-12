package me.rufia.fightorflight.data;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MoveData {
    public static final Map<String, List<MoveData>> moveData = new HashMap<>();
    private final String type;
    private final String target;
    private final float chance;
    private final boolean canActivateSheerForce;
    private String name;

    protected LivingEntity pickTarget(PokemonEntity pokemonEntity, LivingEntity hurtTarget) {
        if (target.equals("self")) {
            return pokemonEntity;
        } else if (target.equals("target")) {
            return hurtTarget;
        }
        return null;
    }

    protected boolean chanceTest(RandomSource source) {
        float rand = source.nextFloat();
        return chance > rand;
    }

    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public float getChance() {
        return chance;
    }

    public boolean canActivateSheerForce() {
        return canActivateSheerForce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int calculateEffectDuration(PokemonEntity pokemonEntity) {
        return Math.max(pokemonEntity.getPokemon().getLevel() / 10, 1);
    }

    public MoveData(String type, String target, float chance, boolean canActivateSheerForce, String name) {
        this.type = type;
        this.target = target;
        this.chance = chance;
        this.canActivateSheerForce = canActivateSheerForce;
        this.name = name;
    }

    public abstract void invoke(PokemonEntity pokemonEntity, LivingEntity target);
}
