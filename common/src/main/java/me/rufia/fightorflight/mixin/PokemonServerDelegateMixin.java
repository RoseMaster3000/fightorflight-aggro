package me.rufia.fightorflight.mixin;

import com.cobblemon.mod.common.api.entity.PokemonSideDelegate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonServerDelegate;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonServerDelegate.class)
public abstract class PokemonServerDelegateMixin implements PokemonSideDelegate {
    @Shadow
    public PokemonEntity entity;

    @Inject(method = "updateMaxHealth", at = @At("HEAD"), cancellable = true, remap = false)
    public void updateMaxHealthMixin(CallbackInfo ci) {
        //TODO try to find a way to make it work
        if (CobblemonFightOrFlight.commonConfig().shouldOverrideUpdateMaxHealthUpdate) {
            if (entity.getPokemon().getSpecies().getName().equals("shedinja")) {
                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0);
                entity.setHealth(1.0f);//if you can send it, it should be alive,right?
            } else {
                int hpStat = entity.getPokemon().getHp();//TODO don't forget to replace this one,this will be deprecated
                int minStat = CobblemonFightOrFlight.commonConfig().min_HP_required_stat;
                int midStat = CobblemonFightOrFlight.commonConfig().mid_HP_required_stat;
                int maxStat = CobblemonFightOrFlight.commonConfig().max_HP_required_stat;
                int stat = Mth.clamp(hpStat, minStat, maxStat);
                float minHealth = CobblemonFightOrFlight.commonConfig().min_HP;
                float midHealth = CobblemonFightOrFlight.commonConfig().mid_HP;
                float maxHealth = CobblemonFightOrFlight.commonConfig().max_HP;
                float health = minHealth;
                health = (float) (10 * Math.round(
                        stat < midStat ?
                                Mth.lerp((float) (stat - minStat) / (midStat - minStat), minHealth, midHealth) :
                                Mth.lerp((float) (maxStat - stat) / (maxStat - midStat), midHealth, maxHealth))) / 10;

                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
                entity.setHealth(health);
            }
            ci.cancel();
        }
    }
}
