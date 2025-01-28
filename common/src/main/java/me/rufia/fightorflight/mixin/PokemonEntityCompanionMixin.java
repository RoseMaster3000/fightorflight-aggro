package me.rufia.fightorflight.mixin;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PokemonEntity.Companion.class)
public class PokemonEntityCompanionMixin {
    @Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
    private void addAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.ATTACK_DAMAGE, 1d));
    }
}
