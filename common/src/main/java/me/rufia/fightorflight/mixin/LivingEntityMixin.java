package me.rufia.fightorflight.mixin;

import me.rufia.fightorflight.CobblemonFightOrFlight;
import me.rufia.fightorflight.effects.FOFEffects;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(Holder<MobEffect> effect);

    @ModifyArg(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float updateDamageAmount(DamageSource damageSource, float damageAmount) {
        if (damageSource != null) {
            if (!damageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                int amp = -1;
                var effect = getEffect(FOFEffects.RESISTANCE_WEAKENED);
                if (effect != null) {
                    CobblemonFightOrFlight.LOGGER.info("EFFECT DETECTED!");
                    amp = effect.getAmplifier();
                }
                if (amp > 3) {
                    amp = 3;
                }
                return damageAmount * 5 / (4 - amp);
            }
        }
        return damageAmount;
    }
}
