package me.rufia.fightorflight.data.movedatas;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.data.MoveData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class StatChangeMoveData extends MoveData {
    private int stage;

    public StatChangeMoveData(String target, float chance, boolean canActivateSheerForce, String name, int stage) {
        super("stat", target, chance, canActivateSheerForce, name);
        this.stage = stage;
    }

    private boolean isPositive() {
        return stage > 0 && stage < 7;
    }

    private boolean isNegative() {
        return stage < 0 && stage > -7;
    }

    @Override
    public void invoke(PokemonEntity pokemonEntity, LivingEntity target) {
        if (!chanceTest(pokemonEntity.getRandom()) || pokemonEntity.getPokemon().getAbility().getName().equals("sheerforce") && canActivateSheerForce()) {
            return;
        }
        LivingEntity finalTarget = pickTarget(pokemonEntity, target);
        if (finalTarget == null) {
            return;
        }
        //CobblemonFightOrFlight.LOGGER.info("Trying to apply stat related effect");
        if (Objects.equals(getName(), "attack") || Objects.equals(getName(), "special_attack")) {
            if (isPositive()) {
                finalTarget.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, calculateEffectDuration(pokemonEntity) * 20, stage - 1));
            } else if (isNegative()) {
                finalTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, calculateEffectDuration(pokemonEntity) * 20, stage + 1));
            }
        }
        if (Objects.equals(getName(), "speed")) {
            if (isPositive()) {
                finalTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, calculateEffectDuration(pokemonEntity) * 20, stage - 1));
            } else if (isNegative()) {
                finalTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, calculateEffectDuration(pokemonEntity) * 20, stage + 1));
            }
        }
    }
}
