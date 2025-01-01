package me.rufia.fightorflight.mixin;


import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.entity.pokemon.ai.goals.PokemonFollowOwnerGoal;
import me.rufia.fightorflight.utils.PokemonUtils;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin is designed to disable the teleport.
//It seems quite hard to mixin the private function of the parent class,so we just edit the canUse
@Mixin(PokemonFollowOwnerGoal.class)
public abstract class PokemonFollowOwnerGoalMixin extends FollowOwnerGoal {
    public PokemonFollowOwnerGoalMixin(TamableAnimal tamable, double speedModifier, float startDistance, float stopDistance) {
        super(tamable, speedModifier, startDistance, stopDistance);
    }

    @Shadow(remap = false)
    public abstract PokemonEntity getEntity();

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    public void canUseMixin(CallbackInfoReturnable<Boolean> cir) {
        if (PokemonUtils.shouldDisableFollowOwner(getEntity())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    public void canContinueToUseMixin(CallbackInfoReturnable<Boolean> cir) {
        if (PokemonUtils.shouldDisableFollowOwner(getEntity())) {
            cir.setReturnValue(false);
        }
    }
}
