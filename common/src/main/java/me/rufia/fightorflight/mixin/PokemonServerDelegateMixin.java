package me.rufia.fightorflight.mixin;

import com.cobblemon.mod.common.api.entity.PokemonSideDelegate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonServerDelegate;
import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.utils.PokemonUtils;
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
        if (CobblemonFightOrFlight.commonConfig().shouldOverrideUpdateMaxHealth) {
            if (entity.getPokemon().getSpecies().getName().equals("shedinja")) {
                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0);
                entity.setHealth(1.0f);//if you can send it, it should be alive,right?
            } else {
                int hpStat = entity.getPokemon().getHp();
                int currentHealth = entity.getPokemon().getCurrentHealth();
                float health = PokemonUtils.getMaxHealth(entity);
                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
                entity.setHealth(Math.round((float) currentHealth / hpStat * health));
            }
            ci.cancel();
        }
    }
}
