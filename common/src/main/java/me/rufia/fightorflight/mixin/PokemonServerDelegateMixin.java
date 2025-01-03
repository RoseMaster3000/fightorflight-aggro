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
    @Shadow(remap = false)
    public PokemonEntity entity;

    @Inject(method = "updateMaxHealth", at = @At("HEAD"), cancellable = true, remap = false)
    public void updateMaxHealthMixin(CallbackInfo ci) {
        //Attention: this mixin influences the init of max health and the health sync from pokemon to pokemon entity.
        if (CobblemonFightOrFlight.commonConfig().shouldOverrideUpdateMaxHealth) {
            if (entity.getPokemon().getSpecies().getName().equals("shedinja")) {
                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1.0);
                entity.setHealth(1.0f);//if you can send it, it should be alive,right?
            } else {
                int hpStat = entity.getPokemon().getMaxHealth();
                int currentHealth = entity.getPokemon().getCurrentHealth();
                int entityMaxHealth = PokemonUtils.getMaxHealth(entity);
                boolean notUpdated = entityMaxHealth != 20 && entity.getMaxHealth() == 20;//Attention: I'm not sure what bugs it might cause currently
                float newHealth = entity.getOwner() == null && CobblemonFightOrFlight.commonConfig().health_sync_for_wild_pokemon ? (notUpdated ? entityMaxHealth : entity.getHealth()) : Math.round((float) currentHealth / hpStat * entityMaxHealth);
                entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(entityMaxHealth);
                entity.setHealth(currentHealth > 0 && newHealth == 0 ? 1 : newHealth);
            }
            ci.cancel();
        }
    }
}
