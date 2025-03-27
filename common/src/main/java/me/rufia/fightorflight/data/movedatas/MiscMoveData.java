package me.rufia.fightorflight.data.movedatas;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.data.MoveData;
import me.rufia.fightorflight.entity.PokemonAttackEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class MiscMoveData extends MoveData {
    public MiscMoveData(String target, String triggerEvent, float chance, boolean canActivateSheerForce, String name) {
        super("misc", target, triggerEvent, chance, canActivateSheerForce, name);
    }

    @Override
    public void invoke(PokemonEntity pokemonEntity, LivingEntity target) {
        if (Objects.equals("recharge_1_turn", getName()) || Objects.equals("charge_1_turn", getName())) {
            if (pokemonEntity.getTarget() != null) {
                int originalAttackTime = ((PokemonInterface) pokemonEntity).getAttackTime();
                PokemonAttackEffect.refreshAttackTime(pokemonEntity, originalAttackTime * 2);
            }
        }
    }
}
