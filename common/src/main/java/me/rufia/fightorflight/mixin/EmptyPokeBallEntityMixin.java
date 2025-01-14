package me.rufia.fightorflight.mixin;

import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.mixin.pokeball.ThrownEntityMixin;
import me.rufia.fightorflight.PokemonInterface;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmptyPokeBallEntity.class)
public abstract class EmptyPokeBallEntityMixin extends ThrowableProjectile {
    @Shadow(remap = false)
    private PokemonEntity capturingPokemon;

    protected EmptyPokeBallEntityMixin(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "breakFree", at = @At("HEAD"), remap = false)
    private void breakFreeInject(CallbackInfo ci) {
        if (capturingPokemon != null && this.getOwner() != null) {
            ((PokemonInterface) capturingPokemon).setCapturedBy(this.getOwner().getId());
        }
    }
}
