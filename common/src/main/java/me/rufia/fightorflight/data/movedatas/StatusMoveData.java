package me.rufia.fightorflight.data.movedatas;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.data.MoveData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class StatusMoveData extends MoveData {
    public StatusMoveData(String target, float chance, boolean canActivateSheerForce, String name) {
        super("status", target, chance, canActivateSheerForce, name);
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
        int duration = calculateEffectDuration(pokemonEntity);
        if (Objects.equals(getName(), "poison")) {
            finalTarget.addEffect(new MobEffectInstance(MobEffects.POISON, duration * 30, 0));
        } else if (Objects.equals(getName(), "sleep")) {
            finalTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 25, 2));
            finalTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration * 25, 1));
        } else if (Objects.equals(getName(), "freeze")) {
            finalTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 25, 2));
            finalTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration * 25, 1));
            finalTarget.setTicksFrozen(finalTarget.getTicksFrozen() + duration);
        } else if (Objects.equals(getName(), "paralysis")) {
            finalTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration * 30, 0));
            finalTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration * 30, 0));
        } else if (Objects.equals(getName(), "burn")) {
            finalTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration * 30, 0));
            finalTarget.setRemainingFireTicks(duration * 30);
        }
    }
}
