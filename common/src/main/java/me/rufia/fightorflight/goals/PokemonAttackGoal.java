package me.rufia.fightorflight.goals;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.activestate.ShoulderedState;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.PokemonInterface;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class PokemonAttackGoal extends Goal {
    private int ticksUntilNewAngerParticle = 0;
    private int ticksUntilNewAngerCry = 0;
    protected PokemonEntity getPokemonEntity() {
        return null;
    }

    protected void setAttackTime(int i) {
        PokemonEntity pokemonEntity = getPokemonEntity();
        if (pokemonEntity == null) {
            return;
        }
        ((PokemonInterface) (Object) pokemonEntity).setAttackTime(i);
    }

    protected int getAttackTime() {
        PokemonEntity pokemonEntity = getPokemonEntity();
        if (pokemonEntity == null) {
            return -1;
        }
        return ((PokemonInterface) (Object) pokemonEntity).getAttackTime();
    }

    protected void resetAttackTime(double d) {
        PokemonEntity pokemonEntity = getPokemonEntity();
        float attackSpeedModifier = Math.max(0.1f, 1 - pokemonEntity.getSpeed() / CobblemonFightOrFlight.commonConfig().speed_stat_limit);
        float f = (float) Math.sqrt(d) / PokemonUtils.getAttackRadius() * attackSpeedModifier;
        //this.attackTime = Mth.floor(20 * Mth.lerp(f, CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_interval, CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_interval));
        setAttackTime(Mth.floor(20 * Mth.lerp(f, CobblemonFightOrFlight.commonConfig().minimum_ranged_attack_interval, CobblemonFightOrFlight.commonConfig().maximum_ranged_attack_interval)));
    }

    @Override
    public void tick(){
        if(getPokemonEntity()==null){
            return;
        }
        PokemonEntity pokemonEntity=getPokemonEntity();
        LivingEntity owner=pokemonEntity.getOwner();
        if (owner == null) {
            if (ticksUntilNewAngerParticle < 1) {
                CobblemonFightOrFlight.PokemonEmoteAngry(pokemonEntity);
                ticksUntilNewAngerParticle = 10;
            } else {
                ticksUntilNewAngerParticle = ticksUntilNewAngerParticle - 1;
            }

            if (ticksUntilNewAngerCry < 1) {
                pokemonEntity.cry();
                ticksUntilNewAngerCry = 100 + (int) (Math.random() * 200);
            } else {
                ticksUntilNewAngerCry = ticksUntilNewAngerCry - 1;
            }
        }
    }

    @Override
    public boolean canUse(){
        if(PokemonUtils.moveCommandAvailable(getPokemonEntity())){
            return false;
        }
        if(getPokemonEntity().getPokemon().getState() instanceof ShoulderedState){
            return false;
        }
        return true;
    }
}
